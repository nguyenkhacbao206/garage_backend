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
@CrossOrigin
@Tag(name = "User Profile", description = "API quản lý thông tin hồ sơ người dùng")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "Lấy toàn bộ thông tin hồ sơ",
        description = "Trả về toàn bộ thông tin profile của user đang đăng nhập.\nNếu chưa có thì tự động tạo mới."
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
        Các field hợp lệ:

        - id
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

        Gọi API: /api/profile/{field}
        """
    )
    public ResponseEntity<?> getField(
            @Parameter(description = "Tên trường cần lấy")
            @PathVariable String field) {

        try {
            return ResponseEntity.ok(service.getField(field));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping
    @Operation(
        summary = "Cập nhật toàn bộ hồ sơ người dùng",
        description = "Cập nhật toàn bộ profile dựa vào JSON truyền vào."
    )
    public ResponseEntity<?> updateProfile(
            @Parameter(description = "Dữ liệu cập nhật")
            @RequestBody UpdateProfileRequest req) {

        try {
            return ResponseEntity.ok(service.updateProfile(req));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/password")
    @Operation(
        summary = "Đổi mật khẩu người dùng",
        description = "Truyền oldPassword và newPassword để đổi mật khẩu."
    )
    public ResponseEntity<?> changePassword(
            @Parameter(description = "Mật khẩu cũ và mật khẩu mới")
            @RequestBody ChangePasswordRequest req) {

        try {
            return ResponseEntity.ok(service.changePassword(req));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/avatar")
    @Operation(
        summary = "Upload avatar mới",
        description = "Upload file ảnh và lưu đường dẫn avatar."
    )
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
    @Operation(
        summary = "Xóa avatar hiện tại",
        description = "Xóa ảnh avatar của user khỏi hệ thống."
    )
    public ResponseEntity<?> deleteAvatar() {
        try {
            return ResponseEntity.ok(service.deleteAvatar());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
