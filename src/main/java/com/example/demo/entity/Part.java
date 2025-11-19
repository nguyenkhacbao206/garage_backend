package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parts")
public class Part {

    @Id
    private String id;

    private String name;
    private String unit;
    private Double price;
    private Integer stock;
    private String description;
    private String supplierId;

    public Part() {}

    public Part(String name, String unit, Double price, Integer stock, String description, String supplierId) {
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
