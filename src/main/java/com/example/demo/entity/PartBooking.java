package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "part_bookings")
public class PartBooking {

    @Id
    private String id;

    private String supplierId;
    private String supplierCode;
    private String bookingCode;     // Mã đơn đặt hàng
    private String partId;          // ID part được đặt
    private String partName;        // Tên part (snapshot)
    private Integer quantity;       // Số lượng đặt
    private String note;
    private Integer remainingStock; // Số lượng còn lại sau khi trừ
    private Double price;
    private String phone;
    private String address;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // GETTER / SETTER
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplierId() { return supplierId;}
    public void setSupplierId(String supplierId) {this.supplierId =supplierId;}

    public String getSupplierCode() {return supplierCode;}
    public void setSupplierCode(String supplierCode) {this.supplierCode = supplierCode;}

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }
    
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getNote() { return note;}
    public void setNote(String note) { this.note = note;}

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() { return phone;}
    public void setPhone(String phone) {this.phone=phone;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address=address;}

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

}
