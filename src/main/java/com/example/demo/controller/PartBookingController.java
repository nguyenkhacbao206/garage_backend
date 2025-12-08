
package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PartBookingRequest;
import com.example.demo.dto.PartBookingResponse;
import com.example.demo.entity.PartBooking;
import com.example.demo.service.PartBookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/part-bookings")
@CrossOrigin(origins = "*")
public class PartBookingController {

    private final PartBookingService partBookingService;

    public PartBookingController(PartBookingService partBookingService) {
        this.partBookingService = partBookingService;
    }

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

    @PostMapping("/create")
    public ResponseEntity<PartBookingResponse> create(@Valid @RequestBody PartBookingRequest request) {
        try {
            PartBookingResponse resp = partBookingService.createBooking(request);
            return ResponseEntity.ok(
                    new PartBookingResponse("Đặt phụ tùng thành công", resp)
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartBookingResponse> getById(@PathVariable String id) {
        try {
            PartBookingResponse resp = partBookingService.getById(id);
            return ResponseEntity.ok(
                    new PartBookingResponse("Lấy thông tin booking thành công", resp)
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PartBookingResponse>>> getAll() {
        try {
            List<PartBookingResponse> data = partBookingService.getAllBookings();
            return ResponseEntity.ok(new ApiResponse<>("OK", data));
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<PartBookingResponse>> confirmBooking(@PathVariable String id) {
        try {
            PartBookingResponse resp = partBookingService.confirmBooking(id);
            return ResponseEntity.ok(new ApiResponse<>("Xác nhận đơn hàng thành công", resp));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi: " + ex.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PartBookingResponse> delete(@PathVariable String id) {
        try {
            PartBookingResponse resp = partBookingService.deleteBooking(id);
            return ResponseEntity.ok(
                    new PartBookingResponse("Xóa booking thành công. Tồn kho đã được hoàn trả (nếu part tồn tại)", resp)
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<ApiResponse<List<PartBookingResponse>>> deleteAllBookings() {
        try {
            List<PartBookingResponse> deletedBookings = partBookingService.deleteAllBookingsWithoutRestoringStock();
            return ResponseEntity.ok(new ApiResponse<>("Xóa tất cả booking thành công", deletedBookings));
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

}