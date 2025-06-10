package org.example.userserver.controller;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.example.common.Result;
import org.example.userserver.entity.User;
import org.example.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//表示接口查询出来的所有方法都会被渲染成为JSON
@RequestMapping("/admin")
@RefreshScope
public class UserManController {
    @Autowired
    private UserService userService;


    /**查找所有用户*/
    @GetMapping("/all")
    public List<User> getUser() {
        return userService.findAll();
    }
    //查找单个用户
//    @GetMapping("/user")
//    public ResponseEntity<User> selectOneUser(@RequestParam long id) {
//        User user = userService.findById(id);
//        if(user == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
    /**查找单个用户*/
    @GetMapping("/user")
    public ResponseEntity<User> selectList(User user) {
        List<User> list =userService.selectlist(user);
        if(list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * 查找当页数据
     * pageNum：当前页码
     * pageSize：每页个数
     * */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") int pageNum,
                             @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<User> pageInfo= userService.selectPage(pageNum,pageSize);
        if(pageInfo == null) {
            return Result.error();
        }
        return Result.success(pageInfo);
    }

    /**
     * 增加用户
     * 返回结果为影响行数
     * */
    @GetMapping("/addUser")
    public Result addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.success();
    }
    /**
     * 增加管理员
     * 返回结果为影响行数
     * */
    @GetMapping("/addAdmin")
    public Result addAdmin(@RequestBody User user) {
        userService.addAdmin(user);
        return Result.success();
    }


    /**软删除用户
     *删除单个
     * */
    @DeleteMapping("/userdelete/{id}")
    public Result delete(@PathVariable("id") long id) {
        userService.deleteById(id);
        return Result.success();
    }
    /**软删除用户
     *删除all
     * */
    @DeleteMapping("/userdelete/all")
    public ResponseEntity<Integer> delete() {
        int deleteCount = userService.deleteAll();
        if(deleteCount <=0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(deleteCount, HttpStatus.OK);
        }
    }




}
