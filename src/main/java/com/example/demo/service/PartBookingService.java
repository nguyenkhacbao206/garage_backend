package com.example.demo.service;

import com.example.demo.dto.PartBookingRequest;
import com.example.demo.dto.PartBookingResponse;
import com.example.demo.entity.Part;
import com.example.demo.entity.PartBooking;
import com.example.demo.repository.PartBookingRepository;
import com.example.demo.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PartBookingService {

    private final PartRepository partRepository;
    private final PartBookingRepository partBookingRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PartBookingService(PartRepository partRepository,PartBookingRepository partBookingRepository,MongoTemplate mongoTemplate) {
        this.partRepository = partRepository;
        this.partBookingRepository = partBookingRepository;
        this.mongoTemplate = mongoTemplate;
    }

    // CREATE BOOKING
    public PartBookingResponse createBooking(PartBookingRequest req) {

        if (req == null) 
            throw new IllegalArgumentException("Request is null");

        if (req.getQuantity() == null || req.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity must be > 0");

        // Atomic decrement stock
        Query q = new Query(Criteria.where("_id").is(req.getPartId())
                .and("stock").gte(req.getQuantity()));

        Update u = new Update()
                .inc("stock", -req.getQuantity())
                .set("updatedAt", LocalDateTime.now());

        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

        Part updatedPart = mongoTemplate.findAndModify(q, u, options, Part.class);

        if (updatedPart == null) {
            Optional<Part> check = partRepository.findById(req.getPartId());
            if (!check.isPresent()) {
                throw new RuntimeException("Part not found: " + req.getPartId());
            } else {
                throw new RuntimeException("Not enough stock. Available: " + check.get().getStock());
            }
        }

        // Create booking snapshot
        PartBooking booking = new PartBooking();
        booking.setBookingCode(generateBookingCode());
        booking.setSupplierId(req.getSupplierId());
        booking.setSupplierCode(req.getSupplierCode());
        booking.setPartId(updatedPart.getId());  // ID part
        booking.setPartName(updatedPart.getName());
        booking.setQuantity(req.getQuantity());
        booking.setRemainingStock(updatedPart.getStock());
        booking.setNote(req.getNote());
        booking.setCustomerName(req.getCustomerName());
        booking.setPhone(req.getPhone());
        booking.setAddress(req.getAddress());
        booking.setCreatedAt(LocalDateTime.now());

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

    //
    private String generateBookingCode() {
        String date = LocalDateTime.now().toLocalDate().toString().replace("-", "");
        String rnd = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "PB-" + date + "-" + rnd;
    }

    private PartBookingResponse toResponse(PartBooking booking) {
        return new PartBookingResponse("OK", booking);
    }
}
