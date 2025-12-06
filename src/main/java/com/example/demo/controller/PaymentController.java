package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.UpdatePaymentStatusRequest;
import com.example.demo.entity.PaymentHistoryItem;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "API quản lý thanh toán")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Tạo payment cho RepairOrder")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@RequestBody PaymentRequest request) {
        try {
            PaymentResponse resp = paymentService.createPayment(request);
            return ResponseEntity.ok(new ApiResponse<>("Tạo payment thành công", resp));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy payment theo id")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @Parameter(description = "ID của Payment") @PathVariable String id) {
        try {
            PaymentResponse resp = paymentService.getPayment(id);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", resp));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(ex.getMessage(), null));
        }
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách payment")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> listPayments(
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "status", required = false) String status) {
        
        try {
            List<PaymentResponse> result;

            if (orderId != null)
                result = paymentService.findByRepairOrderId(orderId);
            else if (status != null)
                result = paymentService.findByStatus(status);
            else
                result = paymentService.listPayments();

            return ResponseEntity.ok(new ApiResponse<>("Thành công", result));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> updateStatus(
            @PathVariable String id, @RequestBody UpdatePaymentStatusRequest req) {
        try {
            PaymentResponse resp = paymentService.updateStatus(id, req.getStatus());
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật trạng thái thành công", resp));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(ex.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa payment")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        try {
            paymentService.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa payment thành công", null));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(ex.getMessage(), null));
        }
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Lấy lịch sử thanh toán của Payment")
    public ResponseEntity<ApiResponse<List<PaymentHistoryItem>>> getPaymentHistory(@PathVariable String id) {
        try {
            List<PaymentHistoryItem> history = paymentService.getPaymentHistory(id);
            return ResponseEntity.ok(new ApiResponse<>("Lịch sử thanh toán", history));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(ex.getMessage(), null));
        }
    }
}
