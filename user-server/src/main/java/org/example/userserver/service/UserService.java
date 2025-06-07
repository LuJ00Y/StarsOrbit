package org.example.userserver.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;//法一
//import org.apache.ibatis.mapping.Environment;
import org.example.userserver.entity.PasswordResetToken;
import org.example.userserver.entity.User;
import org.example.userserver.mapper.PasswordResetTokenMapper;
import org.example.userserver.mapper.UserMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


@Service
public class UserService {

    @Value("${app.base-url:http://localhost:8080}")//法二
    private String appBaseUrl;

//    @Autowired
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenMapper tokenMapper;
    private final JavaMailSender mailSender;
//    private final Environment env;
    private final TemplateEngine templateEngine;

//    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, PasswordResetTokenMapper tokenMapper, JavaMailSender mailSender, Environment env, TemplateEngine templateEngine) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenMapper = tokenMapper;
        this.mailSender = mailSender;
//        this.env = env;
        this.templateEngine = templateEngine;
    }

    //修改用户信息
    public void partialUpdateUser(Long id, Map<String, Object> updates) {
        User user = new User();
        user.setId(id);

        if (updates.containsKey("username")) {
            user.setUsername((String) updates.get("username"));
        }
        if (updates.containsKey("password")) {
            user.setPassword((String) updates.get("password"));
        }
        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }

        userMapper.updateUserById(user);
    }

    /**
     * 注册新用户
     * @param user
     * @return 0表示成功
     * 1表示用户重复
     * 2表示失败
     * 3表示密码不合格
     */
    public int register(User user) {
        // 检查邮箱是否已存在
        User existingUser = userMapper.findByEmail(user.getEmail());
        if (existingUser != null) {
            return 1; // 邮箱已存在
        }
        // 设置默认值
        if (user.getIsAdmin() == null) {
            user.setIsAdmin(false);
        }
        user.setEnabled(true); // 用户可用

        if (!isPasswordStrong(user.getPassword())) {
            return 3; // 密码强度不足
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户
        int result = userMapper.save(user);

        // 返回注册结果
        return result > 0 ? 0 : 2; // 0=成功, 2=失败
    }
    private boolean isPasswordStrong(String password) {
        // 至少8个字符，包含大小写字母、数字和特殊字符
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }


    // 新增密码找回方法
    public void requestPasswordReset(String email) throws MessagingException {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            // 出于安全考虑，不告知用户邮箱不存在
            return;
        }

        // 生成唯一令牌
        String token = UUID.randomUUID().toString();

        // 创建或更新重置令牌
        PasswordResetToken resetToken = tokenMapper.findByUserId(user.getId());
        if (resetToken != null) {
            resetToken.setToken(token);
            resetToken.setExpiryDate(new Date());
            resetToken.setUsed(false);
            tokenMapper.updateToken(resetToken);
        } else {
            resetToken = new PasswordResetToken(user, token);
            tokenMapper.saveToken(resetToken);
        }

        // 发送密码重置邮件
        sendPasswordResetEmail(user.getEmail(), token);
    }
    // 重置密码
    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenMapper.findByToken(token);

        if (resetToken == null || resetToken.isUsed() || resetToken.isExpired()) {
            return false;
        }

        // 更新用户密码
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updatePassword(user);

        // 标记令牌为已使用
        resetToken.setUsed(true);
        tokenMapper.updateToken(resetToken);

        return true;
    }
    private void sendPasswordResetEmail(String email, String token) {
        String resetUrl = appBaseUrl + "/auth/reset-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply@example.com");
            helper.setTo(email);
            helper.setSubject("密码重置请求");

            // 使用模板创建HTML内容
            Context context = new Context();
            context.setVariable("resetUrl", resetUrl);
            // 处理模板
            String htmlContent = templateEngine.process("password-reset-email", context);

            helper.setText(htmlContent, true); // true表示HTML内容

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送失败", e);
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
//    // 在TodoService中限制查询范围
//    public List<TodoItem> findByUserId(Long userId) {
//        return repository.findByUserId(userId);
//    }
//
//    public List<TodoItem> findAll() {
//        return repository.findAll();
//    }

}
