package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.UpdatePaymentStatusRequest;
import com.example.demo.entity.PaymentHistoryItem;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "API quản lý thanh toán Repair Order")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ================= CREATE PAYMENT =================
    @PostMapping
    @Operation(summary = "Tạo mới Payment cho RepairOrder",
               description = "Tự động tính tổng tiền từ Repair Order và tạo Payment kèm lịch sử ban đầu")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tạo payment thành công",
            content = @Content(schema = @Schema(implementation = PaymentResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Lỗi: ResourceNotFoundException"
        )
    })
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @RequestBody PaymentRequest request
    ) {
        try {
            PaymentResponse response = paymentService.createPayment(request);
            return ResponseEntity.ok(new ApiResponse<>("Tạo payment thành công", response));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ex.getMessage(), null));

        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    // ================= GET PAYMENT BY ID =================
    @GetMapping("/{id}")
    @Operation(summary = "Lấy Payment theo ID",
               description = "Bao gồm thông tin Repair Order + lịch sử thanh toán")
    @ApiResponses({ // Thêm ApiResponses để Swagger hiển thị mẫu rõ ràng hơn
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Thành công",
            content = @Content(schema = @Schema(implementation = PaymentResponse.class)) // Trả về PaymentResponse
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Lỗi: ResourceNotFoundException"
        )
    })
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable String id
    ) {
        try {
            PaymentResponse response = paymentService.getPayment(id);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", response));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ex.getMessage(), null));

        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    // ================= LIST PAYMENTS =================
    @GetMapping
    @Operation(summary = "Lấy danh sách Payment",
               description = "Có thể lọc theo orderId hoặc status")
    @ApiResponses({ // Thêm ApiResponses để Swagger hiển thị mẫu rõ ràng hơn
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Thành công",
            // Dùng PaymentResponse.class. Swagger sẽ tự động hiểu nó là List<PaymentResponse> 
            // vì phương thức trả về ResponseEntity<ApiResponse<List<PaymentResponse>>>
            content = @Content(schema = @Schema(implementation = PaymentResponse.class)) 
        )
    })
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> listPayments(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String status
    ) {
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

    // ================= UPDATE STATUS =================
    @PutMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái Payment",
               description = "Tự động thêm lịch sử vào PaymentHistory và cập nhật RepairOrder nếu SUCCESS")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Cập nhật trạng thái thành công",
            content = @Content(schema = @Schema(implementation = PaymentResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Lỗi: ResourceNotFoundException"
        )
    })
    public ResponseEntity<ApiResponse<PaymentResponse>> updateStatus(
            @PathVariable String id,
            @RequestBody UpdatePaymentStatusRequest request
    ) {
        try {
            PaymentResponse updated = paymentService.updateStatus(id, request.getStatus());
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật trạng thái thành công", updated));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ex.getMessage(), null));

        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    // ================= DELETE PAYMENT =================
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa Payment theo ID")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Xóa payment thành công"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Lỗi: ResourceNotFoundException"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable String id) {
        try {
            paymentService.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa payment thành công", null));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ex.getMessage(), null));

        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    // ================= PAYMENT HISTORY =================
    @GetMapping("/{id}/history")
    @Operation(summary = "Lấy lịch sử thanh toán của Payment")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lịch sử thanh toán",
            // Quan trọng: Phải sử dụng PaymentHistoryItem.class ở đây
            content = @Content(schema = @Schema(implementation = PaymentHistoryItem.class)) 
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Lỗi: ResourceNotFoundException"
        )
    })
    public ResponseEntity<ApiResponse<List<PaymentHistoryItem>>> getHistory(
            @PathVariable String id
    ) {
        try {
            List<PaymentHistoryItem> history = paymentService.getPaymentHistory(id);
            return ResponseEntity.ok(new ApiResponse<>("Lịch sử thanh toán", history));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ex.getMessage(), null));

        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }
}