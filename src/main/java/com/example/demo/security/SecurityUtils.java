package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    // Lấy userId (hoặc email) từ JWT
    public static String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername(); // username = email = userId
        }

        if (principal instanceof String s) {
            return s; 
        }

        return null;
    }
}
