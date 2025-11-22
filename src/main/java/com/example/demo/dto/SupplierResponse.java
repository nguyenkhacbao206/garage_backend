package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Đối tượng phản hồi từ API nhà cung cấp")
public class SupplierResponse {

    @Schema(description = "Thông báo kết quả API", example = "Thành công")
    private String message;

    @Schema(description = "Dữ liệu trả về (1 nhà cung cấp hoặc danh sách)")
    private Object data;

    @Schema(description = "Mã nhà cung cấp (chỉ hiển thị khi lấy 1 đối tượng)", example = "NCC-010")
    private String supplierCode;

    public SupplierResponse() {}

    public SupplierResponse(String message, Object data) {
        this.message = message;
        this.data = data;

        if (data instanceof com.example.demo.entity.Supplier) {
            this.supplierCode = ((com.example.demo.entity.Supplier) data).getSupplierCode();
        }
    }

    // getter / setter
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) {
        this.data = data;
        if (data instanceof com.example.demo.entity.Supplier) {
            this.supplierCode = ((com.example.demo.entity.Supplier) data).getSupplierCode();
        }
    }

    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
}
