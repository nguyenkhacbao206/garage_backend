package com.example.demo.controller;

import com.example.demo.service.AuthService;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.entity.User;
import com.example.demo.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
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
    // REGISTER
    @Operation(summary = "Đăng ký tài khoản mới",
               description = "Tạo tài khoản người dùng với username, email, phonenumber và password.")
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        user.setPassword(null); // Ẩn password khi trả về
        return ResponseEntity.ok(user);
    }

    // LOGIN

    @Operation(summary = "Đăng nhập và nhận JWT token",
               description = "Xác thực người dùng và trả về accessToken + refreshToken.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // REFRESH TOKEN
    
    @Operation(summary = "Làm mới Access Token",
               description = "Nhận accessToken mới bằng refreshToken hợp lệ.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(401)
                    .body("Refresh token không hợp lệ hoặc đã hết hạn!");
        }

        String username = jwtService.extractUsername(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(username);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }
}
