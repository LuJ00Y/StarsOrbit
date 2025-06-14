package org.example.userserver.controller;

import org.example.common.Result;
import org.example.userserver.entity.RegisterDTO;
import org.example.userserver.entity.User;
import org.example.userserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created by sang on 2017/12/17.
 */
@RestController
public class LoginRegController {

    private static final Logger log = LoggerFactory.getLogger(LoginRegController.class);
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

/**
 * 登录逻辑——不返回data*/
//    @PostMapping("/loginByEmail")
//    public Result loginByEmail(@RequestBody User user) {
//        try {
//            int result = userService.login(user);
//            switch (result) {
//                case 0:
//                    return Result.success();
//                case 1:
//                    return new Result("error", "用户不存在");
//                case 2:
//                    return new Result("error", "密码或用户名错误!");
//                case 4:
//                    return new Result("error", "密码不能为空!");
//                case 5:
//                    return new Result("error", "用户名不能为空!");
//                default:
//                    return new Result("error", "未知错误");
//            }
//        } catch (IllegalArgumentException e) {
//            return new Result("error", e.getMessage());
//        }
//    }
    @PostMapping("Login")
    public Result login(@RequestBody User user) {
        User dbuser =userService.loginByEmail(user);
        return Result.success(dbuser);
    }

    @PostMapping("/register")
    public ResponseEntity<Result> reg( @RequestBody RegisterDTO registerDTO) { // 添加 @RequestBody
        try {
            // 检查密码匹配
            if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Result.error("400", "两次输入的密码不一致"));
            }

            // 检查是否同意条款
            if (registerDTO.getAgree() == null || !registerDTO.getAgree()) {
                return ResponseEntity.badRequest().body(Result.error("400", "请同意服务条款"));
            }


            // 创建用户对象
            User user = new User();
            user.setUsername(registerDTO.getUsername());
            user.setEmail(registerDTO.getEmail());
            user.setPassword(registerDTO.getPassword());

            // 调用服务
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
