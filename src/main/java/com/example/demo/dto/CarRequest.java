package com.example.demo.dto;

public class CarRequest {
    private String plate;
    private String model;
    private String manufacturer;
    private String description;
    private String customerId; 
    private Boolean active;    

    public CarRequest() {}

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

    public Boolean getActive() { 
        return active; 
    }
    public void setActive(Boolean active) { 
        this.active = active; 
    }
}
