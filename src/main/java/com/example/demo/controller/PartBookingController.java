package com.example.demo.controller;

import com.example.demo.dto.PartBookingRequest;
import com.example.demo.dto.PartBookingResponse;
import com.example.demo.service.PartBookingService;
import com.example.demo.dto.ApiResponse;
import jakarta.validation.Valid;
import com.example.demo.entity.PartBooking;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/part-bookings")
@CrossOrigin(origins = "*")
public class PartBookingController {

    private final PartBookingService partBookingService;

    public PartBookingController(PartBookingService partBookingService) {
        this.partBookingService = partBookingService;
    }

    //SORT
@GetMapping("/sort")
public ResponseEntity<ApiResponse<List<PartBookingResponse>>> sort(
        @RequestParam(defaultValue = "false") boolean asc
) {
    try {
        // Lấy entity
        List<PartBooking> list = partBookingService.getAll();

        // Sort ra response
        List<PartBookingResponse> sorted = partBookingService.sortByCreatedAt(list, asc);

        return ResponseEntity.ok(new ApiResponse<>(
                asc ? "Sắp xếp tăng dần thành công" : "Sắp xếp giảm dần thành công",
                sorted
        ));
    } catch (Exception ex) {
        System.err.println("Lỗi khi sắp xếp item nhập: " + ex.getMessage());
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

    //GET ALL
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
}
