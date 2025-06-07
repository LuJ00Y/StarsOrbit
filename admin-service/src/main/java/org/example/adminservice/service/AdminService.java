package org.example.adminservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private TodoServiceClient todoServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    public List<TodoItem> getAllTodos() {
        return todoServiceClient.getAllTodos();
    }

    public void restoreTodo(Long id) {
        todoServiceClient.restoreTodo(id);
    }

    public List<User> getAllUsers() {
        return userServiceClient.getAllUsers();
    }
}
