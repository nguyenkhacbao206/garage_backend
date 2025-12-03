package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Đối tượng phản hồi từ API dịch vụ")
public class ServiceResponse {

    @Schema(description = "Thông báo kết quả API", example = "Thành công")
    private String message;

    @Schema(description = "Dữ liệu trả về (1 dịch vụ hoặc danh sách)")
    private Object data;

    public ServiceResponse() {}

    public ServiceResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    // Getter / Setter
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
