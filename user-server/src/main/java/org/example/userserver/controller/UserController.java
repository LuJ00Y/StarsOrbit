package org.example.userserver.controller;


import jakarta.annotation.Resource;
import org.example.userserver.config.RegisterStatus;
import org.example.userserver.entity.User;
import org.example.userserver.mapper.UserMapper;
import org.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping(value = "/add")
    public String addAccount() {
        return "this is user-server";
    }

    @Autowired
    private UserService userService;

    @Resource
    UserMapper userMapper;

    @GetMapping("/all")
    public List<User> getUser() {
        return userMapper.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        // 设置默认值
        if (user.isAdmin() == null) {
            user.setAdmin(false);
        }

        userMapper.save(user);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "id", user.getId()
        ));
    }

    @PostMapping("/update")
    public String updateUser(@RequestBody User user) {
        userMapper.updateUserById(user);
        return "success";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        int status = userService.register(user);

        if (status == RegisterStatus.SUCCESS) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "注册成功",
                    "userId", user.getId()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "code", status,
                    "message", RegisterStatus.getMessage(status)
            ));
        }
    }


//    // 获取当前用户的待办事项
//    @GetMapping("/todos")
//    public String getUserTodos(Model model, Authentication authentication) {
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);
//        List<TodoItem> todos = todoRepository.findByUserAndDeletedFalse(user);
//        model.addAttribute("items", todos);
//        return "user/todos";
//    }
//
//    // 添加新待办
//    @PostMapping("/add")
//    public String addTodo(@ModelAttribute TodoItem todoItem,
//                          Authentication authentication) {
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);
//        todoItem.setUser(user);
//        todoRepository.save(todoItem);
//        return "redirect:/user/todos";
//    }
//
//    // 软删除待办
//    @PostMapping("/delete/{id}")
//    public String deleteTodo(@PathVariable Long id) {
//        todoRepository.softDelete(id);
//        return "redirect:/user/todos";
//    }


}
