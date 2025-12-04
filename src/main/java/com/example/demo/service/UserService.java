package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GoogleIdTokenVerifier verifier;
    private final JavaMailSender mailSender; // TIÊM THÊM DEPENDENCY

    // Sử dụng @Value để lấy Client ID từ application.properties
    @Value("${spring.mail.username}")
    private String systemEmail; // Lấy email hệ thống để đặt làm người gửi

    // CẬP NHẬT CONSTRUCTOR: Tiêm thêm JavaMailSender
    public UserService(@Value("${google.client.id}") String clientId, UserRepository userRepository,JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        
        // Khởi tạo GoogleIdTokenVerifier
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            .setAudience(Collections.singletonList(clientId))
            .build();
    }

    /**
     * 1. Logic processGoogleLogin(): Xác minh Google Token và Quản lý DB (Lưu/Cập nhật User).
     * @param idTokenString Token ID nhận từ Frontend
     * @return Đối tượng User đã được xử lý
     */
    public User processGoogleLogin(String idTokenString) throws GeneralSecurityException, IOException {
        
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String googleId = payload.getSubject();
            String pictureUrl = (String) payload.get("picture");

            Optional<User> existingUser = userRepository.findByEmail(email);

            User user;
            if (existingUser.isPresent()) {
                //Cập nhật thông tin Google mới nhất
                user = existingUser.get();
                user.setUsername(name); 
                user.setPictureUrl(pictureUrl);
                user.setAuthProvider("google"); 
                userRepository.save(user); 
            } else {
                // Tạo User mới (Sử dụng constructor 5 tham số)
                user = new User(email, name, googleId, pictureUrl, true);
                userRepository.save(user); 
            }
            return user;
        }
        return null; // Token không hợp lệ
    }

    //Tạo mã OTP ngẫu nhiên gồm 6 chữ số.
    public String generateOtp() {
        Random random = new Random();
        // Tạo số từ 100000 đến 999999
        int otp = 100000 + random.nextInt(900000); 
        return String.valueOf(otp);
    }
    
    /**
    @param toEmail
    @param otpCode
     */
    public void sendOtpEmail(String toEmail, String otpCode) { 
        SimpleMailMessage message = new SimpleMailMessage();
        
        // Sử dụng email hệ thống được lấy từ application.properties
        message.setFrom(systemEmail); 
        
        message.setTo(toEmail);
        message.setSubject("[Xác thực] Mã OTP để hoàn tất đăng nhập");
        message.setText("Chào bạn,\n\nMã xác thực OTP của bạn là: " + otpCode + 
                        "\nVui lòng sử dụng mã này để hoàn tất đăng nhập.\n\nTrân trọng!");

        mailSender.send(message);
    }
}