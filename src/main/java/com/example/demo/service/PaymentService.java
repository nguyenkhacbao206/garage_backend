package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.entity.Part;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentHistoryItem;
import com.example.demo.entity.RepairOrder;
import com.example.demo.entity.RepairOrderItem;
import com.example.demo.exception.ResourceNotFoundException;

import com.example.demo.repository.PartRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.RepairOrderRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final UserRepository userRepository;
    private final RepairOrderService repairOrderService;
    private final PartRepository partRepository;  

    public PaymentService(PaymentRepository paymentRepository,
                          RepairOrderRepository repairOrderRepository,
                          UserRepository userRepository,
                          RepairOrderService repairOrderService,
                          PartRepository partRepository) {        

        this.paymentRepository = paymentRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.userRepository = userRepository;
        this.repairOrderService = repairOrderService;
        this.partRepository = partRepository;
    }

    // Tạo payment
    public PaymentResponse createPayment(PaymentRequest request) {

        RepairOrder ro = repairOrderRepository.findById(request.getRepairOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "RepairOrder not found: " + request.getRepairOrderId()
                ));

        BigDecimal total = ro.calculateEstimatedTotal();

        Payment p = new Payment();
        p.setRepairOrderId(ro.getId());
        p.setAmount(total);
        p.setMethod(request.getMethod() == null ? "CASH" : request.getMethod());
        p.setStatus("SUCCESS");

        LocalDateTime now = LocalDateTime.now();
        p.setCreatedAt(now);
        p.setUpdatedAt(now);
        p.addHistory(new PaymentHistoryItem(p.getStatus(), p.getMethod(), now));

        Payment saved = paymentRepository.save(p);

    // check apart from stock part when payment
        if (ro.getParts() != null) {
            for (RepairOrderItem item : ro.getParts()) {

                Part part = partRepository.findById(item.getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Part: " + item.getId()));

                int newStock = part.getStock() - item.getQuantity();

                if (newStock < 0) {
                    throw new RuntimeException("Part " + part.getName() + " không đủ số lượng tồn kho!");
                }

                part.setStock(newStock);
                partRepository.save(part);
            }
        }

        // update repair order
        ro.setStatus("PAID");
        ro.setDateReturned(now);
        repairOrderRepository.save(ro);

        return toResponse(saved);
    }

   
    // Lấy payment theo ID
    public PaymentResponse getPayment(String id) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        return toResponse(p);
    }

   
    // List all
    public List<PaymentResponse> listPayments() {
        return paymentRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<PaymentResponse> findByRepairOrderId(String repairOrderId) {
        return paymentRepository.findByRepairOrderId(repairOrderId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<PaymentResponse> findByStatus(String status) {
        return paymentRepository.findByStatus(status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

   
    // Update Status
    public PaymentResponse updateStatus(String id, String status) {

        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));

        LocalDateTime now = LocalDateTime.now();

        p.setStatus(status);
        p.setUpdatedAt(now);
        p.addHistory(new PaymentHistoryItem(status, p.getMethod(), now));

        Payment saved = paymentRepository.save(p);

      
        // Khi chuyển sang SUCCESS thì trừ kho
        if ("SUCCESS".equalsIgnoreCase(status)) {

            RepairOrder ro = repairOrderRepository.findById(p.getRepairOrderId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Repair Order"));

            if (ro.getParts() != null) {
                for (RepairOrderItem item : ro.getParts()) {

                    Part part = partRepository.findById(item.getId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy Part: " + item.getId()));

                    int newStock = part.getStock() - item.getQuantity();

                    if (newStock < 0) {
                        throw new RuntimeException("Part " + part.getName() + " không đủ số lượng tồn kho!");
                    }

                    part.setStock(newStock);
                    partRepository.save(part);
                }
            }

            ro.setStatus("PAID");
            ro.setDateReturned(now);
            repairOrderRepository.save(ro);
        }

        return toResponse(saved);
    }

    public void deleteById(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found: " + id);
        }
        paymentRepository.deleteById(id);
    }

    public List<PaymentHistoryItem> getPaymentHistory(String id) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        return p.getHistory();
    }

   
    // Convert Entity -> Response 
    private PaymentResponse toResponse(Payment p) {

        RepairOrder ro = repairOrderRepository.findById(p.getRepairOrderId())
                .orElse(null);

        return new PaymentResponse(
                p.getId(),
                p.getRepairOrderId(),
                ro != null ? repairOrderService.convertToResponse(ro) : null,
                p.getAmount(),
                p.getMethod(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.getHistory()
        );
    }
}
