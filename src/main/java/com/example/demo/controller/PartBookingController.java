package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.PartBooking;


import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PartBookingRequest;
import com.example.demo.dto.PartBookingResponse;
import com.example.demo.service.PartBookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/part-bookings")
@CrossOrigin(origins = "*")
public class PartBookingController {

    private final PartBookingService partBookingService;

    public PartBookingController(PartBookingService partBookingService) {
        this.partBookingService = partBookingService;
    }

    // SORT
    @GetMapping("/sort")
    public ResponseEntity<ApiResponse<List<PartBookingResponse>>> sort(
            @RequestParam(defaultValue = "false") boolean asc
    ) {
        try {
            List<PartBooking> list = partBookingService.getAll();
            List<PartBookingResponse> sorted = partBookingService.sortByCreatedAt(list, asc);

            return ResponseEntity.ok(new ApiResponse<>(
                    asc ? "Sắp xếp tăng dần thành công" : "Sắp xếp giảm dần thành công",
                    sorted
            ));
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi khi sắp xếp item nhập", ex);
        }
    }

    // SEARCH
    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm đơn đặt hàng", description = "Tìm theo tên khách hàng, SĐT hoặc mã đơn")
    public ResponseEntity<ApiResponse<List<PartBookingResponse>>> search(
            @RequestParam(required = false) String keyword
    ) {
        try {
            List<PartBookingResponse> data = partBookingService.searchBookings(keyword);
            return ResponseEntity.ok(new ApiResponse<>("Tìm kiếm thành công", data));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    // CREATE
    @PostMapping("/create")
    @Operation(summary = "Tạo đơn đặt hàng")
    public ResponseEntity<ApiResponse<PartBookingResponse>> create(
            @Valid @RequestBody PartBookingRequest request) {
        try {
            PartBookingResponse resp = partBookingService.createBooking(request);
            return ResponseEntity.ok(new ApiResponse<>("Đặt phụ tùng thành công", resp));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        }
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PartBookingResponse>> getById(@PathVariable String id) {
        try {
            PartBookingResponse resp = partBookingService.getById(id);
            return ResponseEntity.ok(new ApiResponse<>("Lấy thông tin thành công", resp));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        }
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<PartBookingResponse>>> getAll() {
        try {
            List<PartBookingResponse> data = partBookingService.getAllBookings();
            return ResponseEntity.ok(new ApiResponse<>("OK", data));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    // CONFIRM
    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<PartBookingResponse>> confirmBooking(@PathVariable String id) {
        try {
            PartBookingResponse resp = partBookingService.confirmBooking(id);
            return ResponseEntity.ok(new ApiResponse<>("Xác nhận đơn hàng thành công", resp));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Lỗi: " + ex.getMessage(), null));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật booking")
    public ResponseEntity<ApiResponse<PartBookingResponse>> update(
            @PathVariable String id,
            @RequestBody PartBookingRequest req) {
        try {
            PartBookingResponse updated = partBookingService.update(id, req);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updated));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    // DELETE ONE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<PartBookingResponse>> delete(@PathVariable String id) {
        try {
            PartBookingResponse resp = partBookingService.deleteBooking(id);
            return ResponseEntity.ok(
                    new ApiResponse<>("Xóa booking thành công (không hoàn kho)", resp)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        }
    }

    // DELETE ALL
    @DeleteMapping
    public ResponseEntity<ApiResponse<List<PartBookingResponse>>> deleteAllBookings() {
        try {
            List<PartBookingResponse> deleted = partBookingService.deleteAllBookingsWithoutRestoringStock();
            return ResponseEntity.ok(new ApiResponse<>("Đã xoá toàn bộ booking (KHÔNG hoàn kho)", deleted));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

}

