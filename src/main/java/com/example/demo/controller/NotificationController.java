package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Lấy tất cả
    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // Lấy chưa đọc
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread() {
        return ResponseEntity.ok(service.getUnread());
    }

    // Đánh dấu đã đọc
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markRead(@PathVariable String id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }

    // Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Admin confirm booking 
    @PostMapping("/confirm")
    public ResponseEntity<Notification> confirm(
            @RequestParam String bookingId,
            @RequestParam String clientId,
            @RequestParam String adminId
    ) {
        return ResponseEntity.ok(service.sendConfirmToClient(bookingId, clientId, adminId));
    }
}
