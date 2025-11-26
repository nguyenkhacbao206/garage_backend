package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.entity.UserProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.UserProfileRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "uploads/avatars/";

    public UserProfileService(UserProfileRepository userProfileRepository,
                              UserRepository userRepository,
                              AuthService authService,
                              PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    // Lấy email từ token
    private String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); // username = email
    }

    // Lấy profile đúng user từ bảng user_profiles
    private UserProfile getOrCreateProfile() {
        String email = getCurrentEmail();

        // ✔ FIX: tìm đúng profile theo email
        UserProfile profile = userProfileRepository.findByEmail(email).orElse(null);
        if (profile != null) return profile;

        // Tạo mới profile từ bảng User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        UserProfile newProfile = new UserProfile();
        newProfile.setUsername(user.getUsername());
        newProfile.setEmail(user.getEmail());
        newProfile.setPhonenumber(user.getPhonenumber());
        newProfile.setPassword(user.getPassword());
        newProfile.setCreatedAt(LocalDateTime.now());
        newProfile.setUpdatedAt(LocalDateTime.now());

        return userProfileRepository.save(newProfile);
    }

    // Lấy toàn bộ profile
    public UserProfileResponse getProfile() {
        return convert(getOrCreateProfile());
    }

    // Cập nhật profile
    public UserProfileResponse updateProfile(UpdateProfileRequest req) {
        UserProfile profile = getOrCreateProfile();

        profile.setUsername(req.getUsername());
        profile.setPhonenumber(req.getPhonenumber());
        profile.setBirthday(req.getBirthday());
        profile.setGender(req.getGender());
        profile.setCity(req.getCity());
        profile.setHometown(req.getHometown());
        profile.setAddress(req.getAddress());
        profile.setDescription(req.getDescription());
        profile.setUpdatedAt(LocalDateTime.now());


        userProfileRepository.save(profile);
        return convert(profile);
    }

    // Đổi mật khẩu
    public AuthResponse changePassword(ChangePasswordRequest req) {
        UserProfile profile = getOrCreateProfile();

        if (!passwordEncoder.matches(req.getOldPassword(), profile.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }

        // Update bảng UserProfile
        String encodedPass = passwordEncoder.encode(req.getNewPassword());
        profile.setPassword(encodedPass);
        profile.setUpdatedAt(LocalDateTime.now());
        userProfileRepository.save(profile);

        // Update bảng User
        User user = userRepository.findByEmail(profile.getEmail())
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        user.setPassword(encodedPass);
        userRepository.save(user);

        // Trả token mới
        return authService.generateNewToken(user);
    }

    // Upload avatar
    public UserProfileResponse uploadAvatar(MultipartFile file) {
        UserProfile profile = getOrCreateProfile();

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String filename = profile.getUsername() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + filename);

            Files.write(path, file.getBytes());

            profile.setAvatar("/avatars/" + filename);
            profile.setUpdatedAt(LocalDateTime.now());
            userProfileRepository.save(profile);

            return convert(profile);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload avatar!");
        }
    }

    // Xoá avatar
    public String deleteAvatar() {
        UserProfile profile = getOrCreateProfile();

        profile.setAvatar(null);
        profile.setUpdatedAt(LocalDateTime.now());
        userProfileRepository.save(profile);

        return "Xóa avatar thành công!";
    }

    // Lấy thông tin 1 trường
    public Object getField(String field) {
        UserProfileResponse p = getProfile();

        return switch (field.toLowerCase()) {
            case "id" -> p.getId();
            case "username" -> p.getUsername();
            case "email" -> p.getEmail();
            case "phonenumber" -> p.getPhonenumber();
            case "gender" -> p.getGender();
            case "birthday" -> p.getBirthday();
            case "city" -> p.getCity();
            case "hometown" -> p.getHometown();
            case "address" -> p.getAddress();
            case "avatar" -> p.getAvatar();
            case "description" -> p.getDescription();
            case "createdat" -> p.getCreatedAt();
            case "updatedat" -> p.getUpdatedAt();
            default -> throw new RuntimeException("Trường '" + field + "' không tồn tại!");
        };
    }

    // Convert sang DTO
    private UserProfileResponse convert(UserProfile p) {
        UserProfileResponse r = new UserProfileResponse();

        r.setId(p.getId());
        r.setUsername(p.getUsername());
        r.setEmail(p.getEmail());
        r.setPhonenumber(p.getPhonenumber());
        r.setBirthday(p.getBirthday());
        r.setGender(p.getGender());
        r.setCity(p.getCity());
        r.setHometown(p.getHometown());
        r.setAddress(p.getAddress());
        r.setAvatar(p.getAvatar());
        r.setDescription(p.getDescription());

        if (p.getCreatedAt() != null) r.setCreatedAt(p.getCreatedAt().toString());
        if (p.getUpdatedAt() != null) r.setUpdatedAt(p.getUpdatedAt().toString());

        return r;
    }
}

