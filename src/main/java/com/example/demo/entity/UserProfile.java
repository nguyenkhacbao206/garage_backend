package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "user_profiles")
public class UserProfile {

    @Id
    private String id;

    private String username;
    
    @Indexed(unique = true)
    private String email;
    private String phonenumber;

    private String birthday;
    private String gender;
    private String city;
    private String hometown;
    private String address;

    private String avatar;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String password;

    public UserProfile() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // getter, setter

    public String getId() { 
        return id;
     }
    public void setId(String id) { 
        this.id = id;
     }

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

    public String getBirthday() { 
        return birthday;
     }
    public void setBirthday(String birthday) { 
        this.birthday = birthday;
     }

    public String getGender() { 
        return gender;
     }
    public void setGender(String gender) { 
        this.gender = gender;
     }

    public String getCity() { 
        return city;
     }
    public void setCity(String city) { 
        this.city = city;
     }

    public String getHometown() { 
        return hometown;
     }
    public void setHometown(String hometown) { 
        this.hometown = hometown;
     }

    public String getAddress() { 
        return address;
     }
    public void setAddress(String address) { 
        this.address = address;
     }

    public String getAvatar() { 
        return avatar;
     }
    public void setAvatar(String avatar) { 
        this.avatar = avatar;
     }

    public String getDescription() { 
        return description;
     }
    public void setDescription(String description) { 
        this.description = description;
     }

    public LocalDateTime getCreatedAt() { 
        return createdAt;
     }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt;
     }

    public LocalDateTime getUpdatedAt() { 
        return updatedAt;
     }
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt;
     }

    public String getPassword() { 
        return password;
     }
    public void setPassword(String password) { 
        this.password = password;
     }
}
