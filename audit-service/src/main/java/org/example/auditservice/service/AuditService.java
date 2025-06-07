package org.example.auditservice.service;

import org.example.auditservice.entity.AuditLog;
import org.example.auditservice.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;


    public AuditLog logAction(AuditLog auditLog) {

        return auditLog;
    }

    public List<AuditLog> getUserLogs(Long userId) {
        return null;
    }

    public List<AuditLog> getTodoLogs(Long todoId) {
        return null;
    }
    @EventListener
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        // 标记相关审计记录
        auditRepository.markUserDeleted(event.getUserId());
    }

    @EventListener
    public void handleTodoDeletedEvent(TodoDeletedEvent event) {
        // 标记相关审计记录
        auditRepository.markTodoDeleted(event.getTodoId());
    }
}
