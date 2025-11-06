package com.example.demo.exception;

import com.example.demo.dto.CustomerResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Bắt lỗi chung cho mọi RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomerResponse(ex.getMessage(), null));
    }

    // Bắt lỗi trùng key MongoDB (email/phone bị trùng unique index)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKey(DuplicateKeyException ex) {
        String message = "Dữ liệu bị trùng lặp (email hoặc số điện thoại đã tồn tại)";
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new CustomerResponse(message, null));
    }

    // Bắt lỗi validate dữ liệu (nếu sau này bạn dùng @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomerResponse(message, null));
    }

    // Bắt lỗi không xác định khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomerResponse("Lỗi hệ thống: " + ex.getMessage(), null));
    }
}
