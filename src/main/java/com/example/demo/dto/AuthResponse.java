package com.example.demo.dto;

public class AuthResponse {
    private String username;
    private String email;
    private String phonenumber;
    private String accessToken;
    private String refreshToken;

    public AuthResponse() {}
    public AuthResponse( String username, String email, String phonenumber, String accessToken, String refreshToken) {
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPhonenumber() {
        return phonenumber;
    }
    public String getAccessToken() { 
        return accessToken; 
    }
    public String getRefreshToken() { 
        return refreshToken; 
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public void setAccessToken(String accessToken) { 
        this.accessToken = accessToken; 
    }
    public void setRefreshToken(String refreshToken) { 
        this.refreshToken = refreshToken; 
    }

    
}
