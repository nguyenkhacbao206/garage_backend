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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.TokenDto;
import com.example.demo.dto.OtpRequest;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Quản lý đăng ký và đăng nhập người dùng")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    private final UserService userService;

    public AuthController(AuthService authService, JwtService jwtService, UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;

        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản mới")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        user.setPassword(null); // ẩn password
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập và nhận JWT token")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Làm mới Access Token")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        // Chỉ chấp nhận refresh token
        if (refreshToken == null || !jwtService.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(401)
                    .body("Refresh token không hợp lệ hoặc đã hết hạn!");
        }

        String email = jwtService.extractUsername(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(email);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Lấy thông tin user đang đăng nhập")
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

        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-login")
    @Operation(summary = "B1: Đăng nhập Google, gửi OTP")
    public ResponseEntity<String> step1LoginAndSendOtp(@RequestBody TokenDto token, HttpSession session) {
        // Gọi logic từ UserService (Google Verify, DB, Gửi Email)
        // ... (Logic tương tự như trong GoogleAuthController đã viết) ...
        try {
            User user = userService.processGoogleLogin(token.getToken());
            String otp = userService.generateOtp();
            userService.sendOtpEmail(user.getEmail(), otp);
            session.setAttribute("tempOtp", otp);
            session.setAttribute("tempUserEmail", user.getEmail());
            
            return ResponseEntity.ok("Mã OTP đã được gửi đến email " + user.getEmail());
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Xác minh hoặc gửi OTP thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "B2: Xác thực OTP và nhận JWT")
    public ResponseEntity<?> step2VerifyOtp(@RequestBody OtpRequest otpRequest, HttpSession session) {
        // Logic xác minh OTP, gọi AuthService để tạo JWT
        // ... (Logic tương tự như trong GoogleAuthController đã viết) ...
        String sessionOtp = (String) session.getAttribute("tempOtp");
        String userEmail = (String) session.getAttribute("tempUserEmail");

        if (sessionOtp == null || !sessionOtp.equals(otpRequest.getOtp())) {
            return ResponseEntity.status(401).body("Mã OTP không đúng hoặc phiên đã hết hạn.");
        }

        try {
            User user = authService.getByEmail(userEmail);
            AuthResponse authResponse = authService.generateNewToken(user);
            
            session.removeAttribute("tempOtp");
            session.removeAttribute("tempUserEmail");

            return ResponseEntity.ok(authResponse);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi tạo token sau xác minh OTP.");
        }
    }

}
