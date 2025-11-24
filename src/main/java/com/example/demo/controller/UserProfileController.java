package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.service.UserProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
@Tag(name = "User Profile", description = "API quản lý thông tin người dùng")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    // Lấy thông tin người dùng
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(@PathVariable String userId) {
        try {
            UserProfileResponse user = service.getProfile(userId);
            return ResponseEntity.ok(new ApiResponse<>("Success", user));
        } catch (Exception ex) {
            throw ex; 
        }
    }

    // Cập nhật thông tin
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @PathVariable String userId,
            @RequestBody UpdateProfileRequest req
    ) {
        try {
            UserProfileResponse updated = service.updateProfile(userId, req);
            return ResponseEntity.ok(new ApiResponse<>("Profile updated successfully", updated));
        } catch (Exception ex) {
            throw ex;
        }
    }

    // Đổi mật khẩu
    @PutMapping("/change-password/{userId}")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @PathVariable String userId,
            @RequestBody ChangePasswordRequest req
    ) {
        try {
            String msg = service.changePassword(userId, req);
            return ResponseEntity.ok(new ApiResponse<>(msg, null));
        } catch (Exception ex) {
            throw ex;
        }
    }

    // Upload avatar
    @PostMapping("/avatar/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> uploadAvatar(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            UserProfileResponse updatedProfile = service.uploadAvatar(userId, file);
            return ResponseEntity.ok(new ApiResponse<>("Avatar uploaded successfully", updatedProfile));
        } catch (Exception ex) {
            throw ex;
        }
    }

    // Xóa avatar
    @DeleteMapping("/avatar/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteAvatar(@PathVariable String userId) {
        try {
            String msg = service.deleteAvatar(userId);
            return ResponseEntity.ok(new ApiResponse<>(msg, null));
        } catch (Exception ex) {
            throw ex;
        }
    }
}
