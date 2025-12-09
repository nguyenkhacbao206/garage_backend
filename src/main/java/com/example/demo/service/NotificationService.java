package com.example.demo.service;

import com.example.demo.dto.NotificationResponse;
import com.example.demo.entity.GarageService;
import com.example.demo.entity.Notification;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final ServiceBookingRepository bookingRepo;
    private final ServiceRepository serviceRepo;
    private final SimpMessagingTemplate ws;

    public NotificationService(NotificationRepository repo,
                               ServiceBookingRepository bookingRepo,
                               ServiceRepository serviceRepo,
                               SimpMessagingTemplate ws) {
        this.repo = repo;
        this.bookingRepo = bookingRepo;
        this.serviceRepo = serviceRepo;
        this.ws = ws;
    }

    private NotificationResponse toResponse(Notification n) {
        ServiceBooking booking = null;
        List<GarageService> services = List.of();

        if (n.getBookingId() != null && !n.getBookingId().isBlank()) {
            booking = bookingRepo.findById(n.getBookingId()).orElse(null);
            if (booking != null && booking.getServiceIds() != null && !booking.getServiceIds().isEmpty()) {
                services = serviceRepo.findAllById(booking.getServiceIds());
            }
        }
        return new NotificationResponse(n, booking, services);
    }

    // CLIENT gửi thông báo cho ADMIN
    public NotificationResponse sendBookingToAdmin(String bookingId, String clientId) {
        ServiceBooking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Notification noti = new Notification(
                "Có lịch mới",
                "Khách hàng " + booking.getCustomerName()
                        + " vừa đặt lịch xe " + booking.getLicensePlate(),
                bookingId,
                clientId, // senderId = client
                "ADMIN",  // receiver = admin
                "BOOKING"
        );

        Notification saved = repo.save(noti);
        NotificationResponse resp = toResponse(saved);

        // Gửi broadcast cho admin
        ws.convertAndSend("/topic/admin", resp);

        return resp;
    }

    // ADMIN confirm booking → gửi đúng client
    public NotificationResponse confirmByNotification(String notificationId) {
        Notification noti = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        ServiceBooking booking = bookingRepo.findById(noti.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String clientId = noti.getSenderId(); // lấy từ notification

        noti.setStatus("CONFIRMED");
        noti.setTitle("Lịch đã được xác nhận");
        noti.setMessage("Lịch hẹn xe " + booking.getLicensePlate() + " đã được xác nhận");

        Notification saved = repo.save(noti);
        NotificationResponse resp = toResponse(saved);

        // Gửi private message cho client
        ws.convertAndSendToUser(clientId, "/queue/notifications", resp);

        return resp;
    }

    // ADMIN cancel booking → gửi đúng client
    public NotificationResponse cancelByNotification(String notificationId) {
        Notification noti = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        ServiceBooking booking = bookingRepo.findById(noti.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String clientId = noti.getSenderId(); // lấy từ notification

        noti.setStatus("CANCELLED");
        noti.setTitle("Lịch đã bị hủy");
        noti.setMessage("Lịch hẹn xe " + booking.getLicensePlate() 
                        + " đã bị hủy. Vui lòng liên hệ garage để biết thêm chi tiết");

        Notification saved = repo.save(noti);
        NotificationResponse resp = toResponse(saved);

        // Gửi private message cho client
        ws.convertAndSendToUser(clientId, "/queue/notifications", resp);

        return resp;
    }

    // Lấy tất cả thông báo
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
