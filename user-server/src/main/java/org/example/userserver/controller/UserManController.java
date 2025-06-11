package org.example.userserver.controller;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Lang;
import org.example.common.Result;
import org.example.userserver.entity.User;
import org.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.List;

@RestController//表示接口查询出来的所有方法都会被渲染成为JSON
@RequestMapping("/admin")
@RefreshScope
public class UserManController {
    @Autowired
    private UserService userService;


    /**查找所有用户*/
    @GetMapping("/all")
    public List<User> getUser(String keyword) {
        return userService.findAll(keyword);
    }

    /**查找单个用户*/
    @GetMapping("/user")
    public ResponseEntity<User> getOneUser(@RequestParam(required = false) String username,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(required = false) Long id,
                                           @RequestParam(required = false) Boolean role,
                                           @RequestParam(required = false) Boolean enabled){
        User query = new User();
        query.setUsername(username);
        query.setEmail(email);
        query.setId(id);
        query.setRole(role);
        query.setEnabled(enabled);

        List<User> users = userService.getOneUser(query);

        if(users == null || users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        if(users.size() > 1) {
//            log.warn("多个用户匹配查询条件: username={}, email={}", username, email);
//        }
        // 如果有多个匹配项，返回第一个（或根据业务逻辑选择）
        return new ResponseEntity<>(users.get(0), HttpStatus.OK);
    }

    /**查找多个用户*/
    @GetMapping("/userlist")
    public ResponseEntity<List<User>> getUserList(@RequestParam(required = false) String username,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(required = false) Long id,
                                                  @RequestParam(required = false) Boolean role,
                                                  @RequestParam(required = false) Boolean enabled){
        User query =new User();
        query.setUsername(username);
        query.setEmail(email);
        query.setId(id);
        query.setRole(role);
        query.setEnabled(enabled);

        List<User> users = userService.getUserList(query);

        if(users == null || users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * 查找当页数据
     * pageNum：当前页码
     * pageSize：每页个数
     * 能根据用户的role属性选择展示数据
     * */
    @GetMapping("/selectPage")
    public Result selectPage(
                             @RequestParam(defaultValue = "1") int pageNum,
                             @RequestParam(defaultValue = "10") int pageSize,
                             @RequestParam(required = false) String keyword) {
        PageInfo<User> pageInfo= userService.selectPage(pageNum,pageSize,keyword);
        if(pageInfo == null) {
            return Result.error();
        }
        return Result.success(pageInfo);
    }
    @GetMapping("/selectPageUser")
    public Result selectPageUser(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        PageInfo<User> pageInfo= userService.selectPageUser(pageNum,pageSize,keyword);
        if(pageInfo == null) {
            return Result.error();
        }
        return Result.success(pageInfo);
    }
    @GetMapping("/selectPageAdmin")
    public Result selectPageAdmin(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        PageInfo<User> pageInfo= userService.selectPageAdmin(pageNum,pageSize,keyword);
        if(pageInfo == null) {
            return Result.error();
        }
        return Result.success(pageInfo);
    }

    /**
     * 增加用户- 仅管理员
     * 返回结果为影响行数
     * */
    @PostMapping("/addUser")
    @PreAuthorize("hasRole('ADMIN')") // 方法级安全控制
    public Result addUser(@RequestBody User user) {
//        if (!user.getRole()) {
//            return Result.error("权限不足");
//        }
        userService.addUser(user);
        return Result.success();
    }
    /**
     * 增加管理员- 仅管理员
     * 返回结果为影响行数
     * */
    @PostMapping("/addAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public Result addAdmin(@RequestBody User user) {
//        if (!user.getRole()) {
//            return Result.error("权限不足");
//        }
        userService.addAdmin(user);
        return Result.success();
    }


    /**软删除用户 - 仅管理员
     *删除单个
     * */
    @DeleteMapping("/userdelete/one/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public Result delete(@PathVariable("id") Long id) {
//        User user = userService.findById(id);
//        if (!user.getRole()) {
//            return Result.error("权限不足");
//        }
        userService.deleteById(id);
        return Result.success();
    }
    /**软删除用户 - 仅管理员
     *删除all
     * */
    @DeleteMapping("/userdelete/all/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteAll(@PathVariable("id") Long id) {
        User user = userService.findById(id);
//        if (!user.getRole()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
        int deleteCount = userService.deleteAll();
        return deleteCount > 0
                ? ResponseEntity.ok(deleteCount)
                : ResponseEntity.noContent().build();
    }

/**
 *
 * 批量删除。
 * 后端必须用@RequestBody来接受数组集合
 * */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        userService.deleteBatch(ids);
        return Result.success();
    }


}
