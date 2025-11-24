package com.example.demo.dto;

public class UpdateProfileRequest {
    private String username;
    private String email;
    private String phonenumber;

    public String getUsername() { 
        return username; 
    }
    public String getEmail() { 
        return email; 
    }
    public String getPhonenumber() { 
        return phonenumber; 
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
}
