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
            System.err.println("Lỗi lấy danh sách kỹ thuật viên: " + ex.getMessage());
            throw ex;
        }
    }

    @GetMapping("/sort")
    @Operation(summary = "Sắp xếp kỹ thuật viên theo ngày tạo")
    public ResponseEntity<ApiResponse<List<TechnicianResponse>>> sort(
            @RequestParam(defaultValue = "false") boolean asc
    ) {
        try {
            // lấy toàn bộ danh sách
            List<TechnicianResponse> list = technicianService.getAll();

            // gọi hàm sort trong service
            List<TechnicianResponse> sorted = technicianService.sortByCreatedAt(list, asc);

            return ResponseEntity.ok(new ApiResponse<>(
                    asc ? "Sắp xếp tăng dần thành công" : "Sắp xếp giảm dần thành công",
                    sorted
            ));
        } catch (Exception ex) {
            System.err.println("Lỗi sắp xếp kỹ thuật viên: " + ex.getMessage());
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Lỗi sắp xếp: " + ex.getMessage(), null)
            );
        }
    }


    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm kỹ thuật viên theo mã, tên hoặc số điện thoại")
    public ResponseEntity<ApiResponse<List<TechnicianResponse>>> search(
            @RequestParam(required = false) String keyword) {

        try {
            List<TechnicianResponse> results = technicianService.search(keyword);
            return ResponseEntity.ok(new ApiResponse<>(
                    "Tìm kiếm thành công",
                    results
            ));
        } catch (Exception ex) {
            System.err.println("Lỗi tìm kiếm kỹ thuật viên: " + ex.getMessage());
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Lỗi tìm kiếm: " + ex.getMessage(), null)
            );
        }
    }

    @PostMapping
    @Operation(summary = "Tạo kỹ thuật viên")
    public ResponseEntity<ApiResponse<TechnicianResponse>> create(
            @Valid @RequestBody TechnicianRequest req) {
        try {
            TechnicianResponse response = technicianService.create(req);
            return ResponseEntity.ok(new ApiResponse<>(
                    "Tạo thành công",
                    response
            ));
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
            return ResponseEntity.ok(new ApiResponse<>(
                    "Cập nhật thành công",
                    response
            ));
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
            return ResponseEntity.ok(new ApiResponse<>(
                    "Xóa thành công",
                    null
            ));
        } catch (Exception ex) {
            System.err.println("Lỗi xóa kỹ thuật viên: " + ex.getMessage());
            throw ex;
        }
    }
}
