package com.example.demo.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class ServiceBookingResponse {

    @Schema(description = "ID duy nhất của đơn đặt", example = "BOOK-123456")
    private String id;

    @Schema(description = "ID khách hàng", example = "CUST-001")
    private String customerId;

    @Schema(description = "ID dịch vụ", example = "SERV-999")
    private String serviceId;

    @Schema(description = "Ghi chú", example = "Làm kỹ phần gầm xe")
    private String note;

    @Schema(description = "Trạng thái đơn hàng", example = "PENDING")
    private String status;

    @Schema(description = "Thời gian đặt hẹn")
    private LocalDateTime bookingTime;

    @Schema(description = "Thời gian tạo đơn")
    private LocalDateTime createdAt;

    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

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

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public LocalDateTime getBookingTime() { 
        return bookingTime; 
    }
    public void setBookingTime(LocalDateTime bookingTime) { 
        this.bookingTime = bookingTime; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}
