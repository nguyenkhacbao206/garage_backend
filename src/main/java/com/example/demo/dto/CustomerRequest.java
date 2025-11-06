package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CustomerRequest {

    @Schema(description = "Tên khách hàng", example = "Nguyen Van A")
    private String name;

    @Schema(description = "Số điện thoại", example = "0987654321")
    private String phone;

    @Schema(description = "Email khách hàng", example = "a.nguyen@gmail.com")
    private String email;

    @Schema(description = "Địa chỉ", example = "Hanoi")
    private String address;

    @Schema(description = "Ghi chú", example = "Khách VIP")
    private String note;

    // getters / setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
