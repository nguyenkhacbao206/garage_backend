package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentHistoryItem;
import com.example.demo.entity.RepairOrder;
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
    private final RepairOrderService repairOrderService;

    public PaymentService(PaymentRepository paymentRepository,
                          RepairOrderRepository repairOrderRepository,
                          UserRepository userRepository,
                          RepairOrderService repairOrderService) {
        this.paymentRepository = paymentRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.userRepository = userRepository;
        this.repairOrderService = repairOrderService;
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        RepairOrder ro = repairOrderRepository.findById(request.getRepairOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found: " + request.getRepairOrderId()));

        BigDecimal total = ro.calculateEstimatedTotal();

        Payment p = new Payment();
        p.setRepairOrderId(ro.getId());
        p.setAmount(total);
        p.setMethod(request.getMethod() == null ? "CASH" : request.getMethod());
        p.setStatus("SUCCESS");
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());

        // Lưu lịch sử lần tạo
        p.addHistory(new PaymentHistoryItem(p.getStatus(), p.getMethod(), LocalDateTime.now()));

        Payment saved = paymentRepository.save(p);

        // Cập nhật RepairOrder
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

        // Lưu lịch sử khi update
        p.addHistory(new PaymentHistoryItem(status, p.getMethod(), LocalDateTime.now()));

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

    // Lấy lịch sử thanh toán
    public List<PaymentHistoryItem> getPaymentHistory(String id) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        return p.getHistory();
    }

    private PaymentResponse toResponse(Payment p) {
        RepairOrder ro = repairOrderRepository.findById(p.getRepairOrderId())
                .orElse(null);

        var roResponse = ro != null ? repairOrderService.convertToResponse(ro) : null;

        PaymentResponse resp = new PaymentResponse();
        resp.setId(p.getId());
        resp.setRepairOrderId(p.getRepairOrderId());
        resp.setRepairOrder(roResponse);
        resp.setAmount(p.getAmount());
        resp.setMethod(p.getMethod());
        resp.setStatus(p.getStatus());
        resp.setCreatedAt(p.getCreatedAt());
        resp.setUpdatedAt(p.getUpdatedAt());

        return resp;
    }
}
