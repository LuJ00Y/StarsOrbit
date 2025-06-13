package org.example.todoserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.example.todoserver.entity.TodoItem;
import org.example.todoserver.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TodoService {

    @Autowired
    private TodoMapper todoMapper;

    //用户存活的清单
    public List<TodoItem> getUserTodos(Long userId) {
        return todoMapper.findByUserIdAndDeletedFalse(userId);
    }
    //用户所有创建过的清单
    public List<TodoItem> findByUserId(Long userId) {
        return todoMapper.findByUserId(userId);
    }
    public TodoItem addTodo(TodoItem todoItem) {
        todoItem.setDeleted(false);
        return todoMapper.save(todoItem);
    }

    @Transactional
    public void softDelete(Long id, Long userId) {
        todoMapper.softDelete(id, userId);
    }

    public TodoItem updateTodoName(Long id, TodoItem todoItem) {
        // 1. 验证输入
        if (todoItem.getUserId() == null) {
            throw new IllegalArgumentException("用户未登录");
        }

        // 2. 获取现有待办项
        TodoItem existing = (TodoItem) todoMapper.findTodoById(id)
                .orElseThrow(() -> new EntityNotFoundException("没有找到id为" + id + "的待办事项"));
/**
//        // 3. 处理旧数据中的 null user_id
//        if (existing.getUserId() == null) {
//            // 记录警告并修复数据
//            log.warn("Fixing null user_id for todo id: {}", id);
//            existing.setUserId(todoItem.getUserId());
//            todoMapper.fixNullUserId(id, todoItem.getUserId());
//        }*/

        // 4. 验证用户权限
//        if (!existing.getUserId().equals(todoItem.getUserId())) {
//            throw new SecurityException("当前没有权限修改待办事项");
//        }

        // 5. 更新字段
        existing.setName(todoItem.getName());
        existing.setCategory(todoItem.getCategory());
        existing.setComplete(todoItem.isComplete());

        // 6. 执行更新
        int updated = todoMapper.updateTodoName(existing);
        if (updated == 0) {
            throw new RuntimeException("id为" + id+"的待办事项更新失败");
        }

        return existing;
    }

    public TodoItem updateTodoType(Long id, TodoItem todoItem) {
        // 1. 验证输入
        if (todoItem.getUserId() == null) {
            throw new IllegalArgumentException("用户未登录");
        }

        // 2. 获取现有待办项
        TodoItem existing = (TodoItem) todoMapper.findTodoById(id)
                .orElseThrow(() -> new EntityNotFoundException("没有找到id为" + id + "的待办事项"));
/**
 //        // 3. 处理旧数据中的 null user_id
 //        if (existing.getUserId() == null) {
 //            // 记录警告并修复数据
 //            log.warn("Fixing null user_id for todo id: {}", id);
 //            existing.setUserId(todoItem.getUserId());
 //            todoMapper.fixNullUserId(id, todoItem.getUserId());
 //        }*/

        // 4. 验证用户权限
//        if (!existing.getUserId().equals(todoItem.getUserId())) {
//            throw new SecurityException("当前没有权限修改待办事项");
//        }

        // 5. 更新字段
        existing.setName(todoItem.getName());
        existing.setCategory(todoItem.getCategory());
        existing.setComplete(todoItem.isComplete());

        // 6. 执行更新
        int updated = todoMapper.updateTodoType(existing);
        if (updated == 0) {
            throw new RuntimeException("id为" + id+"的待办事项更新失败");
        }

        return existing;
    }

    public TodoItem save(TodoItem item) {
        return todoMapper.save(item);
    }

    // 切换任务完成状态
    public int toggleTodoStatus(Long id) {
        TodoItem todoItem = (TodoItem) todoMapper.findTodoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("没有找到id为" + id+"的待办事项"));

        // 获取当前状态并切换
        boolean newStatus = !todoItem.isComplete();
        // 直接更新数据库
        int updated = todoMapper.toggleTodoStatus(id, newStatus);

        if (updated == 0) {
            throw new RuntimeException("状态更新失败，ID: " + id);
        }
        // 更新实体状态（可选，保持一致性）
        todoItem.setComplete(newStatus);
        todoItem.setUpdatedAt(LocalDateTime.now());

        return updated;
    }

    // 软删除单个待办事项
    @Transactional
    public void softDeleteTodo(Long id, Long userId) {
        int updated = todoMapper.softDelete(id, userId);
        if (updated == 0) {
            throw new ResourceNotFoundException("TodoItem not found with id: " + id + " and userId: " + userId);
        }
    }

    // 清除所有待办事项
    @Transactional
    public int clearAllTodos(Long userId) {
        return todoMapper.softDeleteAllByUserId(userId);
    }

    // 清除已完成待办事项
    @Transactional
    public int clearCompletedTodos(Long userId) {
        return todoMapper.softDeleteCompletedByUserId(userId);
    }
    // 获取待办事项统计
    public TodoStats getTodoStats(Long userId) {
        List<TodoItem> allTodos = todoMapper.findByUserIdAndDeletedFalse(userId);
        List<TodoItem> completedTodos = todoMapper.findByUserIdAndCompleteTrueAndDeletedFalse(userId);

        TodoStats stats = new TodoStats();
        stats.setTotal(allTodos.size());
        stats.setCompleted(completedTodos.size());
        stats.setIncomplete(allTodos.size() - completedTodos.size());
        return stats;
    }

    public List<TodoItem> getTodos(Long userId) {
        return todoMapper.findByUserIdAndDeletedFalse(userId);
    }

    public List<TodoItem> findAll() {
        return todoMapper.findAll();
    }

//    public TodoItem insertTodo(TodoItem todoItem) {
//        return todoMapper.insertTodo(todoItem);
//    }
    public int insertTodo(TodoItem todoItem) {
        int res=todoMapper.insertTodo(todoItem);  // 执行插入
        return res;  // 返回传入对象（此时应已包含生成的ID）
    }

    public TodoItem getTodoById(Long id) {
        return todoMapper.getTodoById(id);
    }

    /**
     * 用户清单的模糊查找
     * 需要有用户ID
     * */
    public PageInfo<TodoItem> selectPage(int pageNum, int pageSize, String keyword, Long userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<TodoItem> list=todoMapper.findTodoByNameOrType(keyword,userId);
        return PageInfo.of(list);
    }
    // 统计信息DTO
    @Data
    public static class TodoStats {
        private int total;
        private int completed;
        private int incomplete;

        // getters and setters

    }
}
