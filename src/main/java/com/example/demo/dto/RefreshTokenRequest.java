package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Yêu cầu refresh token để tạo Access Token mới")
public class RefreshTokenRequest {

    @Schema(description = "Refresh Token hợp lệ", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
