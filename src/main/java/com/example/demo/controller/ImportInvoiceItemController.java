package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ImportInvoiceItemRequest;
import com.example.demo.dto.ImportInvoiceItemResponse;
import com.example.demo.service.ImportInvoiceItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-invoice-items")
@CrossOrigin(origins = "*")
@Tag(name = "Import Invoice Item API")
public class ImportInvoiceItemController {

    private final ImportInvoiceItemService service;

    public ImportInvoiceItemController(ImportInvoiceItemService service) {
        this.service = service;
    }

    //SORT
    @GetMapping("/sort")
    @Operation(summary = "Sắp xếp item nhập theo ngày tạo")
    public ResponseEntity<ApiResponse<List<ImportInvoiceItemResponse>>> sort(
            @RequestParam(defaultValue = "false") boolean asc
    ) {
        try {
            List<ImportInvoiceItemResponse> list = service.getAll();
            List<ImportInvoiceItemResponse> sorted = service.sortByCreatedAt(list, asc);
            return ResponseEntity.ok(new ApiResponse<>(
                    asc ? "Sắp xếp tăng dần thành công" : "Sắp xếp giảm dần thành công",
                    sorted
            ));
        } catch (Exception ex) {
            // log lỗi riêng
            System.err.println("Lỗi khi sắp xếp item nhập: " + ex.getMessage());
            // ném tiếp để GlobalExceptionHandler xử lý
            throw new RuntimeException("Lỗi khi sắp xếp item nhập", ex);
        }
    }

    //CREATE
    @PostMapping
    @Operation(summary = "Tạo mới item nhập")
    public ResponseEntity<ApiResponse<ImportInvoiceItemResponse>> create(@RequestBody ImportInvoiceItemRequest req) {
        try {
            ImportInvoiceItemResponse res = service.create(req);
            return ResponseEntity.ok(new ApiResponse<>("Tạo mới thành công", res));
        } catch (Exception ex) {
            // log lỗi riêng nếu muốn
            System.err.println("Lỗi tạo item: " + ex.getMessage());
            throw ex;
        }
    }

    //GET ALL
    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả item")
    public ResponseEntity<ApiResponse<List<ImportInvoiceItemResponse>>> getAll() {
        try {
            List<ImportInvoiceItemResponse> list = service.getAll();
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", list));
        } catch (Exception ex) {
            System.err.println("Lỗi lấy danh sách item: " + ex.getMessage());
            throw ex;
        }
    }

    //GET BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Lấy item theo ID")
    public ResponseEntity<ApiResponse<ImportInvoiceItemResponse>> getById(@PathVariable String id) {
        try {
            return service.getById(id)
                    .map(item -> ResponseEntity.ok(new ApiResponse<>("Lấy item thành công", item)))
                    .orElseGet(() -> ResponseEntity.status(404)
                            .body(new ApiResponse<>("Không tìm thấy item với ID: " + id, null)));
        } catch (Exception ex) {
            System.err.println("Lỗi lấy item theo ID: " + ex.getMessage());
            throw ex;
        }
    }

    //UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật item theo ID")
    public ResponseEntity<ApiResponse<ImportInvoiceItemResponse>> update(
            @PathVariable String id,
            @RequestBody ImportInvoiceItemRequest req
    ) {
        try {
            return service.update(id, req)
                    .map(item -> ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", item)))
                    .orElseGet(() -> ResponseEntity.status(404)
                            .body(new ApiResponse<>("Không tìm thấy item với ID: " + id, null)));
        } catch (Exception ex) {
            System.err.println("Lỗi cập nhật item: " + ex.getMessage());
            throw ex;
        }
    }
    //DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa item theo ID")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String id) {
        try {
            boolean deleted = service.delete(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>("Đã xóa thành công!", id));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>("Không tìm thấy item với ID: " + id, null));
            }
        } catch (Exception ex) {
            System.err.println("Lỗi xóa item: " + ex.getMessage());
            throw ex;
        }
    }
}