package org.example.todoserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.todoserver.entity.TodoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
@Mapper
public interface TodoMapper extends CrudRepository<TodoItem, Long> {

    @Select("SELECT * from `todo_item`")
    List<TodoItem> findAll();

    List<TodoItem> findByUserIdAndDeletedFalse(Long userId);

    List<TodoItem> findByUserId(Long userId);

    int softDelete(Long id, Long userId);

    int softDeleteCompletedByUserId(Long userId);

    int softDeleteAllByUserId(Long userId);

    List<TodoItem> findByUserIdAndCompleteTrueAndDeletedFalse(Long userId);
}