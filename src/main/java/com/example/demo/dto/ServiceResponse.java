package com.example.demo.dto;

import java.math.BigDecimal;

public class ServiceResponse {
    private String id;
    private String serviceCode;
    private String name;
    private String description;
    private BigDecimal price;

    // Getter & Setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getServiceCode() { 
        return serviceCode; 
    }
    public void setServiceCode(String serviceCode) { 
        this.serviceCode = serviceCode; 
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

