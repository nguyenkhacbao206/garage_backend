package com.example.demo.controller;

import com.example.demo.service.AuthService;
import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Quản lý đăng ký và đăng nhập người dùng")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản user")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register-admin")
    @Operation(summary = "Tạo tài khoản admin")
    public ResponseEntity<User> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        User user = authService.registerAdmin(request);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/login-admin")
    @Operation(summary = "Admin login")
    public ResponseEntity<AuthResponse> loginAdmin(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.adminLogin(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Làm mới Access Token")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || !jwtService.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(401)
                    .body(null);
        }

        String email = jwtService.extractUsername(refreshToken);
        User user = authService.getByEmail(email);

        // Tạo token mới từ AuthService
        AuthResponse newTokens = authService.generateNewToken(user);

        return ResponseEntity.ok(newTokens);
    }


    @GetMapping("/me")
    @Operation(summary = "Lấy thông tin tài khoản hiện tại")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = userDetails.getUsername();
        User user = authService.getByEmail(email);

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("phonenumber", user.getPhonenumber());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }
}
