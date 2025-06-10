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
    @Column(name = "isAdmin")
    private Boolean admin;
    private Boolean enabled;

    public User() {}

    // 全参构造函数
    public User(String username, String password, String email, Boolean admin, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin != null ? admin : false;
        this.enabled = enabled != null ? enabled : true;
    }
}