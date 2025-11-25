package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.service.UserProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "User Profile", description = "API quản lý thông tin hồ sơ người dùng")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }


    @GetMapping
    @Operation(
        summary = "Lấy toàn bộ thông tin hồ sơ",
        description = "Trả về toàn bộ thông tin profile của user đang đăng nhập. Nếu chưa có profile thì tự tạo profile mới."
    )
    public ResponseEntity<?> getProfile() {
        try {
            return ResponseEntity.ok(service.getProfile());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @GetMapping("/{field}")
    @Operation(
        summary = "Lấy thông tin theo từng trường",
        description = """
            Ví dụ các trường hợp hợp lệ:
            - username
            - email
            - phonenumber
            - gender
            - birthday
            - city
            - hometown
            - address
            - avatar
            - description
            - createdAt
            - updatedAt
            """
    )
    public ResponseEntity<?> getField(
            @Parameter(description = "Tên trường muốn lấy. Ví dụ: username, email, city, avatar.")
            @PathVariable String field) {

        try {
            Object result = service.getField(field);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @PutMapping
    @Operation(summary = "Cập nhật toàn bộ hồ sơ người dùng")
    public ResponseEntity<?> updateProfile(
            @Parameter(description = "Dữ liệu cập nhật thông tin người dùng")
            @RequestBody UpdateProfileRequest req) {

        try {
            return ResponseEntity.ok(service.updateProfile(req));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @PutMapping("/password")
    @Operation(summary = "Đổi mật khẩu user đang đăng nhập")
    public ResponseEntity<?> changePassword(
            @Parameter(description = "Gồm mật khẩu cũ và mật khẩu mới")
            @RequestBody ChangePasswordRequest req) {

        try {
            return ResponseEntity.ok(service.changePassword(req));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/avatar")
    @Operation(summary = "Upload avatar mới cho user")
    public ResponseEntity<?> uploadAvatar(
            @Parameter(description = "File ảnh avatar")
            @RequestParam("file") MultipartFile file) {

        try {
            return ResponseEntity.ok(service.uploadAvatar(file));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @DeleteMapping("/avatar")
    @Operation(summary = "Xóa avatar hiện tại của user")
    public ResponseEntity<?> deleteAvatar() {
        try {
            return ResponseEntity.ok(service.deleteAvatar());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
