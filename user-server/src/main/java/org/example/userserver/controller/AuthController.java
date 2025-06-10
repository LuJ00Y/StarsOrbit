package org.example.userserver.controller;

import jakarta.mail.MessagingException;
import org.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    // 请求密码重置
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) throws MessagingException {
        String email = request.get("email");
        userService.requestPasswordReset(email);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "如果邮箱存在，重置链接已发送至您的邮箱"
        ));
    }

    // 重置密码
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (userService.resetPassword(token, newPassword)) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "密码已成功重置"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "密码重置失败：令牌无效或已过期"
            ));
        }
    }
}