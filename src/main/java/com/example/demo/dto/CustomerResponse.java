package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Đối tượng phản hồi từ API khách hàng")
public class CustomerResponse {

    @Schema(description = "Thông báo kết quả API", example = "Thêm khách hàng thành công")
    private String message;

    @Schema(description = "Dữ liệu trả về (có thể là danh sách khách hàng hoặc 1 khách hàng cụ thể)")
    private Object data;

    @Schema(description = "Mã khách hàng (chỉ hiển thị khi tạo mới hoặc xem chi tiết)", example = "KH-025")
    private String customerCode;

    public CustomerResponse() {}

    // Constructor cơ bản (message + data)
    public CustomerResponse(String message, Object data) {
        this.message = message;
        this.data = data;

        // Nếu data là 1 khách hàng thì tự động lấy mã khách hàng
        if (data instanceof com.example.demo.entity.Customer) {
            this.customerCode = ((com.example.demo.entity.Customer) data).getCustomerCode();
        }
    }

    // getters / setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) {
        this.data = data;
        if (data instanceof com.example.demo.entity.Customer) {
            this.customerCode = ((com.example.demo.entity.Customer) data).getCustomerCode();
        }
    }

    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
}
