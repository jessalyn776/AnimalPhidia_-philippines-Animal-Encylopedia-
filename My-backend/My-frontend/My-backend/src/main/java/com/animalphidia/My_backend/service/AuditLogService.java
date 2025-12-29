package com.animalphidia.My_backend.service;

import com.animalphidia.My_backend.model.AuditLog;
import com.animalphidia.My_backend.model.User;
import com.animalphidia.My_backend.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // Log user registration
    public void logRegistration(User user, HttpServletRequest request, boolean success) {
        AuditLog log = new AuditLog();
        log.setUserId(user != null ? user.getId().longValue() : null);
        log.setAction(success ? "USER_REGISTER_SUCCESS" : "USER_REGISTER_FAILED");
        log.setEntityType("USER");
        log.setEntityId(user != null ? user.getId().longValue() : null);
        log.setDetails("User registration: " + (user != null ? user.getEmail() : "Unknown"));

        if (request != null) {
            log.setIpAddress(getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }

        auditLogRepository.save(log);
    }

    // Log user login
    public void logLogin(User user, HttpServletRequest request, boolean success) {
        AuditLog log = new AuditLog();
        log.setUserId(user != null ? user.getId().longValue() : null);
        log.setAction(success ? "USER_LOGIN_SUCCESS" : "USER_LOGIN_FAILED");
        log.setEntityType("USER");
        log.setEntityId(user != null ? user.getId().longValue() : null);
        log.setDetails("Login attempt: " + (user != null ? user.getEmail() : "Unknown"));

        if (request != null) {
            log.setIpAddress(getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }

        auditLogRepository.save(log);
    }

    // Log email verification
    public void logEmailVerification(User user, String token, boolean success) {
        AuditLog log = new AuditLog();
        log.setUserId(user.getId().longValue());
        log.setAction(success ? "EMAIL_VERIFIED_SUCCESS" : "EMAIL_VERIFIED_FAILED");
        log.setEntityType("USER");
        log.setEntityId(user.getId().longValue());
        log.setDetails("Email verification for: " + user.getEmail() +
                " | Token: " + (token != null ? token.substring(0, 8) + "..." : "N/A"));

        auditLogRepository.save(log);
    }

    // Log general user action
    public void logUserAction(Long userId, String action, String entityType,
                              Long entityId, String details, HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);

        if (request != null) {
            log.setIpAddress(getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }

        auditLogRepository.save(log);
    }

    // Get client IP address
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}