package org.example.userserver.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.common.Result;
import org.example.exception.DuplicateEmailException;
import org.example.exception.DuplicateUsernameException;
import org.example.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;//法一
//import org.apache.ibatis.mapping.Environment;
import org.example.userserver.entity.PasswordResetToken;
import org.example.userserver.entity.User;
import org.example.userserver.mapper.PasswordResetTokenMapper;
import org.example.userserver.mapper.UserMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;
import java.util.UUID;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


@Service
public class UserService {

    private final View error;
    @Value("${app.base-url:http://localhost:9080}")//法二
    private String appBaseUrl;
//    @Autowired
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
//    private final PasswordResetTokenMapper tokenMapper;
    private final JavaMailSender mailSender;
//    private final Environment env;
    private final TemplateEngine templateEngine;

//    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);


    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, JavaMailSender mailSender, Environment env, TemplateEngine templateEngine, View error) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
//        this.tokenMapper = tokenMapper;
        this.mailSender = mailSender;
//        this.env = env;
        this.templateEngine = templateEngine;
        this.error = error;
    }


    public List<User> findAll(String keyword) {
        return userMapper.findAll(keyword);
    }
    public void addUser(User user) {
        userMapper.addUser(user);
    }
    public void addAdmin(User user) {
        userMapper.addAdmin(user);
    }
    public User findById(long id) {
        return userMapper.findById(id);
    }

    public int login(User user) {
        // 1. 验证必要字段
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        // 2. 密码非空检查
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return 4; // 密码为空错误码
        }
        // 检查邮箱是否存在
        User existingUser = userMapper.findByEmail(user.getEmail());
        if (existingUser == null) {
            return 1; // 用户不存在
        }
        // 检查用户是否死亡
        if (!existingUser.getEnabled()) {
            return 1; // 用户不存在
        }
        //检查密码是否对应
        if(!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return 2;//密码或用户名错误!
        }
        return 0;
    }

    /**
     * 注册新用户
     * @param user
     * @return 0表示成功
     * 1表示用户重复
     * 2表示失败
     * 3表示密码强度不足
     */
    public int register(User user) {
        // 1. 验证必要字段
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        // 2. 密码非空检查
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return 4; // 新增密码为空错误码
        }

        // 检查邮箱是否已存在
        User existingUser = userMapper.findByEmail(user.getEmail());
        if (existingUser != null) {
            return 1; // 邮箱已存在
        }
        // 设置默认值
        if (user.getRole() == null) {
            user.setRole(false);
        }
        user.setEnabled(true); // 用户可用

        // 5. 密码强度检查
        if (!isPasswordStrong(user.getPassword())) {
            return 3; // 密码强度不足
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户
        int result = userMapper.register(user);
        // 返回注册结果
        return result > 0 ? 0 : 2; // 0=成功, 2=失败
    }
    private boolean isPasswordStrong(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        // 至少8个字符，包含大小写字母、数字和特殊字符
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
//        return password.matches(pattern);
        if (password.length() < 8) {
            return false;
        }

        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isUpperCase(c)) hasUpper = true;
            if ("@#$%^&+=!".indexOf(c) >= 0) hasSpecial = true;
        }

        return hasDigit && hasLower && hasUpper && hasSpecial;
    }



//    public void save(User user) {
//        userMapper.save(user);
//    }

    public List<User> getOneUser(User user) {
        return userMapper.getOneUser(user);
    }

    public List<User> getUserList(User query) {
        return userMapper.getOneUser(query);
    }

    public PageInfo<User> selectPage(int pageNum, int pageSize,String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list=userMapper.findAll(keyword);
        return PageInfo.of(list);
    }

    public PageInfo<User> selectPageUser(int pageNum, int pageSize,String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list=userMapper.findEnabledUser(keyword);
        return PageInfo.of(list);
    }

    public PageInfo<User> selectPageAdmin(int pageNum, int pageSize,String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list=userMapper.findEnabledAdmin(keyword);
        return PageInfo.of(list);
    }
    public void deleteById(Long id) {
//        userMapper.deleteById(userMapper.findAll().get(0).getId());
        userMapper.deleteById(id);
    }
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            this.deleteById(id);
        }
    }
    public int deleteAll() {
        return userMapper.deleteAll();
    }


    /**
     *
     * */
    public int updateUser(User user) {

        User existing = userMapper.findById(user.getId());
        if (existing == null) {
            throw new UserNotFoundException("用户不存在");
        }
        // 验证用户名唯一性（即使相同也检查，因为可能被其他用户占用）
        if (user.getUsername() != null) {
            // 只有当用户名实际变化时才检查
            if (!user.getUsername().equals(existing.getUsername())) {
                int count = userMapper.countByUsernameExcludingId(
                        user.getUsername(),
                        user.getId()
                );
                if (count > 0) {
                    throw new DuplicateUsernameException("用户名已存在");
                }
            }
        }

        if (user.getEmail() != null) {
            // 只有邮箱变化时才需要检查唯一性
            if (!user.getEmail().equals(existing.getEmail())) {
                int count = userMapper.countByEmailExcludingId(
                        user.getEmail(),
                        user.getId()
                );
                if (count > 0) {
                    throw new DuplicateEmailException("邮箱已被使用");
                }
            }
        }

        // 密码加密（如果提供新密码）
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(encodePassword(user.getPassword()));
        }
        return userMapper.updateUser(user);
    }


    private String encodePassword(String rawPassword) {
        // 使用 BCrypt 或其他加密算法
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }


}


//    @Transactional
//    public void deleteUser(Long userId) {
//        // 1. 删除用户
//        userRepository.deleteById(userId);
//
//        // 2. 发布用户删除事件
//        UserDeletedEvent event = new UserDeletedEvent(userId);
//        eventPublisher.publishEvent(event);
//    }

