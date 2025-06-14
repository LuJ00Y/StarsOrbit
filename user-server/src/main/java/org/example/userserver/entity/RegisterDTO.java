package org.example.userserver.entity;

import lombok.Data;

// RegisterDTO.java
@Data
public class RegisterDTO {
//    @NotBlank(message = "用户名不能为空")
    private String username;

//    @Email(message = "邮箱格式不正确")
    private String email;

//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
//            message = "密码必须至少8个字符，包含大小写字母、数字和特殊字符")
    private String password;

//    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    private Boolean agree;
}