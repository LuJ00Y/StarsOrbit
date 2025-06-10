package org.example.userserver.entity;

//import javax.persistence.*;

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
    private Boolean isAdmin = false;
    private Boolean enabled = true;

    // 正确的布尔属性访问方法
    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    // 正确的布尔属性访问方法
    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

//     Lombok 不会自动生成布尔属性的标准 getter
//     所以我们需要显式定义
    public User() {}

    // 全参构造函数
    public User(String username, String password, String email, Boolean isAdmin, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin != null ? isAdmin : false;
        this.enabled = enabled != null ? enabled : true;
    }
}