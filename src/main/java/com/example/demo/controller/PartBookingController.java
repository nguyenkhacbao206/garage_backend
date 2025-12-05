package com.example.demo.controller;

import com.example.demo.dto.PartBookingRequest;
import com.example.demo.dto.PartBookingResponse;
import com.example.demo.service.PartBookingService;
import jakarta.validation.Valid;
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
