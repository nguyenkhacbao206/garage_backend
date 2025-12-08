package com.example.demo.dto;

import com.example.demo.entity.GarageService;
import com.example.demo.entity.Notification;
import com.example.demo.entity.ServiceBooking;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponse {

    private String id;
    private String title;
    private String message;
    private String type;
    private String status;
    private boolean read;
    private LocalDateTime createdAt;

    private ServiceBooking booking; 
    private List<GarageService> services;

    public NotificationResponse(Notification n, ServiceBooking booking, List<GarageService> services) {
        this.id = n.getId();
        this.title = n.getTitle();
        this.message = n.getMessage();
        this.type = n.getType();
        this.status = n.getStatus();
        this.read = n.isRead();
        this.createdAt = n.getCreatedAt();
        this.booking = booking;
        this.services = services;
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
    public List<GarageService> getServices() {
        return services;
    }
}
