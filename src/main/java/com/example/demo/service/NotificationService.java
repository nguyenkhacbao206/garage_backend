package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository repo,
                               SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }


    //  lưu DB , đẩy real-time WebSocket

    public Notification createNotification(String title, String message) {
        Notification n = new Notification(title, message);
        Notification saved = repo.save(n);

        // Gửi object notification cho FE
        messagingTemplate.convertAndSend("/topic/booking-notification", saved);

        return saved;
    }


    // Gửi thông báo đầy đủ
    public Notification createFull(String title, String message, String bookingId, String type) {
        Notification n = new Notification(title, message, bookingId, type);
        Notification saved = repo.save(n);

        messagingTemplate.convertAndSend("/topic/booking-notification", saved);

        return saved;

    public Notification create(Notification n) {
        return repo.save(n);

    }

    public List<Notification> getAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public List<Notification> getUnread() {
        return repo.findByReadFalseOrderByCreatedAtDesc();
    }

    public Optional<Notification> findById(String id) {
        return repo.findById(id);
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
