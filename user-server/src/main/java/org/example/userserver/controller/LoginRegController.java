package org.example.userserver.controller;

import org.example.common.Result;
import org.example.userserver.entity.User;
import org.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created by sang on 2017/12/17.
 */
@RestController
public class LoginRegController {

    @Autowired
    UserService userService;

//    @RequestMapping("/login_error")
//    public Result loginError() {
//        return new Result("error", "登录失败!");
//    }
//
//    @RequestMapping("/login_success")
//    public Result loginSuccess() {
//        return new Result("success", "登录成功!");
//    }

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     * <p>
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return
     */
//    @RequestMapping("/login")
//    public Result loginPage() {
//        return new Result("error", "尚未登录，请登录!");
//    }

    @PostMapping("/loginByEmail")
    public ResponseEntity<Result> loginByEmail(@RequestBody User user) {
        try {
            int result = userService.login(user);
            switch (result) {
                case 0:
                    return ResponseEntity.ok(new Result("success", "登录成功!"));
                case 1:
                    return ResponseEntity.badRequest().body(new Result("error", "用户不存在"));
                case 2:
                    return ResponseEntity.internalServerError().body(new Result("error", "密码或用户名错误!"));
                case 4:
                    return ResponseEntity.badRequest().body(new Result("error", "密码不能为空!"));
                case 5:
                    return ResponseEntity.badRequest().body(new Result("error", "用户名不能为空!"));
                default:
                    return ResponseEntity.internalServerError().body(new Result("error", "未知错误"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Result("error", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Result> reg(@RequestBody User user) { // 添加 @RequestBody
        try {
            int result = userService.register(user);
            switch (result) {
                case 0:
                    return ResponseEntity.ok(new Result("success", "注册成功!"));
                case 1:
                    return ResponseEntity.badRequest().body(new Result("error", "邮箱已存在"));
                case 2:
                    return ResponseEntity.internalServerError().body(new Result("error", "注册失败"));
                case 3:
                    return ResponseEntity.badRequest().body(new Result("error", "密码强度不足!"));
                case 4: // 新增密码为空错误
                    return ResponseEntity.badRequest().body(new Result("error", "密码不能为空!"));
                default:
                    return ResponseEntity.internalServerError().body(new Result("error", "未知错误"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Result("error", e.getMessage()));
        }
    }

}
