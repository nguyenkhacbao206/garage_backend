package com.example.demo.entity;

import java.time.LocalDateTime;

public class PaymentHistoryItem {
    private String status;
    private String method;
    private LocalDateTime updatedAt;

    public PaymentHistoryItem() {}

    public PaymentHistoryItem(String status, String method, LocalDateTime updatedAt) {
        this.status = status;
        this.method = method;
        this.updatedAt = updatedAt;
    }

    // getters / setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
