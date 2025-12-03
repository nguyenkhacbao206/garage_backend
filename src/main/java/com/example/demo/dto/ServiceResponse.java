package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Đối tượng phản hồi từ API dịch vụ")
public class ServiceResponse {

    @Schema(description = "Thông báo kết quả API", example = "Thành công")
    private String message;

    @Schema(description = "Dữ liệu trả về (1 dịch vụ hoặc danh sách)")
    private Object data;

    @Schema(description = "Thời gian trả về")
    private LocalDateTime timestamp;

    public ServiceResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ServiceResponse(String message, Object data) {
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // getters/setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
