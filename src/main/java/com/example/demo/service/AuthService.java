package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    
    // REGISTER USER
    public User register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPhonenumber(),
                passwordEncoder.encode(request.getPassword()),
                "ROLE_USER"
        );

        return userRepository.save(user);
    }

    
    // REGISTER ADMIN
    public User registerAdmin(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User admin = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPhonenumber(),
                passwordEncoder.encode(request.getPassword()),
                "ROLE_ADMIN"
        );

        return userRepository.save(admin);
    }

    
    // LOGIN USER
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = getByEmail(request.getEmail());

        if (!user.getRole().equals("ROLE_USER") && !user.getRole().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Tài khoản không hợp lệ!");
        }

        return buildTokens(user);
    }

    
    // LOGIN ADMIN
    public AuthResponse adminLogin(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User admin = getByEmail(request.getEmail());

        if (!admin.getRole().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Bạn không có quyền admin");
        }

        return buildTokens(admin);
    }

    
    // LẤY USER TỪ EMAIL
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    
    // TẠO TOKEN MỚI (CHANGEPASSWORD)
    
    public AuthResponse generateNewToken(User user) {
        return buildTokens(user);
    }

    
    // TOKEN BUILDER
    
    private AuthResponse buildTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhonenumber(),
                accessToken,
                refreshToken
        );
    }
}
