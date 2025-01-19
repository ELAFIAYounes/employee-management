package com.employee.service;

import com.employee.model.AuditLog;
import com.employee.model.User;
import com.employee.repository.AuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    public void logAction(String action, String entityType, Long entityId, Object oldValue, Object newValue, String description) {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String oldValueJson = oldValue != null ? objectMapper.writeValueAsString(oldValue) : null;
            String newValueJson = newValue != null ? objectMapper.writeValueAsString(newValue) : null;
            
            String ipAddress = getClientIpAddress();

            AuditLog auditLog = new AuditLog(
                action,
                entityType,
                entityId,
                oldValueJson,
                newValueJson,
                description,
                currentUser,
                ipAddress
            );

            auditRepository.save(auditLog);
            log.info("Audit log created: {}", description);
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage(), e);
        }
    }

    public void logSystemEvent(String action, String description) {
        try {
            User systemUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String ipAddress = getClientIpAddress();

            AuditLog auditLog = new AuditLog(
                action,
                "SYSTEM",
                0L,
                null,
                null,
                description,
                systemUser,
                ipAddress
            );

            auditRepository.save(auditLog);
            log.info("System event logged: {}", description);
        } catch (Exception e) {
            log.error("Failed to log system event: {}", e.getMessage(), e);
        }
    }

    private String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.warn("Could not determine client IP address", e);
        }
        return "unknown";
    }
}
