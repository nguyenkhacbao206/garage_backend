package com.example.demo.dto;

public class SupplierRequest {
    private String name;
    private String supplierCode;
    private String address;
    private String email;
    private String phone;
    private String description;

    public SupplierRequest() {}

    public SupplierRequest(String name,String supplierCode ,String address, String email, String phone, String description) {
        this.name = name;
        this.supplierCode=supplierCode;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.description = description;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplierCode(){
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode){
        this.supplierCode=supplierCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
