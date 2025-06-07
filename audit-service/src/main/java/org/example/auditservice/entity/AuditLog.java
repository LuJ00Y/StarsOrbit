package org.example.auditservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action;
    private Long userId;
    private Long todoId;
    private LocalDateTime timestamp;
    private String details;

    private String userSnapshot; // JSON: {"id":101, "name":"John"}
    private String todoSnapshot; // JSON: {"id":201, "title":"Task"}

    // Getters and setters
    public String getTodoSnapshot() {
        return todoSnapshot;
    }

    public void setTodoSnapshot(String todoSnapshot) {
        this.todoSnapshot = todoSnapshot;
    }

    public String getUserSnapshot() {
        return userSnapshot;
    }

    public void setUserSnapshot(String userSnapshot) {
        this.userSnapshot = userSnapshot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}