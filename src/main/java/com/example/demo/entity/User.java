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
    private String role;

    public User() {}

    public User(String username, String email, String phonenumber, String password, String role) {
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.role = role;
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
    public String getRole() { 
        return role; 
    }

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
    public void setRole(String role) {
         this.role = role; 
    }
}
