package com.example.demo.dto;

import com.example.demo.entity.Notification;
import com.example.demo.entity.ServiceBooking;

import java.time.LocalDateTime;

public class NotificationResponse {

    private String id;
    private String title;
    private String message;
    private String type;
    private String status;
    private boolean read;
    private LocalDateTime createdAt;

    private ServiceBooking booking; 

    public NotificationResponse(Notification n, ServiceBooking booking) {
        this.id = n.getId();
        this.title = n.getTitle();
        this.message = n.getMessage();
        this.type = n.getType();
        this.status = n.getStatus();
        this.read = n.isRead();
        this.createdAt = n.getCreatedAt();
        this.booking = booking;
    }

    // getter
    public String getId() { 
        return id; 
    }
    public String getTitle() { 
        return title; 
    }
    public String getMessage() { 
        return message; 
    }
    public String getType() { 
        return type; 
    }
    public String getStatus() { 
        return status; 
    }
    public boolean isRead() { 
        return read; 
    }
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public ServiceBooking getBooking() { 
        return booking; 
    }
}
