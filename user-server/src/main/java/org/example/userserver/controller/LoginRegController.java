package org.example.userserver.controller;

import org.example.common.Result;
import org.example.userserver.entity.User;
import org.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by sang on 2017/12/17.
 */
@RestController
public class LoginRegController {

    @Autowired
    UserService userService;

    @RequestMapping("/login_error")
    public Result loginError() {
        return new Result("error", "登录失败!");
    }

    @RequestMapping("/login_success")
    public Result loginSuccess() {
        return new Result("success", "登录成功!");
    }

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     * <p>
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return
     */
    @RequestMapping("/login")
    public Result loginPage() {
        return new Result("error", "尚未登录，请登录!");
    }

    @PostMapping("/register")
    public Result reg(User user) {
        int result = userService.register(user);
        if (result == 0) {
            //成功
            return new Result("success", "注册成功!");
        } else if (result == 1) {
            //失败
            return new Result("error", "邮箱已存在");
        }else if (result == 3) {
            //失败
            return new Result("error", "密码强度不足!");
        }else {
            //失败
            return new Result("error", "注册失败!");
        }
    }

}
