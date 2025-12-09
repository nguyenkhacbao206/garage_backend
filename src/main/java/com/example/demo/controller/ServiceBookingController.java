package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ServiceBookingRequest;
import com.example.demo.dto.ServiceBookingResponse;
import com.example.demo.service.ServiceBookingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
@Tag(name = "Service Booking API", description = "API quản lý đặt lịch dịch vụ")
public class ServiceBookingController {

    private final ServiceBookingService bookingService;

    public ServiceBookingController(ServiceBookingService bookingService) {
        this.bookingService = bookingService;
    }

   
    @PostMapping
    @Operation(summary = "Tạo đặt lịch")
    public ResponseEntity<ApiResponse<ServiceBookingResponse>> create(
            @RequestBody ServiceBookingRequest req) {

        try {
            ServiceBookingResponse data = bookingService.create(req);
            return ResponseEntity.ok(new ApiResponse<>("Đặt dịch vụ thành công!", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    
    @GetMapping
    @Operation(summary = "Lấy tất cả đặt lịch")
    public ResponseEntity<ApiResponse<List<ServiceBookingResponse>>> getAll() {

        try {
            List<ServiceBookingResponse> list = bookingService.getAll();
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Lấy đặt lịch theo ID")
    public ResponseEntity<ApiResponse<ServiceBookingResponse>> getById(
            @PathVariable String id) {

        try {
            ServiceBookingResponse item = bookingService.getOne(id);
            return ResponseEntity.ok(new ApiResponse<>("OK", item));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

   
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật đặt lịch theo ID")
    public ResponseEntity<ApiResponse<ServiceBookingResponse>> update(
            @PathVariable String id,
            @RequestBody ServiceBookingRequest req) {

        try {
            ServiceBookingResponse updated = bookingService.update(id, req);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

   
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa đặt lịch theo ID")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {

        try {
            bookingService.delete(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
