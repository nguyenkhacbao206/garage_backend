package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServiceRequest {

    @Schema(description = "Tên dịch vụ", example = "Khám tổng quát")
    private String name;

    @Schema(description = "Mã dịch vụ", example = "DV01")
    private String serviceCode;

    @Schema(description = "Mô tả dịch vụ", example = "Khám tổng quát bao gồm các xét nghiệm cơ bản")
    private String description;

    @Schema(description = "Giá dịch vụ", example = "250000")
    private BigDecimal price;

    @Schema(description = "Ngày tạo", example = "2025-11-22T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Ngày cập nhật", example = "2025-11-22T11:20:10")
    private LocalDateTime updatedAt;

    public ServiceRequest() {}

    public ServiceRequest(String name, String serviceCode, String description, BigDecimal price) {
        this.name = name;
        this.serviceCode = serviceCode;
        this.description = description;
        this.price = price;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
