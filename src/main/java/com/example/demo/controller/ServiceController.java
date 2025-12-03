package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ServiceRequest;
import com.example.demo.dto.ServiceResponse;
import com.example.demo.service.ServiceService;
import com.example.demo.entity.GarageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
@Tag(name = "Service", description = "API quản lý dịch vụ garage")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    //SORT
    @GetMapping("/sort")
    @Operation(summary = "Sắp xếp item nhập theo ngày tạo")
    public ResponseEntity<ApiResponse<List<GarageService>>> sort(
            @RequestParam(defaultValue = "false") boolean asc
    ) {
        try {
            List<GarageService> list = serviceService.getAllServicesRaw();
            List<GarageService> sorted = serviceService.sortByCreatedAt(list, asc);

            return ResponseEntity.ok(new ApiResponse<>(
                asc ? "Sắp xếp tăng dần thành công" : "Sắp xếp giảm dần thành công",
                sorted
            ));
    }    catch (Exception ex) {
            System.err.println("Lỗi khi sắp xếp item nhập: " + ex.getMessage());
            throw new RuntimeException("Lỗi khi sắp xếp item nhập", ex);
        }
}


    @Operation(summary = "Lấy danh sách tất cả dịch vụ")
    @GetMapping
    public ResponseEntity<ServiceResponse> getAll() {
        try {
            List<GarageService> list = serviceService.getAllServicesRaw();
            return ResponseEntity.ok(new ServiceResponse("Danh sách dịch vụ", list));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ServiceResponse("Lỗi khi lấy danh sách: " + e.getMessage(), null));
        }
    }

    @Operation(summary = "Lấy dịch vụ theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable String id) {
        try {
            GarageService service = serviceService.getServiceByIdRaw(id);
            return ResponseEntity.ok(new ServiceResponse("Thành công", service));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ServiceResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Thêm dịch vụ mới")
    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody ServiceRequest request) {
        try {
            GarageService service = serviceService.createService(request);
            return ResponseEntity.ok(new ServiceResponse("Tạo dịch vụ thành công", service));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ServiceResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ServiceResponse("Lỗi server: " + e.getMessage(), null));
        }
    }

    @Operation(summary = "Cập nhật dịch vụ theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(@PathVariable String id, @RequestBody ServiceRequest request) {
        try {
            GarageService service = serviceService.updateService(id, request);
            return ResponseEntity.ok(new ServiceResponse("Cập nhật thành công", service));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ServiceResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ServiceResponse("Lỗi server: " + e.getMessage(), null));
        }
    }

    @Operation(summary = "Xóa dịch vụ theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable String id) {
        try {
            GarageService service = serviceService.deleteService(id);
            return ResponseEntity.ok(new ServiceResponse("Xóa thành công", service));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ServiceResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ServiceResponse("Lỗi server: " + e.getMessage(), null));
        }
    }

    @Operation(summary = "Tìm kiếm dịch vụ theo mã dịch vụ hoặc tên")
    @GetMapping("/search")
    public ResponseEntity<ServiceResponse> search(
            @RequestParam(required = false) String serviceCode,
            @RequestParam(required = false) String name
    ) {
        try {
            List<GarageService> list = serviceService.searchServicesRaw(serviceCode, name);
            return ResponseEntity.ok(new ServiceResponse("Kết quả tìm kiếm dịch vụ", list));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ServiceResponse("Lỗi khi tìm kiếm: " + e.getMessage(), null));
        }
    }
}
