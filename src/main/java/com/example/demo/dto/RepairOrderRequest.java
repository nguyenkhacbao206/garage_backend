package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RepairOrderRequest {

    private String id;
    private String ordercode;
    private String customerId;
    private String carId;
    private CustomerRequest customer;
    private CarRequest car;
    private List<String> technicianIds;
    private String note;
    private List<RepairOrderItemRequest> parts;
    private List<String> serviceIds;
    private String status;
    private BigDecimal serviceFee;
    private LocalDateTime dateReceived;
    private LocalDateTime dateReturned;


    public RepairOrderRequest() {}

    public String getId() { 
        return id;
    }
    public void setId(String id) { 
        this.id = id;
    }
    public String getOrderCode() {
        return ordercode; 
    }
    public void setOrderCode(String ordercode) {
        this.ordercode = ordercode;
    } 

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

    public CustomerRequest getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerRequest customer) {
        this.customer = customer;
    }

    public CarRequest getCar() {
        return car;
    }

    public void setCar(CarRequest car) {
        this.car = car;
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

    public List<String> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<String> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
