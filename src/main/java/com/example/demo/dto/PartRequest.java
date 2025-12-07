package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PartRequest {

    @NotBlank(message = "Tên phụ tùng không được để trống")
    private String name;

    // @NotBlank(message = "Đơn vị tính không được để trống")
    // private String unit;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải >= 0")
    private Double price;
    
    private Double salePrice;


    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Tồn kho phải >= 0")
    private Integer stock;

    private String description;

    @NotBlank(message = "Supplier ID không được để trống")
    private String supplierId;

    public PartRequest() {}

    // Getter - Setter

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

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
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
}
