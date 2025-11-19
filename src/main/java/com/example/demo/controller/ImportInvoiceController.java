package com.example.demo.controller;

import com.example.demo.dto.ImportInvoiceRequest;
import com.example.demo.dto.ImportInvoiceResponse;
import com.example.demo.service.ImportInvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-invoices")
@CrossOrigin(origins = "*")
@Tag(name = "Import Invoice", description = "API quản lý phiếu nhập kho")
public class ImportInvoiceController {

    private final ImportInvoiceService importInvoiceService;

    public ImportInvoiceController(ImportInvoiceService importInvoiceService) {
        this.importInvoiceService = importInvoiceService;
    }

    // LẤY DANH SÁCH
    @Operation(summary = "Lấy danh sách tất cả phiếu nhập")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ImportInvoiceResponse> list = importInvoiceService.getAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi khi lấy danh sách phiếu nhập: " + e.getMessage());
        }
    }

    //LẤY THEO ID
    @Operation(summary = "Lấy phiếu nhập theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(importInvoiceService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // TẠO MỚI
    @Operation(summary = "Tạo phiếu nhập mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ImportInvoiceRequest req) {
        try {
            return ResponseEntity.ok(importInvoiceService.create(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Không thể tạo phiếu nhập: " + e.getMessage());
        }
    }

    //CẬP NHẬT
    @Operation(summary = "Cập nhật phiếu nhập theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ImportInvoiceRequest req) {
        try {
            return ResponseEntity.ok(importInvoiceService.update(id, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi cập nhật phiếu nhập: " + e.getMessage());
        }
    }

    // XÓA
    @Operation(summary = "Xóa phiếu nhập theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            importInvoiceService.delete(id);
            return ResponseEntity.ok("Xóa phiếu nhập thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Không thể xóa phiếu nhập: " + e.getMessage());
        }
    }
}
