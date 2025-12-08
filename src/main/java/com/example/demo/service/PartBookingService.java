package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PartBookingRequest;
import com.example.demo.dto.PartBookingResponse;
import com.example.demo.entity.Part;
import com.example.demo.entity.PartBooking;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.PartBookingRepository;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class PartBookingService {

    private final PartRepository partRepository;
    private final SupplierRepository supplierRepository;
    private final PartBookingRepository partBookingRepository;
    private final NotificationService notificationService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PartBookingService(PartRepository partRepository, PartBookingRepository partBookingRepository, SupplierRepository supplierRepository, NotificationService notificationService, MongoTemplate mongoTemplate) {
        this.partRepository = partRepository;
        this.supplierRepository = supplierRepository;
        this.partBookingRepository = partBookingRepository;
        this.mongoTemplate = mongoTemplate;
        this.notificationService = notificationService;
    }

    public PartBookingResponse createBooking(PartBookingRequest req) {

        Part part = partRepository.findById(req.getPartId())
            .orElseThrow(() -> new RuntimeException("Part not found: " + req.getPartId()));

        Supplier supplier = supplierRepository.findById(req.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found: " + req.getSupplierId()));

        Part updatedPart = part;

        if (req.isActive()) {
            Query q = new Query(Criteria.where("_id").is(req.getPartId())
                    .and("stock").gte(req.getQuantity()));

            Update u = new Update()
                    .inc("stock", -req.getQuantity())
                    .set("updatedAt", LocalDateTime.now());

            updatedPart = mongoTemplate.findAndModify(q, u, FindAndModifyOptions.options().returnNew(true), Part.class);

            if (updatedPart == null) {
                throw new RuntimeException("Not enough stock!");
            }
        }
        String phone = req.getPhone();
        if (phone == null || !phone.matches("0\\d{9}")) {
            throw new RuntimeException("Số điện thoại phải gồm đúng 10 chữ số và số 0  phải ở đầu!");
        }
        if (supplierRepository.existsByPhone(req.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại!");
        }
        
        PartBooking booking = new PartBooking();
        booking.setBookingCode(generateBookingCode());
        booking.setSupplierId(supplier.getId());
        booking.setCustomerEmail(req.getCustomerEmail());
        booking.setSupplierCode(supplier.getSupplierCode());
        booking.setPartId(part.getId());
        booking.setPartName(part.getName());
        booking.setPrice(part.getPrice());
        booking.setQuantity(req.getQuantity());
        booking.setRemainingStock(updatedPart.getStock());
        booking.setNote(req.getNote());
        booking.setCustomerName(req.getCustomerName());
        booking.setIsActive(req.isActive());
        // booking.setPhone(req.getPhone());
        booking.setAddress(req.getAddress());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        PartBooking saved = partBookingRepository.save(booking);

        // Gửi thông báo
        notificationService.sendBookingToAdmin(
                booking.getId(),
                req.getCustomerEmail()
        );


        return toResponse(saved);
    }

    public PartBookingResponse confirmBooking(String id) {
        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartBooking not found: " + id));

        if (Boolean.TRUE.equals(booking.getIsActive())) {
            throw new RuntimeException("Booking is already confirmed");
        }

        Query q = new Query(Criteria.where("_id").is(booking.getPartId())
                .and("stock").gte(booking.getQuantity()));

        Update u = new Update()
                .inc("stock", -booking.getQuantity())
                .set("updatedAt", LocalDateTime.now());

        Part updatedPart = mongoTemplate.findAndModify(q, u, FindAndModifyOptions.options().returnNew(true), Part.class);

        if (updatedPart == null) {
            throw new RuntimeException("Not enough stock to confirm");
        }

        booking.setIsActive(true);
        booking.setRemainingStock(updatedPart.getStock());
        booking.setUpdatedAt(LocalDateTime.now());

        PartBooking saved = partBookingRepository.save(booking);
        return toResponse(saved);
    }

    public PartBookingResponse getById(String id) {
        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartBooking not found: " + id));
        return toResponse(booking);
    }

    public List<PartBookingResponse> getAllBookings() {
        return partBookingRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public PartBookingResponse deleteBooking(String id) {

        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartBooking not found: " + id));

        if (Boolean.TRUE.equals(booking.getIsActive())) {
            Query q = new Query(Criteria.where("_id").is(booking.getPartId()));
            Update u = new Update()
                    .inc("stock", booking.getQuantity())
                    .set("updatedAt", LocalDateTime.now());

            mongoTemplate.findAndModify(q, u, FindAndModifyOptions.options().returnNew(true), Part.class);
        }

        partBookingRepository.deleteById(id);

        return toResponse(booking);
    }

    private String generateBookingCode() {
        PartBooking last = partBookingRepository.findFirstByOrderByCreatedAtDesc();

        if (last == null || last.getBookingCode() == null) {
            return "MĐH-001";
        }

        String maxCode = last.getBookingCode();

        try {
            int number = Integer.parseInt(maxCode.replaceAll("\\D", "")); 
            return "MĐH-" + String.format("%03d", number + 1);
        } catch (NumberFormatException e) {
            return "MĐH-001";
        }
    }

    private PartBookingResponse toResponse(PartBooking booking) {
        return new PartBookingResponse("OK", booking);
    }

    public List<PartBooking> getAll() {
        return partBookingRepository.findAll();
    }
    public List<PartBookingResponse> deleteAllBookingsWithoutRestoringStock() {
    List<PartBooking> bookings = partBookingRepository.findAll();

    // Xóa tất cả booking
    partBookingRepository.deleteAll();

    // Trả về danh sách booking đã xóa (nếu cần)
    return bookings.stream()
            .map(this::toResponse)
            .toList();
    }


    public List<PartBookingResponse> sortByCreatedAt(List<PartBooking> items, boolean asc) {

        Comparator<PartBooking> comp = Comparator.comparing(
                        PartBooking::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );

        if (!asc) comp = comp.reversed();

        items.sort(comp);

        return items.stream()
                .map(this::toResponse)
                .toList();
    }
}
