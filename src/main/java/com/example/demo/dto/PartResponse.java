package com.example.demo.dto;
import java.time.LocalDateTime;

import com.example.demo.entity.Supplier;

public class PartResponse {

    private String id;
    private String partCode;
    private String name;
    // private String unit;
    private Double price;
    private Integer stock;
    private String description;
    private String supplierId;
    private Supplier supplier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PartResponse() {}

    public PartResponse(String id, String partCode, String name, Double price, Integer stock, String description, String supplierId, Supplier supplier) {
        this.id = id;
        this.partCode = partCode;
        this.name = name;
        // this.unit = unit;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.supplierId = supplierId;
        this.supplier = supplier;
        
    }

    // Getter - Setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public String getUnit() {
    //     return unit;
    // }

    // public void setUnit(String unit) {
    //     this.unit = unit;
    // }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    public Supplier getSupplier() {
        return supplier;
    }
    
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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
