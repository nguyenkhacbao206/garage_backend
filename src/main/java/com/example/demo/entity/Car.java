package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cars")
public class Car {

    @Id
    private String id;

    @Indexed(unique = true) // Đảm bảo biển số không trùng
    private String plate;
    // private String carCode; 
    private String model;
    private String manufacturer;
    private String description;
    private String customerId;

    public Car() {}

    public Car(String id, String plate, String carCode, String model, String manufacturer, String description, String customerId) {
        this.id = id;
        this.plate = plate;
        // this.carCode = carCode;
        this.model = model;
        this.manufacturer = manufacturer;
        this.description = description;
        this.customerId = customerId;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    // public String getCarCode() { return carCode; }
    // public void setCarCode(String carCode) { this.carCode = carCode; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
}
