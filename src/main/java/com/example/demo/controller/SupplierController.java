package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.SupplierRequest;
import com.example.demo.entity.Supplier;
import com.example.demo.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ResponseEntity.ok(
                new ApiResponse<>("Danh sách nhà cung cấp", suppliers)
        );
    }

    @Operation(summary = "Lấy nhà cung cấp theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Optional<Supplier> supplierOpt = supplierService.getById(id);
            if (supplierOpt.isPresent()) {
                return ResponseEntity.ok(
                        new ApiResponse<>("Thông tin nhà cung cấp", supplierOpt.get())
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Không tìm thấy nhà cung cấp với ID: " + id, null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @Operation(summary = "Tạo nhà cung cấp mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SupplierRequest request) {
        try {
            Supplier created = supplierService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Tạo nhà cung cấp thành công", created));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @Operation(summary = "Cập nhật nhà cung cấp theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id,
                                    @RequestBody SupplierRequest request) {
        try {
            Optional<Supplier> updatedOpt = supplierService.update(id, request);

            if (updatedOpt.isPresent()) {
                return ResponseEntity.ok(
                        new ApiResponse<>("Cập nhật nhà cung cấp thành công", updatedOpt.get())
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Không tìm thấy nhà cung cấp với ID: " + id, null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @Operation(summary = "Xóa nhà cung cấp theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            boolean deleted = supplierService.delete(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>("Xóa nhà cung cấp thành công", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Không tìm thấy nhà cung cấp với ID: " + id, null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
