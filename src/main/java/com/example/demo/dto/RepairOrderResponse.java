package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.Technician;

public class RepairOrderResponse {
    private String id;
    private String orderCode;
    private String customerId;
    private CustomerResponse customer;  
    private String carId;
    private CarResponse car;           
    private List<String> technicianIds;
    private List<TechnicianResponse> technician;
    private String note;
    private String status;
    private List<RepairOrderItemResponse> parts;
    private List<String> serviceIds;
    private List<GarageServiceResponse> service;
    private BigDecimal estimatedTotal;
    private BigDecimal serviceFee;
    private LocalDateTime dateReceived;
    private LocalDateTime dateReturned;

    // getters và setters
    public String getId() { 
        return id;
    }
    public void setId(String id) { 
        this.id = id;
    }

    public String getOrderCode() { 
        return orderCode;
    }
    public void setOrderCode(String orderCode) { 
        this.orderCode = orderCode;
    }

    public String getCustomerId() { 
        return customerId;
    }
    public void setCustomerId(String customerId) { 
        this.customerId = customerId;
    }

    public CustomerResponse getCustomer() { 
        return customer;
    }
    public void setCustomer(CustomerResponse customer) { 
        this.customer = customer;
    }

    public String getCarId() { 
        return carId;
    }
    public void setCarId(String carId) { 
        this.carId = carId;
    }

    public CarResponse getCar() { 
        return car;
    }
    public void setCar(CarResponse car) { 
        this.car = car;
    }

    public List<String> getTechnicianIds() { 
        return technicianIds;
    }
    public void setTechnicianIds(List<String> technicianIds) { 
        this.technicianIds = technicianIds;
    }
    public List<TechnicianResponse> getTechnician() {
        return technician;
    }
    public void setTechnician(List<TechnicianResponse> technician) {
        this.technician = technician;
    }

    public String getNote() { 
        return note;
    }
    public void setNote(String note) { 
        this.note = note;
    }

    public String getStatus() { 
        return status;
    }
    public void setStatus(String status) { 
        this.status = status;
    }

    public List<RepairOrderItemResponse> getParts() { 
        return parts;
    }
    public void setParts(List<RepairOrderItemResponse> parts) { 
        this.parts = parts;
    }

    public List<String> getServiceIds() { 
        return serviceIds;
    }
    public void setServiceIds(List<String> serviceIds) { 
        this.serviceIds = serviceIds;
    }

    public List<GarageServiceResponse> getService() {
        return service;
    }
    public void setService(List<GarageServiceResponse> service) {
        this.service = service;
    }

    public BigDecimal getEstimatedTotal() { 
        return estimatedTotal;
    }
    public void setEstimatedTotal(BigDecimal estimatedTotal) { 
        this.estimatedTotal = estimatedTotal;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }


    public LocalDateTime getDateReceived() { 
        return dateReceived;
    }
    public void setDateReceived(LocalDateTime dateReceived) { 
        this.dateReceived = dateReceived;
    }

    public LocalDateTime getDateReturned() { 
        return dateReturned;
    }
    public void setDateReturned(LocalDateTime dateReturned) { 
        this.dateReturned = dateReturned;
    }
}
