package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final SimpMessagingTemplate ws;

    public NotificationService(NotificationRepository repo,
                               SimpMessagingTemplate ws) {
        this.repo = repo;
        this.ws = ws;
    }

    // Client đặt lịch , Admin nhận
    public Notification sendBookingToAdmin(String bookingId, String clientId) {
        Notification noti = new Notification(
                "Có lịch mới",
                "Khách hàng vừa đặt lịch #" + bookingId,
                bookingId,
                clientId,
                "ADMIN",
                "BOOKING"
        );

        Notification saved = repo.save(noti);
        ws.convertAndSend("/topic/admin", saved);
        return saved;
    }

    // Admin xác nhận , Client nhận
    public Notification sendConfirmToClient(String bookingId, String clientId, String adminId) {
        Notification noti = new Notification(
                "Lịch đã được xác nhận",
                "Admin đã xác nhận lịch #" + bookingId,
                bookingId,
                adminId,
                clientId,
                "CONFIRM"
        );

        noti.setStatus("CONFIRMED");

        Notification saved = repo.save(noti);
        ws.convertAndSend("/topic/user/" + clientId, saved);
        return saved;
    }

    public List<Notification> getAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public List<Notification> getUnread() {
        return repo.findByReadFalseOrderByCreatedAtDesc();
    }

    public Notification markAsRead(String id) {
        Notification n = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setRead(true);
        return repo.save(n);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
