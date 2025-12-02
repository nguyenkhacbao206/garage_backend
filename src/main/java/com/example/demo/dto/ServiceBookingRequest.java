package com.example.demo.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class ServiceBookingRequest {

    @Schema(description = "ID của khách hàng", example = "CUST-001")
    private String customerId;

    @Schema(description = "ID của dịch vụ", example = "SERV-999")
    private String serviceId;

    @Schema(description = "Ghi chú thêm từ khách hàng", example = "Tôi muốn đến sớm 15 phút")
    private String note;

    @Schema(description = "Thời gian đặt lịch", example = "2023-12-25T10:00:00")
    private LocalDateTime bookingTime;

    public ServiceBookingRequest() {}

    public String getCustomerId() { 
        return customerId; 
    }
    public void setCustomerId(String customerId) { 
        this.customerId = customerId; 
    }

    public String getServiceId() { 
        return serviceId; 
    }
    public void setServiceId(String serviceId) { 
        this.serviceId = serviceId; 
    }

    public String getNote() { 
        return note; 
    }
    public void setNote(String note) { 
        this.note = note; 
    }

    public LocalDateTime getBookingTime() { 
        return bookingTime; 
    }
    public void setBookingTime(LocalDateTime bookingTime) { 
        this.bookingTime = bookingTime; 
    }
}
