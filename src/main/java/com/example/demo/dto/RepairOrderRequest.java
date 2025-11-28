package com.example.demo.dto;

import java.util.List;

public class RepairOrderRequest {
    private String customerId;
    private String carId;
    private List<String> technicianIds;
    private String note;
    private List<RepairOrderItemRequest> parts;
    private List<RepairOrderItemRequest> services;
    private String status;

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

    public List<String> getTechnicianIds() { 
        return technicianIds; 
    }
    public void setTechnicianIds(List<String> technicianIds) { 
        this.technicianIds = technicianIds; 
    }

    public String getNote() { 
        return note; 
    }
    public void setNote(String note) { 
        this.note = note; 
    }

    public List<RepairOrderItemRequest> getParts() { 
        return parts; 
    }
    public void setParts(List<RepairOrderItemRequest> parts) { 
        this.parts = parts; 
    }

    public List<RepairOrderItemRequest> getServices() { 
        return services; 
    }
    public void setServices(List<RepairOrderItemRequest> services) { 
        this.services = services; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
}
