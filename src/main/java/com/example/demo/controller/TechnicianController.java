package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.TechnicianRequest;
import com.example.demo.dto.TechnicianResponse;
import com.example.demo.service.TechnicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@CrossOrigin(origins = "*")
@Tag(name = "Technician", description = "API quản lý kỹ thuật viên")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách kỹ thuật viên")
    public ResponseEntity<ApiResponse<List<TechnicianResponse>>> getAll() {
        try {
            List<TechnicianResponse> list = technicianService.getAll();
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", list));
        } catch (Exception ex) {
            // Log lỗi, sau đó ném lên GlobalExceptionHandler
            System.err.println("Lỗi lấy danh sách kỹ thuật viên: " + ex.getMessage());
            throw ex;
        }
    }

    @PostMapping
    @Operation(summary = "Tạo kỹ thuật viên")
    public ResponseEntity<ApiResponse<TechnicianResponse>> create(@Valid @RequestBody TechnicianRequest req) {
        try {
            TechnicianResponse response = technicianService.create(req);
            return ResponseEntity.ok(new ApiResponse<>("Tạo thành công", response));
        } catch (DuplicateKeyException dx) {
            System.err.println("Duplicate key khi tạo kỹ thuật viên: " + dx.getMessage());
            throw dx;
        } catch (Exception ex) {
            System.err.println("Lỗi tạo kỹ thuật viên: " + ex.getMessage());
            throw ex;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật kỹ thuật viên")
    public ResponseEntity<ApiResponse<TechnicianResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody TechnicianRequest req) {
        try {
            TechnicianResponse response = technicianService.update(id, req);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", response));
        } catch (DuplicateKeyException dx) {
            System.err.println("Duplicate key khi cập nhật kỹ thuật viên: " + dx.getMessage());
            throw dx;
        } catch (Exception ex) {
            System.err.println("Lỗi cập nhật kỹ thuật viên: " + ex.getMessage());
            throw ex;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa kỹ thuật viên")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String id) {
        try {
            technicianService.delete(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa thành công", null));
        } catch (Exception ex) {
            System.err.println("Lỗi xóa kỹ thuật viên: " + ex.getMessage());
            throw ex;
        }
    }
}
