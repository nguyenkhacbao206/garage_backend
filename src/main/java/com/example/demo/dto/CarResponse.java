package com.example.demo.dto;


public class CarResponse {
    private String id;
    // private String carCode;
    private String plate;
    private String model;
    private String manufacturer;
    private String description;
    private String customerId;

    public CarResponse() {}
    public CarResponse(String id, String carCode, String plate, String model, String manufacturer, String description, String customerId) {
        this.id = id;
        // this.carCode = carCode;
        this.plate = plate;
        this.model = model;
        this.manufacturer = manufacturer;
        this.description = description;
        this.customerId = customerId;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // public String getCarCode() { return carCode; }
    // public void setCarCode(String carCode) { this.carCode = carCode; }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
}
