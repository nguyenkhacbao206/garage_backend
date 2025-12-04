package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dữ liệu yêu cầu xác thực OTP")
public class OtpRequest {
    
    @Schema(description = "Mã OTP 6 chữ số", example = "123456")
    @NotBlank(message = "Mã OTP không được để trống")
    @Pattern(regexp = "\\d{6}", message = "Mã OTP phải gồm 6 chữ số")
    private String otp;

    public OtpRequest() {}

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}