package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Đối tượng phản hồi từ API đặt phụ tùng")
public class PartBookingResponse {

    @Schema(description = "Thông báo kết quả API", example = "Đặt phụ tùng thành công")
    private String message;

    @Schema(description = "Dữ liệu trả về (chi tiết booking hoặc danh sách booking)")
    private Object data;

    @Schema(description = "Mã đặt phụ tùng (chỉ hiển thị khi tạo mới hoặc xem chi tiết)", example = "BOOK-025")
    private String bookingCode;

    public PartBookingResponse() {}

    // Constructor cơ bản
    public PartBookingResponse(String message, Object data) {
        this.message = message;
        this.data = data;

        // Nếu data là 1 PartBooking => tự lấy bookingCode
        if (data instanceof com.example.demo.entity.PartBooking) {
            this.bookingCode = ((com.example.demo.entity.PartBooking) data).getBookingCode();
        }
    }

    //GETTER / SETTER
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;

        if (data instanceof com.example.demo.entity.PartBooking) {
            this.bookingCode = ((com.example.demo.entity.PartBooking) data).getBookingCode();
        }
    }

    public String getBookingCode() {
        return bookingCode;
    }
    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }
}
