package com.example.demo.controller;

import com.example.demo.dto.ServiceRequest;
import com.example.demo.dto.ServiceResponse;
import com.example.demo.service.ServiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.GarageService;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
@Tag(name = "Service", description = "API quản lý dịch vụ garage")
public class ServiceController {

    private final ServiceService serviceService;

    @Operation(summary = "Tìm kiếm dịch vụ theo mã dịch vụ hoặc tên")
@GetMapping("/search")
public ResponseEntity<?> search(
        @RequestParam(required = false) String serviceCode,
        @RequestParam(required = false) String name
) {
    try {
        List<GarageService> results = serviceService.searchServices(serviceCode, name);
        return ResponseEntity.ok(results);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Lỗi khi tìm kiếm dịch vụ: " + e.getMessage());
    }
}

    public ServiceController(ServiceService serviceService){
        this.serviceService = serviceService;
    }

    @GetMapping("/sort")
    public ResponseEntity<?> sortServices(@RequestParam(defaultValue = "false") boolean asc) {
        return ResponseEntity.ok(serviceService.sortServicesByCreatedAt(asc));
    }


    @Operation(summary = "Lấy danh sách tất cả dịch vụ")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ServiceResponse> list = serviceService.getAllServices();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi lấy danh sách: " + e.getMessage());
        }
    }

    @Operation(summary = "Lấy dịch vụ theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(serviceService.getServiceById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Thêm dịch vụ mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ServiceRequest request) {
        try {
            return ResponseEntity.ok(serviceService.createService(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Cập nhật dịch vụ theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ServiceRequest request) {
        try {
            return ResponseEntity.ok(serviceService.updateService(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Xóa dịch vụ theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            serviceService.deleteService(id);
            return ResponseEntity.ok("Xóa dịch vụ thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thể xóa: " + e.getMessage());
        }
    }
}
