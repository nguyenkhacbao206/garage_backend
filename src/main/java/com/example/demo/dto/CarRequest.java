package com.example.demo.dto;

public class CarRequest {
    private String plate;
    private String model;
    private String manufacturer;
    private String description;
    private String customerId;

    // Constructor không tham số (bắt buộc cho Swagger/Jackson)
    public CarRequest() {}

    // Getter & Setter
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
