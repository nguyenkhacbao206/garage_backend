package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.service.CarService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*")
@Tag(name = "Car", description = "API quản lý xe")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService){
        this.carService = carService;
    }

    @Operation(summary = "Lấy danh sách tất cả xe")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<CarResponse> list = carService.getAllCars();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách xe: " + e.getMessage());
        }
    }

    @Operation(summary = "Lấy xe theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            CarResponse car = carService.getCarById(id);
            if (car == null)
                throw new RuntimeException("Không tìm thấy xe với ID: " + id);
            return ResponseEntity.ok(car);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy xe: " + e.getMessage());
        }
    }

    @Operation(summary = "Lấy danh sách xe theo ID khách hàng")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getByCustomer(@PathVariable String customerId) {
        try {
            return ResponseEntity.ok(carService.getCarsByCustomerId(customerId));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách xe khách hàng: " + e.getMessage());
        }
    }

    @Operation(summary = "Thêm xe mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CarRequest request) {
        try {
            return ResponseEntity.ok(carService.createCar(request));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm xe: " + e.getMessage());
        }
    }

    @Operation(summary = "Cập nhật xe theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CarRequest request) {
        try {
            return ResponseEntity.ok(carService.updateCar(id, request));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật xe: " + e.getMessage());
        }
    }

    @Operation(summary = "Xóa xe theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok("Xóa xe thành công");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa xe: " + e.getMessage());
        }
    }
}
