package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserProfileResponse {

    @Schema(description = "ID của user", example = "123456")
    private String id;

    @Schema(description = "Mã user", example = "user001")
    private String userId;

    @Schema(description = "Tên người dùng", example = "Nguyen Van A")
    private String username;

    @Schema(description = "Email", example = "user@example.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0909123456")
    private String phonenumber;

    @Schema(description = "Tên file avatar", example = "avatar.png")
    private String avatar;

    public String getId() { 
        return id;
     }
    public String getUserId() { 
        return userId;
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
    public String getAvatar() { 
        return avatar;
     }

    public void setId(String id) { 
        this.id = id;
     }
    public void setUserId(String userId) { 
        this.userId = userId;
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
    public void setAvatar(String avatar) { 
        this.avatar = avatar;
     }
}
