package com.example.demo.dto;

import java.time.LocalDateTime;

public class TechnicianResponse {

    private String id;
    private String techCode; 
    private String name;
    private String phone;
    private Double baseSalary;
    private String position;
    private String userId;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TechnicianResponse() {}

    public TechnicianResponse(String id, String techCode, String name, String phone,
                              Double baseSalary, String position, String userId, 
                              Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.techCode = techCode;
        this.name = name;
        this.phone = phone;
        this.baseSalary = baseSalary;
        this.position = position;
        this.userId = userId;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter/Setter
    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

    public String getTechCode() { 
        return techCode; 
    }
    public void setTechCode(String techCode) { 
        this.techCode = techCode; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    public Double getBaseSalary() { 
        return baseSalary; 
    }
    public void setBaseSalary(Double baseSalary) { 
        this.baseSalary = baseSalary; 
    }

    public String getPosition() { 
        return position; 
    }
    public void setPosition(String position) { 
        this.position = position; 
    }

    public String getUserId() { 
        return userId; 
    }
    public void setUserId(String userId) { 
        this.userId = userId; 
    }

    public Boolean getActive() { 
        return active; 
    }
    public void setActive(Boolean active) { 
        this.active = active; 
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
 
}
