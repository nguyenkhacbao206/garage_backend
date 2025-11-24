package com.example.demo.controller;

import com.example.demo.dto.ImportItemRequest;
import com.example.demo.dto.ImportItemResponse;
import com.example.demo.service.ImportItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/import-items")
@CrossOrigin(origins = "*")
@Tag(name = "Import Item", description = "API quản lý chi tiết hàng nhập")
public class ImportItemController {

    private final ImportItemService importItemService;

    public ImportItemController(ImportItemService importItemService) {
        this.importItemService = importItemService;
    }
    // Lấy tất cả
    @Operation(summary = "Lấy danh sách tất cả hàng nhập")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(importItemService.getAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi khi lấy danh sách hàng nhập: " + e.getMessage());
        }
    }

    // Lấy theo id
    @Operation(summary = "Lấy hàng nhập theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(importItemService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng nhập với ID: " + id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lấy theo invoice id
    @Operation(summary = "Lấy danh sách hàng nhập theo Invoice ID")
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<?> getByInvoiceId(@PathVariable String invoiceId) {
        try {
            return ResponseEntity.ok(importItemService.getByInvoiceId(invoiceId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi khi lấy hàng nhập theo invoice: " + e.getMessage());
        }
    }

    // Tạo mới
    @Operation(summary = "Tạo hàng nhập mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ImportItemRequest req) {
        try {
            return ResponseEntity.ok(importItemService.create(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Không thể tạo hàng nhập: " + e.getMessage());
        }
    }

    // Cập nhật
    @Operation(summary = "Cập nhật hàng nhập theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ImportItemRequest req) {
        try {
            Optional<ImportItemResponse> resOpt = importItemService.update(id, req);

            ImportItemResponse response = resOpt.orElseThrow(
                () -> new RuntimeException("Không tìm thấy hàng nhập với ID: " + id)
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi cập nhật hàng nhập: " + e.getMessage());
        }
    }

    // XÓA
    @Operation(summary = "Xóa hàng nhập theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            boolean ok = importItemService.delete(id);

            if (!ok) {
                throw new RuntimeException("Không tìm thấy hàng nhập với ID: " + id);
            }

            return ResponseEntity.ok("Xóa hàng nhập thành công");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Không thể xóa hàng nhập: " + e.getMessage());
        }
    }
}
