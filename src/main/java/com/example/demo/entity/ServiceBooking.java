package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "service_bookings")
public class ServiceBooking {

    @Id
    private String id;

    // Thông tin khách hàng
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // Thông tin xe
    private String licensePlate;
    private String carBrand;
    private String carModel;

    // Danh sách dịch vụ
    private List<String> serviceIds;

    // Ghi chú , trạng thái
    private String note;
    private String status; // PENDING, CONFIRMED, CANCELLED,...

    // Thời gian đặt lịch
    private LocalDateTime bookingTime;

    // Thời gian hệ thống
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServiceBooking() {}


    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

    public String getCustomerName() { 
        return customerName; 
    }
    public void setCustomerName(String customerName) { 
        this.customerName = customerName; 
    }

    public String getCustomerPhone() { 
        return customerPhone; 
    }
    public void setCustomerPhone(String customerPhone) { 
        this.customerPhone = customerPhone; 
    }

    public String getCustomerEmail() { 
        return customerEmail; 
    }
    public void setCustomerEmail(String customerEmail) { 
        this.customerEmail = customerEmail; 
    }

    public String getLicensePlate() { 
        return licensePlate; 
    }
    public void setLicensePlate(String licensePlate) { 
        this.licensePlate = licensePlate; 
    }

    public String getCarBrand() { 
        return carBrand; 
    }
    public void setCarBrand(String carBrand) { 
        this.carBrand = carBrand; 
    }

    public String getCarModel() { 
        return carModel; 
    }
    public void setCarModel(String carModel) { 
        this.carModel = carModel; 
    }

    public List<String> getServiceIds() { 
        return serviceIds; 
    }
    public void setServiceIds(List<String> serviceIds) { 
        this.serviceIds = serviceIds; 
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

    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }
}
