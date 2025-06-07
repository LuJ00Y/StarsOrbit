package org.example.todoserver.service;

import jakarta.transaction.Transactional;
import org.example.todoserver.entity.TodoItem;
import org.example.todoserver.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TodoService {

    @Autowired
    private TodoItemRepository repository;

    public List<TodoItem> getUserTodos(Long userId) {
        return repository.findByUserIdAndDeletedFalse(userId);
    }

    public TodoItem addTodo(TodoItem todoItem) {
        todoItem.setDeleted(false);
        return repository.save(todoItem);
    }

    @Transactional
    public void softDelete(Long id, Long userId) {
        repository.softDelete(id, userId);
    }

    public TodoItem updateTodo(Long id, TodoItem todoItem) {
        TodoItem existing = repository.findById(id).orElseThrow();
        existing.setName(todoItem.getName());
        existing.setCategory(todoItem.getCategory());
        existing.setComplete(todoItem.isComplete());
        return repository.save(existing);
    }
}
