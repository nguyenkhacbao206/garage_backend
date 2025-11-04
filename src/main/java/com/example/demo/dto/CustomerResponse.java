package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CustomerResponse {

    @Schema(description = "Thông báo kết quả API", example = "Thêm khách hàng thành công")
    private String message;

    @Schema(description = "Dữ liệu trả về, có thể là object hoặc list")
    private Object data;

    public CustomerResponse() {}
    public CustomerResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    // getters / setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
