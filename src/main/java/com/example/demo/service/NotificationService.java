package com.example.demo.service;

import com.example.demo.dto.NotificationResponse;
import com.example.demo.entity.GarageService;
import com.example.demo.entity.Notification;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ServiceBookingRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.example.demo.repository.ServiceRepository;

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

            if (booking != null &&
                booking.getServiceIds() != null &&
                !booking.getServiceIds().isEmpty()) {

                services = serviceRepo.findAllById(booking.getServiceIds());
            }
        }

        return new NotificationResponse(n, booking, services);
    }

    // CLIENT gửi thông báo ADMIN
    public NotificationResponse sendBookingToAdmin(String bookingId, String clientId) {

        ServiceBooking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String message = "Khách hàng " + booking.getCustomerName()
                + " vừa đặt lịch cho xe " + booking.getLicensePlate();

        Notification noti = new Notification(
                "Có lịch mới",
                message,
                bookingId,
                clientId,
                "ADMIN",
                "BOOKING"
        );

        Notification saved = repo.save(noti);
        NotificationResponse resp = toResponse(saved);
        ws.convertAndSend("/topic/admin", resp);

        return resp;
    }

    // ADMIN gửi confirm thông báo sang CLIENT
    public NotificationResponse confirmByNotification(String notificationId) {

        Notification noti = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if ("CONFIRMED".equals(noti.getStatus())) {
            throw new RuntimeException("Notification already confirmed");
        }

        ServiceBooking booking = bookingRepo.findById(noti.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

     
        noti.setStatus("CONFIRMED");
        noti.setTitle("Lịch đã được xác nhận");
        noti.setMessage(
            "Lịch hẹn cho xe " + booking.getLicensePlate() + " đã được admin xác nhận"
        );

        Notification saved = repo.save(noti);
        NotificationResponse resp = toResponse(saved);

        
        ws.convertAndSend("/topic/user/" + noti.getSenderId(), resp);

        return resp;
    }




    // ADMIN gửi cancel thông báo sang CLIENT
    public NotificationResponse cancelByNotification(String notificationId) {

        Notification noti = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if ("CANCELLED".equals(noti.getStatus())) {
            throw new RuntimeException("Notification already cancelled");
        }

        ServiceBooking booking = bookingRepo.findById(noti.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        noti.setStatus("CANCELLED");
        noti.setTitle("Lịch đã bị hủy");
        noti.setMessage(
            "Lịch hẹn cho xe " + booking.getLicensePlate()
            + " đã bị hủy. Vui lòng liên hệ garage để biết thêm chi tiết"
        );

        Notification saved = repo.save(noti);
        NotificationResponse resp = toResponse(saved);

        ws.convertAndSend("/topic/user/" + noti.getSenderId(), resp);

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
