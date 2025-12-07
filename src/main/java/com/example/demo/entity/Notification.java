package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String title;
    private String message;
    private String bookingId;
    private boolean read;
    private String type;
    private String title;      
    private String message;    
    private String bookingId;  
    private boolean read;      
    private String type;       
    private LocalDateTime createdAt;

    public Notification() {}



    // Constructor đầy đủ
    public Notification(String title, String message, String bookingId, String type) {
        this.title = title;
        this.message = message;
        this.bookingId = bookingId;
        this.type = type;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    public Notification(String title, String message) {
        this(title, message, null, null);
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getBookingId() {
        return bookingId;
    }

    public boolean isRead() {
        return read;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // constructor thêm vào để tạo nhanh thông báo 
    public Notification(String title, String message) {
        this.title = title;
        this.message = message;
        this.bookingId = null;
        this.type = null;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    // GETTERS & SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
