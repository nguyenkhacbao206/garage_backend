package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cars")
public class Car {

    @Id
    private String id;
    @Indexed(unique = true)
    private String plate;
    private String model;
    private String manufacturer;
    private String description;
    private String customerId;  
    private String customerCode; 
    private boolean active = false; 

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Car() {}

    public Car(String id, String plate, String model, String manufacturer,
               String description, String customerId, String customerCode, boolean active) {
        this.id = id;
        this.plate = plate;
        this.model = model;
        this.manufacturer = manufacturer;
        this.description = description;
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.active = active;
    }

    // Getter & Setter
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
