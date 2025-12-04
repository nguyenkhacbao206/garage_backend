package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.RepairOrderRequest;
import com.example.demo.dto.RepairOrderResponse;
import com.example.demo.dto.RepairOrderItemResponse;
import com.example.demo.entity.RepairOrderItem;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/repair-orders")
@Tag(name = "Repair Orders", description = "Quản lý hoá đơn sửa chữa")
public class RepairOrderController {

    private final RepairOrderService service;

    public RepairOrderController(RepairOrderService service) {
        this.service = service;
    }

 
    private RepairOrderItemResponse toItemResponse(RepairOrderItem item) {
        RepairOrderItemResponse resp = new RepairOrderItemResponse();
        resp.setId(item.getId());
        resp.setName(item.getName());
        resp.setUnitPrice(item.getUnitPrice());
        resp.setQuantity(item.getQuantity());
        resp.setTotal(item.getTotal());
        return resp;
    }

   
    @PostMapping
    @Operation(summary = "Tạo đơn sửa chữa mới")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> create(@RequestBody RepairOrderRequest request) {
        try {
            RepairOrderResponse resp = service.createRepairOrder(request);
            return ResponseEntity.ok(new ApiResponse<>("Tạo đơn thành công", resp));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi tạo đơn: " + e.getMessage(), null));
        }
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả đơn sửa chữa")
    public ResponseEntity<ApiResponse<List<RepairOrderResponse>>> getAll() {
        try {
            List<RepairOrderResponse> list = service.getAllOrders().stream()
                    .map(service::convertToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi lấy danh sách: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết đơn sửa chữa")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> getById(
            @Parameter(description = "ID của đơn") @PathVariable String id) {
        try {
            RepairOrderResponse resp = service.convertToResponse(service.getOrderById(id));
            return ResponseEntity.ok(new ApiResponse<>("Thành công", resp));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi lấy đơn: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật đơn sửa chữa")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> update(
            @PathVariable String id,
            @RequestBody RepairOrderRequest request) {
        try {
            RepairOrderResponse resp = service.updateOrder(id, request);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", resp));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi cập nhật đơn: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Đánh dấu đã sửa xong")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> complete(@PathVariable String id) {
        try {
            RepairOrderResponse resp = service.convertToResponse(service.completeOrder(id));
            return ResponseEntity.ok(new ApiResponse<>("Hoàn tất sửa chữa", resp));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi hoàn tất đơn: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/pay")
    @Operation(summary = "Thanh toán hoá đơn")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> pay(@PathVariable String id) {
        try {
            RepairOrderResponse resp = service.convertToResponse(service.payOrder(id));
            return ResponseEntity.ok(new ApiResponse<>("Thanh toán thành công", resp));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi thanh toán: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa hoá đơn")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        try {
            service.deleteOrder(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa thành công", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi xóa đơn: " + e.getMessage(), null));
        }
    }


    @PostMapping("/{orderId}/items")
    @Operation(summary = "Thêm item vào đơn")
    public ResponseEntity<ApiResponse<RepairOrderItemResponse>> addItem(
            @PathVariable String orderId,
            @RequestParam boolean isPart,
            @RequestBody RepairOrderItem item) {
        try {
            RepairOrderItem saved = service.addItem(orderId, item, isPart);
            return ResponseEntity.ok(new ApiResponse<>("Thêm item thành công", toItemResponse(saved)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi thêm item: " + e.getMessage(), null));
        }
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Cập nhật item")
    public ResponseEntity<ApiResponse<RepairOrderItemResponse>> updateItem(
            @PathVariable String itemId,
            @RequestBody RepairOrderItem item) {
        try {
            RepairOrderItem updated = service.updateItem(itemId, item);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật item thành công", toItemResponse(updated)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi cập nhật item: " + e.getMessage(), null));
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
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi xóa item: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Lấy danh sách item của đơn")
    public ResponseEntity<ApiResponse<List<RepairOrderItemResponse>>> getItems(
            @PathVariable String orderId) {
        try {
            List<RepairOrderItemResponse> list = service.getItems(orderId).stream()
                    .map(this::toItemResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>("Lấy items thành công", list));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi khi lấy items: " + e.getMessage(), null));
        }
    }
}
