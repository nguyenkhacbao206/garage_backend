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
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PartBookingRepository;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.SupplierRepository;

@Service
public class PartBookingService {

    private final PartRepository partRepository;
    private final SupplierRepository supplierRepository;
    private final PartBookingRepository partBookingRepository;
    private final MongoTemplate mongoTemplate;

    // ĐÃ XÓA NotificationService

    @Autowired
    public PartBookingService(
            PartRepository partRepository,
            PartBookingRepository partBookingRepository,
            SupplierRepository supplierRepository,
            MongoTemplate mongoTemplate
            // ĐÃ XÓA tham số NotificationService trong constructor
    ) {
        this.partRepository = partRepository;
        this.supplierRepository = supplierRepository;
        this.partBookingRepository = partBookingRepository;
        this.mongoTemplate = mongoTemplate;
    }

    // CREATE BOOKING
    public PartBookingResponse createBooking(PartBookingRequest req) {

        Part part = partRepository.findById(req.getPartId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy part: " + req.getPartId()));

        Supplier supplier = supplierRepository.findById(req.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy supplier: " + req.getSupplierId()));

        if (req.getPhone() == null || !req.getPhone().matches("0\\d{9}")) {
            throw new RuntimeException("Số điện thoại phải có 10 số và bắt đầu bằng 0!");
        }

        Part updatedPart = part;
        // Trừ kho ngay nếu tạo active (thường client tạo là inactive/pending)
        if (Boolean.TRUE.equals(req.isActive())) {
            Query q = new Query(
                    Criteria.where("_id").is(part.getId())
                            .and("stock").gte(req.getQuantity())
            );
            Update u = new Update()
                    .inc("stock", -req.getQuantity())
                    .set("updatedAt", LocalDateTime.now());

            updatedPart = mongoTemplate.findAndModify(
                    q, u,
                    FindAndModifyOptions.options().returnNew(true),
                    Part.class
            );

            if (updatedPart == null) {
                throw new RuntimeException("Không đủ tồn kho");
            }
        }

        PartBooking booking = new PartBooking();
        booking.setBookingCode(generateBookingCode());
        booking.setCustomerId(req.getCustomerId());
        booking.setCustomerEmail(req.getCustomerEmail());
        booking.setCustomerName(req.getCustomerName());
        booking.setPhone(req.getPhone());
        booking.setAddress(req.getAddress());
        booking.setNote(req.getNote());
        booking.setPartId(part.getId());
        booking.setPartName(part.getName());
        booking.setPrice(part.getPrice());
        booking.setQuantity(req.getQuantity());
        booking.setRemainingStock(updatedPart.getStock());
        booking.setSupplierId(supplier.getId());
        booking.setSupplierCode(supplier.getSupplierCode());
        booking.setIsActive(req.isActive());
        
        // Mặc định trạng thái
        booking.setStatus(Boolean.TRUE.equals(req.isActive()) ? "CONFIRMED" : "PENDING");
        
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        PartBooking saved = partBookingRepository.save(booking);

        // Không gửi thông báo nữa

        return toResponse(saved);
    }

    // GET BY ID
    public PartBookingResponse getById(String id) {
        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking: " + id));
        return toResponse(booking);
    }

    // CONFIRM BOOKING (Duyệt đơn)
    public PartBookingResponse confirmBooking(String id) {
        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking: " + id));

        if (Boolean.TRUE.equals(booking.getIsActive())) {
            throw new RuntimeException("Booking đã được xác nhận");
        }

        // Trừ tồn kho
        Query q = new Query(
                Criteria.where("_id").is(booking.getPartId())
                        .and("stock").gte(booking.getQuantity())
        );
        Update u = new Update()
                .inc("stock", -booking.getQuantity())
                .set("updatedAt", LocalDateTime.now());

        Part updated = mongoTemplate.findAndModify(
                q, u,
                FindAndModifyOptions.options().returnNew(true),
                Part.class
        );

        if (updated == null) {
            throw new RuntimeException("Không đủ tồn kho để xác nhận");
        }

        booking.setIsActive(true);
        booking.setStatus("CONFIRMED");
        booking.setRemainingStock(updated.getStock());
        booking.setUpdatedAt(LocalDateTime.now());

        return toResponse(partBookingRepository.save(booking));
    }

    // --- HÀM HỦY ĐƠN (CANCEL) ---
    // Logic: Chỉ hủy được khi chưa duyệt. Không cần gửi thông báo qua Service này.
    public PartBookingResponse cancelBooking(String id) {
        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking: " + id));

        // 1. Chặn nếu đơn đã duyệt
        if (Boolean.TRUE.equals(booking.getIsActive()) || "CONFIRMED".equals(booking.getStatus())) {
            throw new RuntimeException("Đơn hàng ĐÃ DUYỆT, không thể hủy!");
        }

        // 2. Nếu đã hủy rồi thì trả về luôn
        if ("CANCELLED".equals(booking.getStatus())) {
            return toResponse(booking);
        }

        // 3. Cập nhật trạng thái
        booking.setStatus("CANCELLED");
        booking.setIsActive(false);
        booking.setUpdatedAt(LocalDateTime.now());

        PartBooking saved = partBookingRepository.save(booking);
        
        return toResponse(saved);
    }

    // UPDATE BOOKING
    public PartBookingResponse update(String id, PartBookingRequest req) {
        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking"));

        Part newPart = partRepository.findById(req.getPartId())
                .orElseThrow(() -> new ResourceNotFoundException("Part không tồn tại"));

        Supplier supplier = supplierRepository.findById(req.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier không tồn tại"));

        // HOÀN KHO CŨ nếu active
        if (Boolean.TRUE.equals(booking.getIsActive())) {
            Query q = new Query(Criteria.where("_id").is(booking.getPartId()));
            Update u = new Update().inc("stock", booking.getQuantity());
            mongoTemplate.updateFirst(q, u, Part.class);
        }

        // TRỪ KHO MỚI nếu active
        if (Boolean.TRUE.equals(req.isActive())) {
            Query q = new Query(
                    Criteria.where("_id").is(req.getPartId())
                            .and("stock").gte(req.getQuantity())
            );
            Update u = new Update().inc("stock", -req.getQuantity());
            Part updated = mongoTemplate.findAndModify(
                    q, u,
                    FindAndModifyOptions.options().returnNew(true),
                    Part.class
            );
            if (updated == null) {
                throw new RuntimeException("Không đủ tồn kho để cập nhật");
            }
            booking.setRemainingStock(updated.getStock());
        }

        booking.setCustomerId(req.getCustomerId());
        booking.setCustomerEmail(req.getCustomerEmail());
        booking.setCustomerName(req.getCustomerName());
        booking.setPhone(req.getPhone());
        booking.setAddress(req.getAddress());
        booking.setNote(req.getNote());
        booking.setPartId(newPart.getId());
        booking.setPartName(newPart.getName());
        booking.setPrice(newPart.getPrice());
        booking.setSupplierId(supplier.getId());
        booking.setSupplierCode(supplier.getSupplierCode());
        booking.setQuantity(req.getQuantity());
        booking.setIsActive(req.isActive());
        booking.setUpdatedAt(LocalDateTime.now());

        return toResponse(partBookingRepository.save(booking));
    }

    // DELETE BOOKING (Xóa cứng)
    public PartBookingResponse deleteBooking(String id) {
        PartBooking booking = partBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking: " + id));
        partBookingRepository.deleteById(id);
        return toResponse(booking);
    }

    // DELETE ALL
    public List<PartBookingResponse> deleteAllBookingsWithoutRestoringStock() {
        List<PartBooking> list = partBookingRepository.findAll();
        partBookingRepository.deleteAll();
        return list.stream().map(this::toResponse).toList();
    }

    // GET ALL
    public List<PartBookingResponse> getAllBookings() {
        return partBookingRepository.findAll()
                .stream().map(this::toResponse).toList();
    }
    
    public List<PartBooking> getAll() {
        return partBookingRepository.findAll();
    }

    // SORT
    public List<PartBookingResponse> sortByCreatedAt(List<PartBooking> items, boolean asc) {
        Comparator<PartBooking> cmp =
                Comparator.comparing(PartBooking::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
        if (!asc) cmp = cmp.reversed();
        items.sort(cmp);
        return items.stream().map(this::toResponse).toList();
    }

    // SEARCH
    public List<PartBookingResponse> searchBookings(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBookings();
        }
        String key = keyword.toLowerCase();
        List<PartBooking> results = partBookingRepository.findAll()
                .stream()
                .filter(b ->
                        (b.getCustomerName() != null && b.getCustomerName().toLowerCase().contains(key)) ||
                        (b.getPhone() != null && b.getPhone().contains(keyword)) ||
                        (b.getBookingCode() != null && b.getBookingCode().toLowerCase().contains(key))
                )
                .toList();
        return results.stream().map(this::toResponse).toList();
    }

    // HELPERS
    private String generateBookingCode() {
        PartBooking last = partBookingRepository.findFirstByOrderByCreatedAtDesc();
        if (last == null || last.getBookingCode() == null) return "MĐH-001";
        String code = last.getBookingCode().replaceAll("\\D", "");
        try {
            int number = Integer.parseInt(code);
            return "MĐH-" + String.format("%03d", number + 1);
        } catch (Exception e) {
            return "MĐH-001";
        }
    }

    private PartBookingResponse toResponse(PartBooking b) {
        return new PartBookingResponse("OK", b);
    }
}