package com.example.demo.controller;

import com.example.demo.dto.SupplierRequest;
import com.example.demo.entity.Supplier;
import com.example.demo.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
@Tag(name = "Supplier", description = "Quản lý nhà cung cấp")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(summary = "Lấy danh sách tất cả nhà cung cấp hoặc tìm theo tên")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String name) {
        List<Supplier> suppliers = supplierService.getAll(name);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Danh sách nhà cung cấp");
        response.put("data", suppliers);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy nhà cung cấp theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Optional<Supplier> supplierOpt = supplierService.getById(id);
            if (supplierOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Thông tin nhà cung cấp");
                response.put("data", supplierOpt.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Không tìm thấy nhà cung cấp với ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Tạo nhà cung cấp mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SupplierRequest request) {
        try {
            Supplier created = supplierService.create(request);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Tạo nhà cung cấp thành công");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Cập nhật nhà cung cấp theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody SupplierRequest request) {
        try {
            Optional<Supplier> updatedOpt = supplierService.update(id, request);
            if (updatedOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Cập nhật nhà cung cấp thành công");
                response.put("data", updatedOpt.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Không tìm thấy nhà cung cấp với ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Xóa nhà cung cấp theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            boolean deleted = supplierService.delete(id);
            Map<String, Object> response = new HashMap<>();
            if (deleted) {
                response.put("message", "Xóa nhà cung cấp thành công");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Không tìm thấy nhà cung cấp với ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
