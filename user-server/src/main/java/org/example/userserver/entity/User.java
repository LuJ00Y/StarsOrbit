package org.example.userserver.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data//一键实现gets/sets方法
//@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
//    private String roles;
//     正确命名 boolean 字段
    private Boolean role;//1=管理员，0等于普通用户
    private Boolean enabled;

    public User() {}

    // 全参构造函数
    public User(String username, String password, String email, Boolean role, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role != null ? role : false;
        this.enabled = enabled != null ? enabled : true;
    }
}