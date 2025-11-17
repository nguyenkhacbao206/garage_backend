package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.RepairOrder;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair-orders")
@Tag(name = "Repair Orders", description = "Quản lý hóa đơn sửa chữa")
public class RepairOrderController {

    private final RepairOrderService service;

    public RepairOrderController(RepairOrderService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Tạo đơn sửa chữa mới")
    public ResponseEntity<ApiResponse<RepairOrder>> create(@RequestBody RepairOrder order) {
        try {
            RepairOrder saved = service.createRepairOrder(order);
            return ResponseEntity.ok(new ApiResponse<>("Tạo đơn thành công", saved));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả đơn sửa chữa")
    public ResponseEntity<ApiResponse<List<RepairOrder>>> getAll() {
        try {
            List<RepairOrder> orders = service.getAllOrders();
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", orders));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết đơn sửa chữa")
    public ResponseEntity<ApiResponse<RepairOrder>> getById(@PathVariable String id) {
        try {
            return service.getOrderById(id)
                    .map(order -> ResponseEntity.ok(new ApiResponse<>("Thành công", order)))
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn với id: " + id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật đơn sửa chữa (items, status)")
    public ResponseEntity<ApiResponse<RepairOrder>> update(@PathVariable String id, @RequestBody RepairOrder order) {
        try {
            RepairOrder updated = service.updateOrder(id, order);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updated));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Đánh dấu sửa xong")
    public ResponseEntity<ApiResponse<RepairOrder>> complete(@PathVariable String id) {
        try {
            RepairOrder updated = service.completeOrder(id);
            return ResponseEntity.ok(new ApiResponse<>("Đã hoàn tất sửa chữa", updated));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @PutMapping("/{id}/pay")
    @Operation(summary = "Thanh toán hóa đơn")
    public ResponseEntity<ApiResponse<RepairOrder>> pay(@PathVariable String id) {
        try {
            RepairOrder updated = service.payOrder(id);
            return ResponseEntity.ok(new ApiResponse<>("Thanh toán thành công", updated));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa đơn sửa chữa")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        try {
            service.deleteOrder(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa thành công", null));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }
}
