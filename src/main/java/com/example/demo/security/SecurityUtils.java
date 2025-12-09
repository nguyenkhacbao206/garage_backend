package com.example.demo.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class SecurityUtils {

    @SuppressWarnings("unchecked")
    public static String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) principal;
            Object id = map.get("userId");
            if (id != null) return id.toString();
        }

        return null;
    }
}
