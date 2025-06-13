package org.example.todoserver.controller;

import com.github.pagehelper.PageInfo;
import org.example.common.Result;
import org.example.todoserver.entity.TodoItem;
import org.example.todoserver.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/todo")
public class TodoRestController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/all")
    public @ResponseBody List<TodoItem> getAll() {
        List<TodoItem> todoList = todoService.findAll();
        return todoList;
    }
    /**
     * 用户清单的模糊查找
     * 需要有用户ID
     * */
    @GetMapping("/selectPage")
    public Result findTodoByNameOrType(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = true) Long userId ){
        PageInfo<TodoItem> pageInfo= todoService.selectPage(pageNum,pageSize,keyword,userId);
        if(pageInfo == null) {
            return Result.error();
        }
        return Result.success(pageInfo);
    }
    /**
     * 获取某用户所有
     * 待办事项*/
    @GetMapping("/useritem/{userId}")
    public ResponseEntity<List<TodoItem>> getUserTodos(@PathVariable Long userId) {
        List<TodoItem> todos = todoService.findByUserId(userId);
        return ResponseEntity.ok(todos); // 直接返回列表
    }

}
