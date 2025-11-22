package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class SupplierRequest {

    @Schema(description = "Tên nhà cung cấp", example = "Công ty TNHH 1 thành viên Minh Đồng nát")
    private String name;

    @Schema(description = "Mã nhà cung cấp", example = "NCC10")
    private String supplierCode;

    @Schema(description = "Địa chỉ nhà cung cấp", example = "Thái Bình")
    private String address;

    @Schema(description = "Email nhà cung cấp", example = "contact@abc.com")
    private String email;

    @Schema(description = "Số điện thoại nhà cung cấp", example = "0909123456")
    private String phone;

    @Schema(description = "Mô tả thêm", example = "Chuyên cung cấp thiết bị điện")
    private String description;

    @Schema(description = "Ngày tạo", example = "2025-11-22T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Ngày cập nhật", example = "2025-11-22T11:20:10")
    private LocalDateTime updatedAt;

    public SupplierRequest() {}

    public SupplierRequest(String name, String supplierCode, String address, String email, String phone, String description) {
        this.name = name;
        this.supplierCode = supplierCode;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.description = description;
    }

    //Getters & Setters
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getSupplierCode() { 
        return supplierCode; 
    }
    public void setSupplierCode(String supplierCode) { 
        this.supplierCode = supplierCode; 
    }

    public String getAddress() { 
        return address; 
    }
    public void setAddress(String address) { 
        this.address = address; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
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
}
