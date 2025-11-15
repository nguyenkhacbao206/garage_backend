package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dữ liệu đăng ký tài khoản mới")
public class RegisterRequest {

    @Schema(description = "Tên người dùng", example = "baokhac")
    private String username;

    @Schema(description = "Email của người dùng", example = "baokhac@example.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0909123456")
    private String phonenumber;

    @Schema(description = "Mật khẩu", example = "123456")
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String phonenumber, String password) {
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
    }

    // getters và setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhonenumber() { return phonenumber; }
    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
