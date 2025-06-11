package org.example.userserver.controller;


import lombok.Data;
import org.example.userserver.config.RegisterStatus;
import org.example.userserver.entity.User;
import org.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController//表示接口查询出来的all方法都会被渲染成为JSON
@RequestMapping(value="/user")//controller的url地址
@RefreshScope
public class UserController {
    @Value("${config.info}")
    private String configInfo;

    //测试
    @GetMapping(value = "/test")
    public String test() {
        return "this is user-server";
    }

    @GetMapping("/test/getConfigInfo")
    public String getConfigInfo(){
        return configInfo;
    }


    @Autowired
    private UserService userService;

    /**
     * 动态更新用户信息
     * */
    // 请求DTO
    @Data
    public static class UserUpdateRequest {
        private String username;
        private String email;
        private String password;
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {

        User user = new User();
        user.setId(id);

        // 只设置需要更新的字段
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }

        int updated = userService.updateUser(user);

        if (updated > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
