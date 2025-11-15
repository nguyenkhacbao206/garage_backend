package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class TechnicianRequest {

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @Size(min = 7, max = 20, message = "SĐT có độ dài không hợp lệ")
    private String phone;

    @PositiveOrZero(message = "Lương cơ bản phải >= 0")
    private Double baseSalary;

    private String position;
    private String userId;

    public TechnicianRequest() {}

    // Getter/Setter
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
}
