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

    
    @GetMapping("/{id}")
    @Operation(summary = "Lấy Payment theo ID",
               description = "Bao gồm thông tin Repair Order + lịch sử thanh toán")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Thành công",
            content = @Content(schema = @Schema(implementation = PaymentResponse.class))
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

   
    @GetMapping
    @Operation(
            summary = "Lấy danh sách Payment",
            description = "Lọc theo orderId, status và sắp xếp theo thời gian tạo"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Thành công",
                content = @Content(
                        schema = @Schema(implementation = PaymentResponse.class)
                )
        )
    })
        public ResponseEntity<ApiResponse<List<PaymentResponse>>> listPayments(

            @Parameter(description = "ID của Repair Order")
            @RequestParam(required = false)
            String orderId,

            @Parameter(description = "Trạng thái Payment (SUCCESS, PENDING, FAILED)")
            @RequestParam(required = false)
            String status,

            @Parameter(
                    description = "Hướng sắp xếp theo thời gian tạo (createdAt)",
                    example = "asc | desc"
            )
            @RequestParam(defaultValue = "desc")
            String sort
    ) {
        try {
            List<PaymentResponse> result;

            if (orderId != null)
                result = paymentService.findByRepairOrderId(orderId, sort);
            else if (status != null)
                result = paymentService.findByStatus(status, sort);
            else
                result = paymentService.listPayments(sort);

            return ResponseEntity.ok(new ApiResponse<>("Thành công", result));

        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @GetMapping("/sort")
    @Operation(
            summary = "Sort Payment theo createdAt",
            description = "API sort RIÊNG, không ảnh hưởng API list hiện tại"
    )
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> sortPayments(
            @RequestParam(defaultValue = "desc") String sort
    ) {
        try {
            List<PaymentResponse> result =
                    paymentService.sortPaymentsByCreatedAt(sort);

            return ResponseEntity.ok(
                    new ApiResponse<>("Sort payment thành công", result)
            );

        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }




   
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

   
    @GetMapping("/{id}/history")
    @Operation(summary = "Lấy lịch sử thanh toán của Payment")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lịch sử thanh toán",
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
