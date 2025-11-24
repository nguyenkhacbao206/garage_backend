package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.SupplierRequest;
import com.example.demo.dto.SupplierResponse;
import com.example.demo.entity.Supplier;
import com.example.demo.service.SupplierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
@Tag(name = "Supplier", description = "Quản lý nhà cung cấp")
public class SupplierController {

    private final SupplierService supplierService;
//tạo phần tìm kiếm
    @Operation(summary = "Tìm kiếm nhà cung cấp nâng cao theo keyword")
@GetMapping("/search")
public ResponseEntity<?> search(@RequestParam String keyword) {
    try {
        List<Supplier> results = supplierService.searchSuppliers(keyword);
        return ResponseEntity.ok(new SupplierResponse("Kết quả tìm kiếm nhà cung cấp", results));
    } catch (RuntimeException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Lỗi khi tìm kiếm nhà cung cấp: " + e.getMessage());
    }
}

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(summary = "Lấy danh sách tất cả nhà cung cấp")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Supplier> suppliers = supplierService.getAll();
            return ResponseEntity.ok(
                new ApiResponse<>("Danh sách nhà cung cấp", suppliers)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Lỗi khi lấy danh sách: " + e.getMessage(), null));
        }
    }

    @GetMapping("/sort")
    public ResponseEntity<?> sortSuppliers(@RequestParam(defaultValue = "false") boolean asc) {
        try {
            List<SupplierResponse> sortedSuppliers = supplierService.sortSuppliersByCreatedAt(asc);

        if (sortedSuppliers.isEmpty()) {
            throw new RuntimeException("Không tìm thấy Supplier nào");
        }

        return ResponseEntity.ok(sortedSuppliers);
    } catch (RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(ex.getMessage(), null));
    } catch (Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null));
        }
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
