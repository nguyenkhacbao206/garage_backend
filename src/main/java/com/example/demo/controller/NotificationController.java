package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
@Tag(name = "Notifications", description = "API quản lý khách hàng")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread() {
        return ResponseEntity.ok(service.getUnread());
    }

    // tất cả xác nhận
    @GetMapping("/confirmed")
    public ResponseEntity<List<Notification>> getConfirmed() {
        return ResponseEntity.ok(service.getByStatus("CONFIRMED"));
    }

    // tất cả hủy
    @GetMapping("/cancelled")
    public ResponseEntity<List<Notification>> getCancelled() {
        return ResponseEntity.ok(service.getByStatus("CANCELLED"));
    }

    // tất cả chưa xác nhận
    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPending() {
        return ResponseEntity.ok(service.getByStatus("NEW"));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markRead(@PathVariable String id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAll() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Notification> confirm(
            @RequestParam String bookingId,
            @RequestParam String clientId,
            @RequestParam String adminId
    ) {
        return ResponseEntity.ok(service.sendConfirmToClient(bookingId, clientId, adminId));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Notification> cancel(
            @RequestParam String bookingId,
            @RequestParam String clientId,
            @RequestParam String adminId
    ) {
        return ResponseEntity.ok(service.sendCancelToClient(bookingId, clientId, adminId));
    }
}
