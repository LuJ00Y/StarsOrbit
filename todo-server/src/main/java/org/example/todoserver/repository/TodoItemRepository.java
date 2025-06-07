package org.example.todoserver.repository;

import org.example.todoserver.entity.TodoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {

//    List<TodoItem> findByUserAndDeletedFalse(org.example.userserver.entity.User user);

    List<TodoItem> findByUserIdAndDeletedFalse(Long userId);

    void softDelete(Long id, Long userId);
}