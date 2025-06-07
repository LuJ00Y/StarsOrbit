package org.example.adminservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/todos")
    public List<TodoItem> getAllTodos() {
        return adminService.getAllTodos();
    }

    @PostMapping("/todos/{id}/restore")
    public void restoreTodo(@PathVariable Long id) {
        adminService.restoreTodo(id);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }
}
