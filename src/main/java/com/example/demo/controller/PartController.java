package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PartRequest;
import com.example.demo.dto.PartResponse;
import com.example.demo.service.PartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@CrossOrigin(origins = "*")
@Tag(name = "Parts", description = "API quản lý phụ tùng")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách phụ tùng")
    public ResponseEntity<ApiResponse<List<PartResponse>>> getAll() {
        try {
            List<PartResponse> data = partService.getAll();
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", data));
        } catch (Exception e) {
            throw e; // ném lỗi cho GlobalExceptionHandler bắt
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết phụ tùng")
    public ResponseEntity<ApiResponse<PartResponse>> getById(@PathVariable String id) {
        try {
            PartResponse data = partService.getById(id);
            return ResponseEntity.ok(new ApiResponse<>("Lấy chi tiết thành công", data));
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping
    @Operation(summary = "Thêm phụ tùng mới")
    public ResponseEntity<ApiResponse<PartResponse>> create(@Valid @RequestBody PartRequest req) {
        try {
            PartResponse data = partService.create(req);
            return ResponseEntity.ok(new ApiResponse<>("Thêm thành công", data));
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật phụ tùng")
    public ResponseEntity<ApiResponse<PartResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody PartRequest req) {
        try {
            PartResponse data = partService.update(id, req);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", data));
        } catch (Exception e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa phụ tùng")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String id) {
        try {
            partService.delete(id);
            return ResponseEntity.ok(new ApiResponse<>("Xóa thành công", null));
        } catch (Exception e) {
            throw e;
        }
    }
}
