package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dữ liệu Token ID từ Google")
public class TokenDto {
    
    @Schema(description = "Token ID (JWT) từ Google", example = "eyJhbGciOiJIUzI1...")
    private String token; 

    public TokenDto() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}