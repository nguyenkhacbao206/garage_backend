package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.NotificationResponse;
import com.example.demo.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
@Tag(name = "Notifications", description = "API quản lý thông báo")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAll() {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Danh sách tất cả thông báo",
                service.getAll()
            )
        );
    }

    // UNREAD
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnread() {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Danh sách thông báo chưa đọc",
                service.getUnread()
            )
        );
    }

    // CONFIRMED
    @GetMapping("/confirmed")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getConfirmed() {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Danh sách thông báo đã xác nhận",
                service.getByStatus("CONFIRMED")
            )
        );
    }

    // CANCELLED
    @GetMapping("/cancelled")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getCancelled() {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Danh sách thông báo đã hủy",
                service.getByStatus("CANCELLED")
            )
        );
    }

    // PENDING (NEW)
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getPending() {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Danh sách thông báo chưa xác nhận",
                service.getByStatus("NEW")
            )
        );
    }

    // MARK READ
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markRead(@PathVariable String id) {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Đánh dấu đã đọc",
                service.markAsRead(id)
            )
        );
    }

    // DELETE ONE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(
            new ApiResponse<>("Xóa thông báo thành công", null)
        );
    }

    // DELETE ALL
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> deleteAll() {
        service.deleteAll();
        return ResponseEntity.ok(
            new ApiResponse<>("Xóa toàn bộ thông báo", null)
        );
    }

    // CONFIRM
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<NotificationResponse>> confirm(
            @RequestParam String bookingId,
            @RequestParam String clientId,
            @RequestParam String adminId
    ) {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Xác nhận lịch thành công",
                service.sendConfirmToClient(bookingId, clientId, adminId)
            )
        );
    }

    // CANCEL
    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<NotificationResponse>> cancel(
            @RequestParam String bookingId,
            @RequestParam String clientId,
            @RequestParam String adminId
    ) {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Hủy lịch thành công",
                service.sendCancelToClient(bookingId, clientId, adminId)
            )
        );
    }

    // BOOKING NOTIFY
    @PostMapping("/booking")
    public ResponseEntity<ApiResponse<NotificationResponse>> bookingNotify(
            @RequestParam String bookingId,
            @RequestParam String clientId
    ) {
        return ResponseEntity.ok(
            new ApiResponse<>(
                "Gửi thông báo đặt lịch thành công",
                service.sendBookingToAdmin(bookingId, clientId)
            )
        );
    }
}
