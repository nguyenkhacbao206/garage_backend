package com.example.demo.dto;

import java.math.BigDecimal;

public class ServiceRequest {
    private String name;
    private String description;
    private String serviceCode;
    private BigDecimal price;

    // Getter & Setter
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}
}
