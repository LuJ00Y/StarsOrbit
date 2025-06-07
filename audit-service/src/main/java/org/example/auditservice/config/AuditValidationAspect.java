package org.example.auditservice.config;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

// 审计服务中监控无效引用
@Aspect
public class AuditValidationAspect {

    @AfterThrowing(pointcut="execution(* logAction(..))", throwing="ex")
    public void handleInvalidReference(InvalidReferenceException ex) {
        metrics.increment("audit.invalid_references");
        alertService.notify("Invalid reference detected: " + ex.getMessage());
    }
}