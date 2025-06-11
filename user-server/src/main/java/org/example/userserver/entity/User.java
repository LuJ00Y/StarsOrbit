package org.example.userserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;


@Data//一键实现gets/sets方法
//@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String email;
//    private String roles;
//     正确命名 boolean 字段
    private Boolean role;//1=管理员，0等于普通用户
    private Boolean enabled;//是否被删除，是软删除tag，0为被删除

    public User() {}

    // 全参构造函数
    public User(String username, String password, String email, Boolean role, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role != null ? role : false;
        this.enabled = enabled != null ? enabled : true;
    }

    // 密码加密方法
    public void encryptPassword() {
        if (this.password != null && !this.password.isEmpty()) {
            this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
        }
    }

    // 密码验证方法
    public boolean checkPassword(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.password);
    }
}