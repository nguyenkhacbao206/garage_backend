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

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Lấy tất cả (có thể thêm paging nếu muốn)
    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }

    // Lấy chỉ chưa đọc
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread() {
        return ResponseEntity.ok(notificationService.getUnread());
    }

    // Đánh dấu đã đọc
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markRead(@PathVariable String id) {
        Notification updated = notificationService.markAsRead(id);
        return ResponseEntity.ok(updated);
    }

    // Xóa thông báo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
