package org.example.todoserver.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.todoserver.entity.TodoItem;
import org.example.todoserver.dto.TodoListViewModel;
import org.example.todoserver.repository.TodoItemRepository;

import org.example.todoserver.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @Autowired//用于与数据库交互
    //通过@Autowired注解，Spring会自动注入这个repository的实例，用于处理TodoItem的持久化操作。
//    private TodoItemRepository repository;
    private TodoService todoService;

    // 每个方法都使用了不同的注解（@GetMapping和@PostMapping），这表明它们处理不同类型的HTTP请求。

    @GetMapping("/user/{userId}")
    public List<TodoItem> getUserTodos(@PathVariable Long userId) {
        return todoService.getUserTodos(userId);
    }

    @PostMapping("/")
    public TodoItem addTodo(@RequestBody TodoItem todoItem) {
        return todoService.addTodo(todoItem);
    }

    @DeleteMapping("/{id}/user/{userId}")
    public void deleteTodo(@PathVariable Long id, @PathVariable Long userId) {
        todoService.softDelete(id, userId);
    }

    @PutMapping("/{id}")
    public TodoItem updateTodo(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        return todoService.updateTodo(id, todoItem);
    }

//    //	第一个方法，GET方法，处理首页请求
//    @GetMapping("/")
//    public String index(Model model) {
//        Iterable<TodoItem> todoList = repository.findAll();
//        model.addAttribute("items", new TodoListViewModel(todoList));
//        //通过repository.findAll()获取所有的TodoItem，
//        // 然后将它们封装到TodoListViewModel中，并添加到Model对象里
//        model.addAttribute("newitem", new TodoItem());//添加了一个新的TodoItem对象到Model中，用于表单提交新项
//        return "index";
//    }
//    //处理添加新待办项
//    @PostMapping("/add")
//    public String add(@ModelAttribute TodoItem requestItem) {
//        TodoItem item = new TodoItem(requestItem.getCategory(),requestItem.getName());
//        repository.save(item);
//        return "redirect:/";
//    }
//    //用户提交的表单数据会被绑定到requestItem对象上。
//    // 控制器创建一个新的TodoItem实例，使用请求中的分类和名称，并通过repository保存到数据库。
//    // 保存完成后，重定向到根路径，刷新页面显示新添加的项。
//
//    //	处理批量更新待办项状态
//    @PostMapping("/update")
//    public String update(@ModelAttribute TodoListViewModel requestItems) {
//        for (TodoItem requestItem : requestItems.getTodoList() ) {
//            TodoItem item = new TodoItem(requestItem.getCategory(), requestItem.getName());
//            item.setComplete(requestItem.isComplete());
//            item.setId(requestItem.getId());
//            repository.save(item);
//        }
//        return "redirect:/";
//    }


//        // 获取当前用户的待办事项
//    @GetMapping("/todos")
//    public String getUserTodos(Model model, Authentication authentication) {
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);
//        List<TodoItem> todos = repository.findByUserAndDeletedFalse(user);
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
//        repository.save(todoItem);
//        return "redirect:/user/todos";
//    }
//
//    // 软删除待办
//    @PostMapping("/delete/{id}")
//    public String deleteTodo(@PathVariable Long id) {
//        repository.softDelete(id);
//        return "redirect:/user/todos";
//    }
}
