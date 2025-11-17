package com.example.demo.dto;

import java.util.List;

public class RepairOrderRequest {
    private String customerId;
    private String carId;
    private String technicianId; 
    private String status;        
    private List<RepairOrderItemRequest> items;

    public RepairOrderRequest() {}

    public String getCustomerId() { 
        return customerId; 
    }
    public void setCustomerId(String customerId) { 
        this.customerId = customerId; 
    }

    public String getCarId() { 
        return carId; 
    }
    public void setCarId(String carId) { 
        this.carId = carId; 
    }

    public String getTechnicianId() { 
        return technicianId; 
    }
    public void setTechnicianId(String technicianId) { 
        this.technicianId = technicianId; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public List<RepairOrderItemRequest> getItems() { 
        return items; 
    }
    public void setItems(List<RepairOrderItemRequest> items) { 
        this.items = items; 
    }
}
