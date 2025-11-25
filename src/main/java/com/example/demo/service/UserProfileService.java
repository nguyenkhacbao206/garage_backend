package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private static final String UPLOAD_DIR = "uploads/avatars/";

    public UserProfileService(UserProfileRepository repo) {
        this.userProfileRepository = repo;
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    /** ============================
     *  LẤY PROFILE — TỰ TẠO NẾU CHƯA CÓ
     * ============================ */
    public UserProfileResponse getProfile() {
        String username = getCurrentUsername();

        UserProfile profile = userProfileRepository.findByUsername(username);

        if (profile == null) {
            // TỰ TẠO PROFILE LẦN ĐẦU
            profile = new UserProfile();
            profile.setUsername(username);
            profile.setEmail(username); // tùy bạn map email
            profile.setPhonenumber(null);

            profile.setCreatedAt(LocalDateTime.now());
            profile.setUpdatedAt(LocalDateTime.now());

            userProfileRepository.save(profile);
        }

        return convert(profile);
    }

    
    /** ============================
     *   CẬP NHẬT PROFILE
     * ============================ */
    public UserProfileResponse updateProfile(UpdateProfileRequest req) {
        String username = getCurrentUsername();

        UserProfile profile = userProfileRepository.findByUsername(username);
        if (profile == null)
            throw new RuntimeException("Không tìm thấy user!");

        profile.setUsername(req.getUsername());
        profile.setEmail(req.getEmail());
        profile.setPhonenumber(req.getPhonenumber());

        profile.setBirthday(req.getBirthday());
        profile.setGender(req.getGender());
        profile.setCity(req.getCity());
        profile.setHometown(req.getHometown());
        profile.setAddress(req.getAddress());
        profile.setDescription(req.getDescription());

        profile.setUpdatedAt(LocalDateTime.now());

        return convert(userProfileRepository.save(profile));
    }

    
    /** ============================
     *   ĐỔI MẬT KHẨU
     * ============================ */
    public String changePassword(ChangePasswordRequest req) {
        String username = getCurrentUsername();
        UserProfile profile = userProfileRepository.findByUsername(username);

        if (profile == null)
            throw new RuntimeException("Không tìm thấy user!");

        if (!profile.getPassword().equals(req.getOldPassword()))
            throw new RuntimeException("Mật khẩu cũ không đúng!");

        profile.setPassword(req.getNewPassword());
        profile.setUpdatedAt(LocalDateTime.now());
        userProfileRepository.save(profile);

        return "Đổi mật khẩu thành công!";
    }

    
    /** ============================
     *   UPLOAD AVATAR
     * ============================ */
    public UserProfileResponse uploadAvatar(MultipartFile file) {
        String username = getCurrentUsername();
        UserProfile profile = userProfileRepository.findByUsername(username);

        if (profile == null)
            throw new RuntimeException("Không tìm thấy user!");

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String filename = username + "_" + file.getOriginalFilename();
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

    
    /** ============================
     *   XÓA AVATAR
     * ============================ */
    public String deleteAvatar() {
        String username = getCurrentUsername();
        UserProfile profile = userProfileRepository.findByUsername(username);

        if (profile == null)
            throw new RuntimeException("Không tìm thấy user!");

        profile.setAvatar(null);
        profile.setUpdatedAt(LocalDateTime.now());
        userProfileRepository.save(profile);

        return "Xóa avatar thành công!";
    }

    
    /** ============================
     *  CHUYỂN ENTITY → RESPONSE
     * ============================ */
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

        if (p.getCreatedAt() != null)
            r.setCreatedAt(p.getCreatedAt().toString());
        if (p.getUpdatedAt() != null)
            r.setUpdatedAt(p.getUpdatedAt().toString());

        return r;
    }


    /** ============================
     * LẤY FIELD BẰNG PATHVARIABLE
     * ============================ */
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
}
