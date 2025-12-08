package com.example.demo.service;

import com.example.demo.dto.NotificationResponse;
import com.example.demo.entity.Notification;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ServiceBookingRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final ServiceBookingRepository bookingRepo;
    private final SimpMessagingTemplate ws;

    public NotificationService(NotificationRepository repo,
                               ServiceBookingRepository bookingRepo,
                               SimpMessagingTemplate ws) {
        this.repo = repo;
        this.bookingRepo = bookingRepo;
        this.ws = ws;
    }
    
    private NotificationResponse toResponse(Notification n) {
        ServiceBooking booking = null;
        if (n.getBookingId() != null && !n.getBookingId().isBlank()) {
            booking = bookingRepo.findById(n.getBookingId()).orElse(null);
        }
        return new NotificationResponse(n, booking);
    }

    // CLIENT gửi thông báo ADMIN
    public NotificationResponse sendBookingToAdmin(String bookingId, String clientId) {
        Notification noti = new Notification(
                "Có lịch mới",
                "Khách hàng vừa đặt lịch #" + bookingId,
                bookingId,
                clientId,
                "ADMIN",
                "BOOKING"
        );

        Notification saved = repo.save(noti);

        // gửi DTO qua WS để FE dễ dùng
        NotificationResponse resp = toResponse(saved);
        ws.convertAndSend("/topic/admin", resp);

        return resp;
    }

    // ADMIN gửi confirm thông báo sang CLIENT
    public NotificationResponse sendConfirmToClient(String bookingId, String clientId, String adminId) {

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
        NotificationResponse resp = toResponse(saved);
        ws.convertAndSend("/topic/user/" + clientId, resp);
        return resp;
    }

    // ADMIN gửi cancel thông báo sang CLIENT
    public NotificationResponse sendCancelToClient(String bookingId, String clientId, String adminId) {
        Notification noti = new Notification(
                "Lịch đã bị hủy",
                "Admin đã hủy lịch #" + bookingId,
                bookingId,
                adminId,
                clientId,
                "CANCEL"
        );

        noti.setStatus("CANCELLED");

        Notification saved = repo.save(noti);
        NotificationResponse resp = toResponse(saved);
        ws.convertAndSend("/topic/user/" + clientId, resp);
        return resp;
    }

    // trả NotificationResponse 
    public List<NotificationResponse> getAll() {
        return repo.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnread() {
        return repo.findByReadFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getByStatus(String status) {
        return repo.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // mark read -> trả DTO
    public NotificationResponse markAsRead(String id) {
        Notification n = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setRead(true);
        Notification saved = repo.save(n);
        return toResponse(saved);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
