package com.example.demo.dto;

import java.time.LocalDateTime;

public class ServiceBookingRequest {

    // --- Khách hàng ---
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // --- Xe ---
    private String licensePlate;
    private String carBrand;
    private String carModel;

    // --- Dịch vụ ---
    private String serviceId;
    private String note;

    private LocalDateTime bookingTime;

    public ServiceBookingRequest() {}

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getCarBrand() { return carBrand; }
    public void setCarBrand(String carBrand) { this.carBrand = carBrand; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
}
