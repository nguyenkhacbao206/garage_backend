package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentResponse {

    private String id;
    private String repairOrderId;
    // private RepairOrder repairOrder;
    private RepairOrderResponse repairOrder;

    private BigDecimal amount;
    private String method;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentResponse() {}

    public PaymentResponse(String id, String repairOrderId, 
                           RepairOrderResponse repairOrder,
                           BigDecimal amount, String method,
                           String status, LocalDateTime createdAt,
                           LocalDateTime updatedAt) {

        this.id = id;
        this.repairOrderId = repairOrderId;
        this.repairOrder = repairOrder;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ---- GETTER / SETTER ----

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getRepairOrderId() {
        return repairOrderId;
    }
    public void setRepairOrderId(String repairOrderId) {
        this.repairOrderId = repairOrderId;
    }

   
    public RepairOrderResponse getRepairOrder() {
        return repairOrder;
    }
    public void setRepairOrder(RepairOrderResponse repairOrder) {
        this.repairOrder = repairOrder;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
