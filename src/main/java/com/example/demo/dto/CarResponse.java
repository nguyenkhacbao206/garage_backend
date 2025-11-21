package com.example.demo.dto;

import java.time.LocalDateTime;

public class CarResponse {
    private String id;
    private String plate;
    private String model;
    private String manufacturer;
    private String description;
    private String customerId;
    private String customerCode; 
    private boolean active; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CarResponse() {}

    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

    public String getPlate() { 
        return plate; 
    }
    public void setPlate(String plate) { 
        this.plate = plate; 
    }

    public String getModel() { 
        return model; 
    }
    public void setModel(String model) { 
        this.model = model; 
    }

    public String getManufacturer() { 
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) { 
        this.manufacturer = manufacturer; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }

    public String getCustomerId() { 
        return customerId; 
    }
    public void setCustomerId(String customerId) { 
        this.customerId = customerId;
    }

    public String getCustomerCode() { 
        return customerCode; 
    }
    public void setCustomerCode(String customerCode) { 
        this.customerCode = customerCode; 
    }

    public boolean isActive() { 
        return active; 
    }
    public void setActive(boolean active) { 
        this.active = active; 
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
