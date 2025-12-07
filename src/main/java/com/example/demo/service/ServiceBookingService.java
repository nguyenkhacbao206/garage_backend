package com.example.demo.service;

import com.example.demo.dto.ServiceBookingRequest;
import com.example.demo.dto.ServiceBookingResponse;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.entity.GarageService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceBookingService {

    private final ServiceBookingRepository bookingRepo;
    private final ServiceRepository serviceRepo;
    private final NotificationService notificationService;

    public ServiceBookingService(
            ServiceBookingRepository bookingRepo,
            ServiceRepository serviceRepo,
            NotificationService notificationService
    ) {
        this.bookingRepo = bookingRepo;
        this.serviceRepo = serviceRepo;
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

        res.setServiceIds(booking.getServiceIds());

        // lấy thông tin đầy đủ của từng service
        List<GarageService> services = serviceRepo.findAllById(booking.getServiceIds());
        res.setService(services);

        res.setNote(booking.getNote());
        res.setStatus(booking.getStatus());
        res.setBookingTime(booking.getBookingTime());
        res.setCreatedAt(booking.getCreatedAt());

        return res;
    }

    public ServiceBookingResponse create(ServiceBookingRequest req) {

        // validate tất cả serviceIds
        for (String sid : req.getServiceIds()) {
            if (!serviceRepo.existsById(sid)) {
                throw new ResourceNotFoundException("Dịch vụ không tồn tại: " + sid);
            }
        }

        ServiceBooking booking = new ServiceBooking();

        booking.setCustomerName(req.getCustomerName());
        booking.setCustomerPhone(req.getCustomerPhone());
        booking.setCustomerEmail(req.getCustomerEmail());

        booking.setLicensePlate(req.getLicensePlate());
        booking.setCarBrand(req.getCarBrand());
        booking.setCarModel(req.getCarModel());

        booking.setServiceIds(req.getServiceIds());
        booking.setNote(req.getNote());
        booking.setBookingTime(req.getBookingTime());

        booking.setStatus("PENDING");

        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepo.save(booking);

        // Gửi thông báo
        notificationService.sendBookingToAdmin(
                booking.getId(),
                req.getCustomerEmail()
        );

        return convert(booking);
    }


    public List<ServiceBookingResponse> getAll() {
        return bookingRepo.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }


    public ServiceBookingResponse getOne(String id) {
        ServiceBooking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking"));

        return convert(booking);
    }

    public ServiceBookingResponse update(String id, ServiceBookingRequest req) {

        ServiceBooking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking"));

        // validate serviceIds mới
        for (String sid : req.getServiceIds()) {
            if (!serviceRepo.existsById(sid)) {
                throw new ResourceNotFoundException("Dịch vụ không tồn tại: " + sid);
            }
        }

        booking.setCustomerName(req.getCustomerName());
        booking.setCustomerPhone(req.getCustomerPhone());
        booking.setCustomerEmail(req.getCustomerEmail());

        booking.setLicensePlate(req.getLicensePlate());
        booking.setCarBrand(req.getCarBrand());
        booking.setCarModel(req.getCarModel());

        booking.setServiceIds(req.getServiceIds());
        booking.setNote(req.getNote());
        booking.setBookingTime(req.getBookingTime());

        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepo.save(booking);

        return convert(booking);
    }


    public void delete(String id) {
        if (!bookingRepo.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy booking");
        }
        bookingRepo.deleteById(id);
    }
}
