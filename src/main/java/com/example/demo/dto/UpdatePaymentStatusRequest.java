package com.example.demo.dto;

public class UpdatePaymentStatusRequest {
    private String status; // PENDING, SUCCESS, FAILED

    public UpdatePaymentStatusRequest() {}
    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
}
