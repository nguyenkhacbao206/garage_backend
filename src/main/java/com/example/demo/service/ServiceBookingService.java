package com.example.demo.service;

import com.example.demo.dto.ServiceBookingRequest;
import com.example.demo.dto.ServiceBookingResponse;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceBookingService {

    private final ServiceBookingRepository bookingRepo;
    private final ServiceRepository serviceRepo;
    private final NotificationService notificationService;

    private final SimpMessagingTemplate messagingTemplate;

    public ServiceBookingService(ServiceBookingRepository bookingRepo,
                                 ServiceRepository serviceRepo,
                                 SimpMessagingTemplate messagingTemplate,
                                 NotificationService notificationService) {
        this.bookingRepo = bookingRepo;
        this.serviceRepo = serviceRepo;
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

   
    private ServiceBookingResponse convert(ServiceBooking booking) {
        ServiceBookingResponse res = new ServiceBookingResponse();

        res.setId(booking.getId());
        res.setCustomerName(booking.getCustomerName());
        res.setCustomerPhone(booking.getCustomerPhone());
        res.setCustomerEmail(booking.getCustomerEmail());

        res.setLicensePlate(booking.getLicensePlate());
        res.setCarBrand(booking.getCarBrand());
        res.setCarModel(booking.getCarModel());

        res.setServiceId(booking.getServiceId());
        res.setNote(booking.getNote());
        res.setStatus(booking.getStatus());

        res.setBookingTime(booking.getBookingTime());
        res.setCreatedAt(booking.getCreatedAt());

        return res;
    }

    
    public ServiceBookingResponse create(ServiceBookingRequest req) {

        if (!serviceRepo.existsById(req.getServiceId())) {
            throw new RuntimeException("Dịch vụ không tồn tại!");
        }

        ServiceBooking booking = new ServiceBooking();

        booking.setCustomerName(req.getCustomerName());
        booking.setCustomerPhone(req.getCustomerPhone());
        booking.setCustomerEmail(req.getCustomerEmail());

        booking.setLicensePlate(req.getLicensePlate());
        booking.setCarBrand(req.getCarBrand());
        booking.setCarModel(req.getCarModel());

        booking.setServiceId(req.getServiceId());
        booking.setNote(req.getNote());
        booking.setBookingTime(req.getBookingTime());

        booking.setStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepo.save(booking);

        // Gửi thông báo đến Admin 
        notificationService.sendBookingToAdmin(
                booking.getId(),
                req.getCustomerEmail()   // client gửi đi bằng email
        );

        return convert(booking);
    }


    public List<ServiceBookingResponse> getAll() {
        return bookingRepo.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public Optional<ServiceBookingResponse> getById(String id) {
        return bookingRepo.findById(id).map(this::convert);
    }

   
    public ServiceBookingResponse updateStatus(String id, String status) {
        ServiceBooking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt dịch vụ!"));

        booking.setStatus(status);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepo.save(booking);

        // Admin xác nhận gửi thông báo về Client 
        if (status.equals("CONFIRMED")) {
            notificationService.sendConfirmToClient(
                    booking.getId(),
                    booking.getCustomerEmail(),   // FE sau này dùng userId nếu có
                    "ADMIN"
            );
        }

        return convert(booking);
    }

    public boolean delete(String id) {
        if (!bookingRepo.existsById(id)) {
            throw new RuntimeException("Không tồn tại đặt dịch vụ!");
        }
        bookingRepo.deleteById(id);
        return true;
    }
}
