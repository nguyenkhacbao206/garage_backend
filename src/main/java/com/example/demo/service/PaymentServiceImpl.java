package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.entity.Payment;
import com.example.demo.entity.RepairOrder;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.RepairOrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final UserRepository userRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              RepairOrderRepository repairOrderRepository,
                              UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        // 1. Lấy repair order
        RepairOrder ro = repairOrderRepository.findById(request.getRepairOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found: " + request.getRepairOrderId()));

        // 2. Lấy cashier (nếu cần)
        User cashier = null;
        if (request.getCashierId() != null) {
            cashier = userRepository.findById(request.getCashierId())
                    .orElseThrow(() -> new ResourceNotFoundException("User (cashier) not found: " + request.getCashierId()));
        }

        // 3. Tính tổng dựa trên repairOrder.calculateTotal()
        BigDecimal total = ro.calculateTotal();

        // 4. Tạo Payment
        Payment p = new Payment();
        p.setRepairOrderId(ro.getId());
        p.setCashierId(cashier != null ? cashier.getId() : null);
        p.setAmount(total);
        p.setMethod(request.getMethod() == null ? "CASH" : request.getMethod());
        p.setStatus("SUCCESS"); // giả định thanh toán thành công sau khi thu tiền
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(p);

        // 5. Cập nhật trạng thái repair order
        ro.setStatus("PAID");
        repairOrderRepository.save(ro);

        return toResponse(saved);
    }

    @Override
    public PaymentResponse getPayment(String id) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        return toResponse(p);
    }

    @Override
    public List<PaymentResponse> listPayments() {
        return paymentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> findByRepairOrderId(String repairOrderId) {
        return paymentRepository.findByRepairOrderId(repairOrderId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> findByStatus(String status) {
        return paymentRepository.findByStatus(status).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public PaymentResponse updateStatus(String id, String status) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        p.setStatus(status);
        p.setUpdatedAt(LocalDateTime.now());
        Payment saved = paymentRepository.save(p);

        // nếu status = SUCCESS thì update repair order trạng thái PAID
        if ("SUCCESS".equalsIgnoreCase(status) && p.getRepairOrderId() != null) {
            repairOrderRepository.findById(p.getRepairOrderId()).ifPresent(ro -> {
                ro.setStatus("PAID");
                repairOrderRepository.save(ro);
            });
        }
        return toResponse(saved);
    }

    @Override
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
                p.getCashierId(),
                p.getAmount(),
                p.getMethod(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
