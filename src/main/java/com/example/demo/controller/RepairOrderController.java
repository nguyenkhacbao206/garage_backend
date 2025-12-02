package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.RepairOrder;
import com.example.demo.entity.RepairOrderItem;
import com.example.demo.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair-orders")
@Tag(name = "Repair Orders", description = "Quản lý hoá đơn sửa chữa")
public class RepairOrderController {

    private final RepairOrderService service;

    public RepairOrderController(RepairOrderService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Tạo đơn sửa chữa mới")
    public ResponseEntity<ApiResponse<RepairOrder>> create(@RequestBody RepairOrder order) {
        try {
            return ResponseEntity.ok(
                    new ApiResponse<>("Tạo đơn thành công", service.createRepairOrder(order))
            );
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả đơn sửa chữa")
    public ResponseEntity<ApiResponse<List<RepairOrder>>> getAll() {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", service.getAllOrders()));
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết đơn sửa chữa")
    public ResponseEntity<ApiResponse<RepairOrder>> getById(
            @Parameter(description = "ID của đơn") @PathVariable String id) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Thành công", service.getOrderById(id)));
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật đơn sửa chữa")
    public ResponseEntity<ApiResponse<RepairOrder>> update(
            @PathVariable String id,
            @RequestBody RepairOrder order) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", service.updateOrder(id, order)));
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Đánh dấu đã sửa xong")
    public ResponseEntity<ApiResponse<RepairOrder>> complete(@PathVariable String id) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Đã hoàn tất sửa chữa", service.completeOrder(id)));
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/{id}/pay")
    @Operation(summary = "Thanh toán hoá đơn")
    public ResponseEntity<ApiResponse<RepairOrder>> pay(@PathVariable String id) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Thanh toán thành công", service.payOrder(id)));
        } catch (Exception e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa hoá đơn")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        try {
            service.deleteOrder(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa thành công", null));
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/{orderId}/items")
    @Operation(summary = "Thêm item vào đơn")
    public ResponseEntity<ApiResponse<RepairOrderItem>> addItem(
            @PathVariable String orderId,
            @RequestParam boolean isPart,
            @RequestBody RepairOrderItem item) {
        try {
            return ResponseEntity.ok(
                    new ApiResponse<>("Thêm item thành công", service.addItem(orderId, item, isPart))
            );
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Cập nhật item")
    public ResponseEntity<ApiResponse<RepairOrderItem>> updateItem(
            @PathVariable String itemId,
            @RequestBody RepairOrderItem item) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật item thành công", service.updateItem(itemId, item)));
        } catch (Exception e) {
            throw e;
        }
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Xóa item")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable String orderId,
            @PathVariable String itemId,
            @RequestParam boolean isPart) {
        try {
            service.deleteItem(orderId, itemId, isPart);
            return ResponseEntity.ok(new ApiResponse<>("Xóa item thành công", null));
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Lấy danh sách item của đơn")
    public ResponseEntity<ApiResponse<List<RepairOrderItem>>> getItems(
            @PathVariable String orderId) {
        try {
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy items thành công", service.getItems(orderId))
            );
        } catch (Exception e) {
            throw e;
        }
    }
}
