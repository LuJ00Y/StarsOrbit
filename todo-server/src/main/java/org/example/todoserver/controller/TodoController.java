package org.example.todoserver.controller;

import org.example.todoserver.entity.TodoItem;

import org.example.todoserver.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todos")
@RefreshScope
public class TodoController {
    @Value("${config.info}")
    private String configInfo;
    @GetMapping("/test")
    public String test(){
        return "this is list-server";
    }
    @GetMapping("/test/getConfigInfo")
    public String getConfigInfo(){
        return configInfo;
    }


    @Autowired//用于与数据库交互// 通过@Autowired注解，Spring会自动注入这个repository的实例，用于处理TodoItem的持久化操作。
    private TodoService todoService;

    // 每个方法都使用了不同的注解（@GetMapping和@PostMapping），这表明它们处理不同类型的HTTP请求。可以返回数值、json等数据

    //展示用户all存在的清单
//    @GetMapping("/show/{userId}")
//    public String index(Model model, @PathVariable long userId) {
//        Iterable<TodoItem> todoList = todoMapper.findByUserIdAndDeletedFalse(userId);//获取所有的TodoItem
//        model.addAttribute("items", new TodoListViewModel(todoList));
//        //然后将它们封装到TodoListViewModel中，并添加到Model对象里
//        model.addAttribute("newitem", new TodoItem());
//        //添加了一个新的TodoItem对象到Model中，用于表单提交新项
//        return "index";
//    }
    /**
     * 查询当个用户的信息
     */
    @GetMapping("/{userId}")
    public List<TodoItem> getTodos(@PathVariable Long userId) {

        List<TodoItem> todos = todoService.getTodos(userId);
        return todos;
    }
    /**
     * 获取用户所有
     * 未删除的
     * 待办事项*/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TodoItem>> getUserTodos(@PathVariable Long userId) {
        List<TodoItem> todos = todoService.getUserTodos(userId);
//        return ResponseEntity.ok((TodoItem) todos);
        return ResponseEntity.ok(todos); // 直接返回列表
    }

//    //添加新的清单
//    @PostMapping("/add")
//    public String add(@ModelAttribute TodoItem requestItem) {
//        TodoItem item = new TodoItem(requestItem.getCategory(),requestItem.getName());
//        todoService.save(item);
//        return "redirect:/";
//    }
    // 添加新待办事项
    @PostMapping("/add")
    public ResponseEntity<Integer> insertTodo(@RequestBody TodoItem todoItem) {
        int newTodo = todoService.insertTodo(todoItem);
        return ResponseEntity.status(201).body(newTodo);
    }

//    @PostMapping("/update")
//    public String update(@ModelAttribute TodoListViewModel requestItems) {
//        for (TodoItem requestItem : requestItems.getTodoList() ) {
//            TodoItem item = new TodoItem(requestItem.getCategory(), requestItem.getName());
//            item.setComplete(requestItem.isComplete());
//            item.setId(requestItem.getId());
//            todoMapper.save(item);
//        }
//        return "redirect:/";
//    }
    // 更新待办事项
//    @PutMapping("/{id}")
//    public ResponseEntity<TodoItem> updateTodoName(@PathVariable Long id, @RequestBody TodoItem todoItem) {
//        TodoItem updatedTodo = todoService.updateTodoName(id, todoItem);
//        return ResponseEntity.ok(updatedTodo);
//    }
    @PutMapping("/updateName/{id}")
    public ResponseEntity<TodoItem> updateTodoName(
            @PathVariable Long id,
            @RequestBody TodoItem todoItem
    ) {
        // 从Header获取用户ID    @RequestHeader("X-User-Id") Long userId

        // 设置请求体中的用户ID（确保不为null）
        todoItem.setUserId(1L);
//        todoItem.getUserId();

        TodoItem updatedTodo = todoService.updateTodoName(id, todoItem);
        return ResponseEntity.ok(updatedTodo);
    }
    @PutMapping("/updateType/{id}")
    public ResponseEntity<TodoItem> updateTodoType(
            @PathVariable Long id,
            @RequestBody TodoItem todoItem
    ) {
        // 从Header获取用户ID    @RequestHeader("X-User-Id") Long userId

        // 设置请求体中的用户ID（确保不为null）
        todoItem.setUserId(1L);
//        todoItem.getUserId();

        TodoItem updatedTodo = todoService.updateTodoType(id, todoItem);
        return ResponseEntity.ok(updatedTodo);
    }

    // 统一更新接口
    @PutMapping("/updateNameOrType/{id}")
    public ResponseEntity<TodoItem> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoItem todoItem,
            @RequestHeader("X-User-Id") Long userId
    ) {
        todoItem.setUserId(userId);
        TodoItem updatedTodo = todoService.updateTodo(id, todoItem);
        return ResponseEntity.ok(updatedTodo);
    }

    //tag
    // 切换任务完成状态
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoItem> toggleTodoStatus(@PathVariable Long id) {
        int updatedTodo = todoService.toggleTodoStatus(id);
        if (updatedTodo > 0) {
            TodoItem updatedItem = todoService.getTodoById(id);
            return ResponseEntity.ok(updatedItem);

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 删除单个待办事项
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, @PathVariable Long userId) {
        todoService.softDeleteTodo(id, userId);
        return ResponseEntity.noContent().build();
    }
    // 清除已完成待办事项
    @DeleteMapping("/completed/user/{userId}")
    public ResponseEntity<Integer> clearCompletedTodos(@PathVariable Long userId) {
        int deletedCount = todoService.clearCompletedTodos(userId);
        return ResponseEntity.ok(deletedCount);
    }

    // 清除所有待办事项
    @DeleteMapping("/all/user/{userId}")
    public ResponseEntity<Integer> clearAllTodos(@PathVariable Long userId) {
        int deletedCount = todoService.clearAllTodos(userId);
        return ResponseEntity.ok(deletedCount);
    }

    // 获取待办事项统计
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<TodoService.TodoStats> getTodoStats(@PathVariable Long userId) {
        TodoService.TodoStats stats = todoService.getTodoStats(userId);
        return ResponseEntity.ok(stats);
    }


//    /**
//     * 查询当个用户的信息
//     */
//    @PostMapping("/")
//    public TodoItem addTodo(@RequestBody TodoItem todoItem) {
//        return todoService.addTodo(todoItem);
//    }
//
//    @DeleteMapping("/{id}/user/{userId}")
//    public void deleteTodo(@PathVariable Long id, @PathVariable Long userId) {
//        todoService.softDelete(id, userId);
//    }
//
//    @PutMapping("/{id}")
//    public TodoItem updateTodoName(@PathVariable Long id, @RequestBody TodoItem todoItem) {
//        return todoService.updateTodoName(id, todoItem);
//    }
//
}
