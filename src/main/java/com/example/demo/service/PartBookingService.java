package com.example.demo.service;

import java.util.Comparator;
import com.example.demo.dto.PartBookingRequest;
import com.example.demo.dto.PartBookingResponse;
import com.example.demo.entity.Part;
import com.example.demo.entity.PartBooking;
import com.example.demo.repository.PartBookingRepository;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Supplier;

import java.time.LocalDateTime;
import java.util.List;

import java.util.UUID;

@Service
public class PartBookingService {

    private final PartRepository partRepository;
    private final SupplierRepository supplierRepository;
    private final PartBookingRepository partBookingRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PartBookingService(PartRepository partRepository,PartBookingRepository partBookingRepository,SupplierRepository supplierRepository,MongoTemplate mongoTemplate) {
        this.partRepository = partRepository;
        this.supplierRepository = supplierRepository;
        this.partBookingRepository = partBookingRepository;
        this.mongoTemplate = mongoTemplate;
    }

        // CREATE BOOKING
    public PartBookingResponse createBooking(PartBookingRequest req) {

        if (req == null)
            throw new IllegalArgumentException("Request is null");

        if (req.getQuantity() == null || req.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity must be > 0");

        Part part = partRepository.findById(req.getPartId())
                .orElseThrow(() -> new RuntimeException("Part not found: " + req.getPartId()));

        Supplier supplier = supplierRepository.findById(req.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found: " + req.getSupplierId()));

        Query q = new Query(Criteria.where("_id").is(req.getPartId())
                .and("stock").gte(req.getQuantity()));

        Update u = new Update()
                .inc("stock", -req.getQuantity())
                .set("updatedAt", LocalDateTime.now());

        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

        Part updatedPart = mongoTemplate.findAndModify(q, u, options, Part.class);

        if (updatedPart == null) {
            throw new RuntimeException("Not enough stock. Available: " + part.getStock());
        }

        PartBooking booking = new PartBooking();
        booking.setBookingCode(generateBookingCode());
        booking.setSupplierId(supplier.getId());
        booking.setSupplierCode(supplier.getSupplierCode());
        booking.setPartId(part.getId());
        booking.setPartName(part.getName());
        booking.setPrice(part.getSalePrice());
        booking.setQuantity(req.getQuantity());
        booking.setRemainingStock(updatedPart.getStock());
        booking.setNote(req.getNote());
        booking.setCustomerName(req.getCustomerName());
        booking.setPhone(req.getPhone());
        booking.setAddress(req.getAddress());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());


        // Save DB
        PartBooking saved = partBookingRepository.save(booking);
        return toResponse(saved);
    }

    // GET BY ID
    public PartBookingResponse getById(String id) {
        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartBooking not found: " + id));
        return toResponse(booking);
    }

    // DELETE BOOKING
    public PartBookingResponse deleteBooking(String id) {

        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartBooking not found: " + id));

        // Restore stock
        Query q = new Query(Criteria.where("_id").is(booking.getPartId()));
        Update u = new Update()
                .inc("stock", booking.getQuantity())
                .set("updatedAt", LocalDateTime.now());

        mongoTemplate.findAndModify(q, u, FindAndModifyOptions.options().returnNew(true), Part.class);

        partBookingRepository.deleteById(id);

        return toResponse(booking);
    }

    // Tạo mã tự động MĐH-001
    private String generateBookingCode() {
    PartBooking last = partBookingRepository.findFirstByOrderByCreatedAtDesc();

    if (last == null || last.getBookingCode() == null) {
        return "MĐH-001";
    }

    String maxCode = last.getBookingCode();

    try {
        // Giả sử định dạng luôn là "MHD-XXX"
        int number = Integer.parseInt(maxCode.replaceAll("\\D", "")); // chỉ lấy số
        return "MĐH-" + String.format("%03d", number + 1);
    } catch (NumberFormatException e) {
        // Nếu parse lỗi, reset
        return "MĐH-001";
    }
}



    private PartBookingResponse toResponse(PartBooking booking) {
        return new PartBookingResponse("OK", booking);
    }
    public List<PartBooking> getAll() {
        return partBookingRepository.findAll();
    }
    // SORT
    public List<PartBookingResponse> sortByCreatedAt(List<PartBooking> items, boolean asc) {

        Comparator<PartBooking> comp =
                Comparator.comparing(
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


