package com.example.demo.dto;

public class PartResponse {

    private String id;
    private String name;
    private String unit;
    private Double price;
    private Integer stock;
    private String description;
    private String supplierId;

    public PartResponse() {}

    public PartResponse(String id, String name, String unit, Double price, Integer stock, String description, String supplierId) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.supplierId = supplierId;
    }

    // Getter - Setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

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
}
