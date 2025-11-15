package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dữ liệu phản hồi sau khi đăng nhập / refresh token")
public class AuthResponse {

    @Schema(description = "Tên người dùng", example = "baokhac")
    private String username;

    @Schema(description = "Email của người dùng", example = "baokhac@example.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0909123456")
    private String phonenumber;

    @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;

    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1...")
    private String refreshToken;

    public AuthResponse() {}

    public AuthResponse(String username, String email, String phonenumber, String accessToken, String refreshToken) {
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // getters & setters
    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; 
    }
    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public String getPhonenumber() { 
        return phonenumber; 
    }
    public void setPhonenumber(String phonenumber) { 
        this.phonenumber = phonenumber; 
    }
    public String getAccessToken() { 
        return accessToken; 
    }
    public void setAccessToken(String accessToken) { 
        this.accessToken = accessToken; 
    }
    public String getRefreshToken() { 
        return refreshToken; 
    }
    public void setRefreshToken(String refreshToken) { 
        this.refreshToken = refreshToken; 
    }
}
