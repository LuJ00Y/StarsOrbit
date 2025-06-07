package org.example.auditservice.controller;

import org.example.auditservice.entity.AuditLog;
import org.example.auditservice.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class AuditController {
    @Autowired
    private AuditService auditService;

    @PostMapping("/")
    public AuditLog logAction(@RequestBody AuditLog auditLog) {
        return auditService.logAction(auditLog);
    }

    @GetMapping("/user/{userId}")
    public List<AuditLog> getUserLogs(@PathVariable Long userId) {
        return auditService.getUserLogs(userId);
    }

    @GetMapping("/todo/{todoId}")
    public List<AuditLog> getTodoLogs(@PathVariable Long todoId) {
        return auditService.getTodoLogs(todoId);
    }
}
