package com.example.demo.controller;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.UpdatePaymentStatusRequest;
import com.example.demo.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "API quản lý thanh toán")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @Operation(summary = "Tạo payment cho RepairOrder")
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse resp = paymentService.createPayment(request);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Lấy payment theo id")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @Operation(summary = "Lấy danh sách payment (có thể filter theo orderId hoặc status bằng query params)")
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> listPayments(
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "status", required = false) String status) {

        if (orderId != null) return ResponseEntity.ok(paymentService.findByRepairOrderId(orderId));
        if (status != null) return ResponseEntity.ok(paymentService.findByStatus(status));
        return ResponseEntity.ok(paymentService.listPayments());
    }

    @Operation(summary = "Cập nhật trạng thái payment")
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updateStatus(@PathVariable String id, @RequestBody UpdatePaymentStatusRequest req) {
        return ResponseEntity.ok(paymentService.updateStatus(id, req.getStatus()));
    }

    @Operation(summary = "Xóa payment")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
