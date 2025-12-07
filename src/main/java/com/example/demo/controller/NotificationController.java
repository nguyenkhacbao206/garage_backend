package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Lấy tất cả thông báo
    @GetMapping
    public List<Notification> getAll() {
        return service.getAll();
    }

    // Tạo thông báo đơn giản
    @PostMapping
    public Notification createSimple(@RequestBody Notification req) {
        return service.createNotification(req.getTitle(), req.getMessage());
    }

    // Tạo thông báo đầy đủ
    @PostMapping("/full")
    public Notification createFull(@RequestBody Notification req) {
        return service.createFull(
                req.getTitle(),
                req.getMessage(),
                req.getBookingId(),
                req.getType()
        );
    }

    // Lấy thông báo chưa đọc
    @GetMapping("/unread")
    public List<Notification> getUnread() {
        return service.getUnread();
    }

    // Đánh dấu là đã đọc
    @PutMapping("/{id}/read")
    public String markAsRead(@PathVariable String id) {
        service.markAsRead(id);
        return "Đã đánh dấu đã đọc";
    }

    // Xóa thông báo
    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        service.delete(id);
        return "Đã xóa thông báo";
    }
}
