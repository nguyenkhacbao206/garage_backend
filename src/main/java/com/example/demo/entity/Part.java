package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parts")
public class Part {

    @Id
    private String id;
    private String partCode;

    private String name;
    // private String unit;
    // private Double price;
    private Double salePrice;
    private Integer stock;
    private String description;
    private String supplierId;
    private Supplier supplier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    

    public Part() {}

    public Part(String partCode, String name, Double salePrice, Integer stock, String description, String supplierId, Supplier supplier) {
        this.partCode = partCode;
        this.name = name;
        // this.unit = unit;
        this.salePrice = salePrice;
        // this.price = price;
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

    // public Double getPrice() {
    //     return price;
    // }

    // public void setPrice(Double price) {
    //     this.price = price;
    // }

    public Double getSalePrice() { return salePrice; }
    public void setSalePrice(Double salePrice) { this.salePrice =salePrice ;}

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
