package com.example.demo.service;

import com.example.demo.dto.ServiceBookingRequest;
import com.example.demo.dto.ServiceBookingResponse;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.repository.ServiceRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceBookingService {

    private final ServiceBookingRepository bookingRepo;
    private final ServiceRepository serviceRepo;

    public ServiceBookingService(ServiceBookingRepository bookingRepo, ServiceRepository serviceRepo) {
        this.bookingRepo = bookingRepo;
        this.serviceRepo = serviceRepo;
    }

    private ServiceBookingResponse convert(ServiceBooking booking) {
        ServiceBookingResponse res = new ServiceBookingResponse();
        res.setId(booking.getId());
        res.setCustomerId(booking.getCustomerId());
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
        booking.setCustomerId(req.getCustomerId());
        booking.setServiceId(req.getServiceId());
        booking.setNote(req.getNote());
        booking.setBookingTime(req.getBookingTime());
        booking.setStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepo.save(booking);
        return convert(booking);
    }

    public List<ServiceBookingResponse> getAll() {
        return bookingRepo.findAll().stream().map(this::convert).collect(Collectors.toList());
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
