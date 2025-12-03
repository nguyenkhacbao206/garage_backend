package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.entity.Payment;
import com.example.demo.entity.RepairOrder;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
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

    public PaymentService(PaymentRepository paymentRepository,
                          RepairOrderRepository repairOrderRepository,
                          UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.userRepository = userRepository;
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        RepairOrder ro = repairOrderRepository.findById(request.getRepairOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found: " + request.getRepairOrderId()));

        // User cashier = null;
        // if (request.getCashierId() != null) {
        //     cashier = userRepository.findById(request.getCashierId())
        //             .orElseThrow(() -> new ResourceNotFoundException("Cashier not found: " + request.getCashierId()));
        // }

        BigDecimal total = ro.calculateEstimatedTotal();

        Payment p = new Payment();
        p.setRepairOrderId(ro.getId());
        // p.setCashierId(cashier != null ? cashier.getId() : null);
        p.setAmount(total);
        p.setMethod(request.getMethod() == null ? "CASH" : request.getMethod());
        p.setStatus("SUCCESS");
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(p);

        ro.setStatus("PAID");
        ro.setDateReturned(LocalDateTime.now());
        repairOrderRepository.save(ro);

        return toResponse(saved);
    }

    public PaymentResponse getPayment(String id) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        return toResponse(p);
    }

    public List<PaymentResponse> listPayments() {
        return paymentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<PaymentResponse> findByRepairOrderId(String repairOrderId) {
        return paymentRepository.findByRepairOrderId(repairOrderId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<PaymentResponse> findByStatus(String status) {
        return paymentRepository.findByStatus(status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PaymentResponse updateStatus(String id, String status) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));

        p.setStatus(status);
        p.setUpdatedAt(LocalDateTime.now());
        Payment saved = paymentRepository.save(p);

        if ("SUCCESS".equalsIgnoreCase(status)) {
            repairOrderRepository.findById(p.getRepairOrderId()).ifPresent(ro -> {
                ro.setStatus("PAID");
                ro.setDateReturned(LocalDateTime.now());
                repairOrderRepository.save(ro);
            });
        }

        return toResponse(saved);
    }

    public void deleteById(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getRepairOrderId(),
                // p.getCashierId(),
                p.getAmount(),
                p.getMethod(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
