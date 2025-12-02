package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ServiceBookingRequest;
import com.example.demo.dto.ServiceBookingResponse;
import com.example.demo.service.ServiceBookingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "Tạo đặt lịch", description = "Tạo một đặt lịch mới cho khách hàng")
    public ResponseEntity<ApiResponse<ServiceBookingResponse>> create(
            @RequestBody ServiceBookingRequest req) {
        try {
            ServiceBookingResponse data = bookingService.create(req);
            return ResponseEntity.ok(new ApiResponse<>("Đặt dịch vụ thành công!", data));
        } catch (Exception e) {
            throw e;
        }
    }

   
    @GetMapping
    @Operation(summary = "Lấy danh sách đặt lịch", description = "Trả về toàn bộ danh sách các đặt lịch dịch vụ")
    public ResponseEntity<ApiResponse<List<ServiceBookingResponse>>> getAll() {
        try {
            List<ServiceBookingResponse> list = bookingService.getAll();
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", list));
        } catch (Exception e) {
            throw e;
        }
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết đặt lịch theo ID", description = "Trả về thông tin chi tiết của một đặt lịch dựa theo ID")
    public ResponseEntity<ApiResponse<ServiceBookingResponse>> getById(
            @Parameter(description = "ID của đơn đặt lịch", required = true)
            @PathVariable String id) {
        try {
            return bookingService.getById(id)
                    .map(item -> ResponseEntity.ok(new ApiResponse<>("OK", item)))
                    .orElseGet(() -> ResponseEntity.badRequest().body(new ApiResponse<>("Không tìm thấy!", null)));
        } catch (Exception e) {
            throw e;
        }
    }

    
    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "Cập nhật trạng thái đặt lịch", description = "Thay đổi trạng thái của một đặt lịch theo ID")
    public ResponseEntity<ApiResponse<ServiceBookingResponse>> updateStatus(
            @Parameter(description = "ID của đơn đặt lịch", required = true)
            @PathVariable String id,
            @Parameter(description = "Trạng thái mới của đơn đặt lịch", required = true)
            @PathVariable String status) {

        try {
            ServiceBookingResponse updated = bookingService.updateStatus(id, status);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật trạng thái thành công", updated));
        } catch (Exception e) {
            throw e;
        }
    }

   
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa đặt lịch", description = "Xóa một đặt lịch theo ID")
    public ResponseEntity<ApiResponse<String>> delete(
            @Parameter(description = "ID của đơn đặt lịch cần xóa", required = true)
            @PathVariable String id) {
        try {
            bookingService.delete(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa thành công", null));
        } catch (Exception e) {
            throw e;
        }
    }
}
