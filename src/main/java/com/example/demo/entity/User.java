package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    
    @Indexed(unique = true)
    private String email;
    private String phonenumber;
    private String password;
    // private String role;

    //trường sign in cho gg
    private String googleId;
    private String pictureUrl;
    private String authProvider;

    public User() {}

    public User(String username, String email, String phonenumber, String password) {
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        // this.role = role;
        this.authProvider = "local"; // Xác định là người dùng cục bộ
    }

    // Đăng ký đăng nhập cho phần Google
    public User(String email, String username, String googleId, String pictureUrl, boolean isGoogle) {
        this.email = email;
        this.username = username;
        this.googleId = googleId;
        this.pictureUrl = pictureUrl;
        this.authProvider = "google";
        this.password = null;        
        this.phonenumber = null;    
    }

    public String getId() { 
        return id; 
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
    public String getPassword() { 
        return password; 
    }
    // public String getRole() { 
    //     return role; 
    // }

    public void setId(String id) { 
        this.id = id; 
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
    public void setPassword(String password) { 
        this.password = password; 
    }
    // public void setRole(String role) {
    //      this.role = role; 
    //     }

    //Getters và Setters của gg
    public String getGoogleId() { return googleId; }
    public void setGoogleId(String googleId) { this.googleId = googleId; }
    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }
    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
}
