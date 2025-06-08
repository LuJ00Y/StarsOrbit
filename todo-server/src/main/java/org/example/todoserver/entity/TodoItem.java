package org.example.todoserver.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;



@Data
public class TodoItem {

    @Id
    private Long id;
    private String category;
    private String name;
    private boolean complete;
    private boolean deleted = false;
    private Long userId;

    public TodoItem() {

    }


//    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // 状态更新方法
    public void toggleComplete() {
        this.complete = !this.complete;
        this.updatedAt = LocalDateTime.now();
    }

    // 软删除方法
    public void softDelete() {
        this.deleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
        return;
    }
    public boolean isDeleted() { return  false;}

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
        return;
    }


    public TodoItem(String category, String name) {
        this.category = category;
        this.name = name;
        this.complete = false;
    }

    @Override
    public String toString() {
        return String.format(
                "TodoItem[id=%d, category='%s', name='%s', complete='%b']",
                id, category, name, complete);
    }





}