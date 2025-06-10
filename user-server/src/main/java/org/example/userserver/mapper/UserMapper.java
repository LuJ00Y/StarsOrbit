package org.example.userserver.mapper;

import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.*;
import org.example.userserver.entity.PasswordResetToken;
import org.example.userserver.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * from `user`")
    List<User> findAll();//用来查询ALL对象

    // 通过邮箱查询用户
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    //用户添加
    @Insert("INSERT INTO `user` (`username`, `password`, `email`, `isAdmin`,`enabled`) VALUES (#{username}, #{password} ,#{email}, #{isAdmin},#{enabled})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(User user);
    // MyBatis 要求插入操作的返回类型应该是 void (小写关键字) 或 int (表示受影响的行数)。

    //用户更新
    @Update("UPDATE user set username=#{username},password=#{password},email=#{email} where id=#{id}")
    @Transactional
    void updateUserById(User user);

    //用户删除

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    void updatePassword(User user);

    // 用于令牌查询
    @Select("SELECT * FROM password_reset_token WHERE user_id = #{userId}")
    PasswordResetToken findByUserId(Long userId);

    //通过用户名查询
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Update("UPDATE `tododb`.`user` SET `enabled` = FALSE WHERE `id` = #{id};")
    void deleteById(Long id);

    @Select("SELECT * FROM user WHERE " +
            "username = #{username} " +
            "or id = #{id} " +
            "or email = #{email} " +
            "or isAdmin=#{isAdmin} " +
            "or enabled=#{enabled}")
    List<User> selectlist(User user);

    @Insert("INSERT INTO `tododb`.`user` (`username`, `password`, `email`, `isAdmin`, `enabled`) " +
            "VALUES (#{username},#{password},#{email},false,true);")
    void addUser(User user);

    @Insert("INSERT INTO `tododb`.`user` (`username`, `password`, `email`, `isAdmin`, `enabled`) " +
            "VALUES (#{username},#{password},#{email},true,true);")
    void addAdmin(User user);

    @Update("UPDATE `tododb`.`user` SET `enabled` = FALSE;")
    int deleteAll();

    @Insert("INSERT INTO `tododb`.`user` (`username`, `password`, `email`, `isAdmin`, `enabled`)" +
            "VALUES (#{username},#{password},#{email},false,true);")
    int register(User user);
}
