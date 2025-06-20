package org.example.todoserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.todoserver.entity.TodoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

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

    int insertTodo(TodoItem todoItem);

//    int updateTodoName(TodoItem existing);
    int updateTodoName(TodoItem todoItem);

    int updateTodoType(TodoItem existing);

    Optional<Object> findTodoById(Long id);

    // Mapper 接口
    int toggleTodoStatus(@Param("id") Long id, @Param("complete") Boolean complete);

    TodoItem getTodoById(Long id);

    List<TodoItem> findTodoByNameOrType(String keyword, Long userId);

    int updateTodo(TodoItem existing);
}