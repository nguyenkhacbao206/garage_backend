package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private static final String UPLOAD_DIR = "uploads/avatars/";

    public UserProfileService(UserProfileRepository repo) {
        this.userProfileRepository = repo;
    }

    // Get current user profile
    public UserProfileResponse getProfile(String userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId);

        if (profile == null)
            throw new RuntimeException("Không tìm thấy thông tin người dùng!");

        return convert(profile);
    }

    // Update profile info
    public UserProfileResponse updateProfile(String userId, UpdateProfileRequest req) {
        UserProfile profile = userProfileRepository.findByUserId(userId);

        if (profile == null)
            throw new RuntimeException("Không tìm thấy user!");

        profile.setUsername(req.getUsername());
        profile.setEmail(req.getEmail());
        profile.setPhonenumber(req.getPhonenumber());

        return convert(userProfileRepository.save(profile));
    }

    // Change password
    public String changePassword(String userId, ChangePasswordRequest req) {
        UserProfile profile = userProfileRepository.findByUserId(userId);

        if (profile == null)
            throw new RuntimeException("Không tìm thấy user!");

        if (!profile.getPassword().equals(req.getOldPassword()))
            throw new RuntimeException("Mật khẩu cũ không đúng!");

        profile.setPassword(req.getNewPassword());
        userProfileRepository.save(profile);

        return "Đổi mật khẩu thành công!";
    }

    // Upload avatar
    public UserProfileResponse uploadAvatar(String userId, MultipartFile file) {
        UserProfile profile = userProfileRepository.findByUserId(userId);

        if (profile == null)
            throw new RuntimeException("Không tìm thấy user!");

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String filename = userId + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + filename);

            Files.write(path, file.getBytes());

            profile.setAvatar("/avatars/" + filename);
            userProfileRepository.save(profile);

            return convert(profile);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload avatar!");
        }
    }

    // Delete avatar
    public String deleteAvatar(String userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId);

        if (profile == null)
            throw new RuntimeException("Không tìm thấy user!");

        profile.setAvatar(null);
        userProfileRepository.save(profile);

        return "Xóa avatar thành công!";
    }

    private UserProfileResponse convert(UserProfile p) {
        UserProfileResponse r = new UserProfileResponse();
        r.setId(p.getId());
        r.setUserId(p.getUserId());
        r.setUsername(p.getUsername());
        r.setEmail(p.getEmail());
        r.setPhonenumber(p.getPhonenumber());
        r.setAvatar(p.getAvatar());
        return r;
    }
}
