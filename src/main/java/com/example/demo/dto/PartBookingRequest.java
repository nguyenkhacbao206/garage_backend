package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Yêu cầu đặt phụ tùng (Part Booking)")
public class PartBookingRequest {

    @Schema(description = "ID nhà cung cấp", example = "SUP001")
    @NotBlank
    private String supplierId;

    @Schema(description = "Mã nhà cung cấp", example = "NCC-01")
    private String supplierCode;

    @Schema(description = "ID linh kiện cần đặt", example = "PT-024")
    @NotBlank
    private String partId;

    @Schema(description = "Tên linh kiện", example = "Má phanh trước")
    private String partName;

    @Schema(description = "Số lượng đặt", example = "3")
    @NotNull
    @Min(1)
    private Integer quantity;

    @Schema(description = "Ghi chú thêm", example = "Lắp cho khách trong ngày")
    private String note;

    @Schema(description = "Tên khách hàng", example = "Nguyen Van B")
    private String customerName;

    @Schema(description = "Số điện thoại khách hàng", example = "0988777555")
    private String phone;

    @Schema(description = "Địa chỉ khách hàng", example = "Hanoi")
    private String address;

    @Schema(description = "Trạng đặt hàng", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Email", example = "tr1223@")
    private String customerEmail;

    //GETTER / SETTER
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }

    public String getPartId() { return partId; }
    public void setPartId(String partId) { this.partId = partId; }

    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public Boolean isActive() { return isActive; }
    public void setIsActive(Boolean isActive ) { this.isActive = isActive; }

    public String getCustomerEmail() { return customerEmail;}
    public void setCustomerEmail(String customerEmail) {this.customerEmail =customerEmail;}
}
