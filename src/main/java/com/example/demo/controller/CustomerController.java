package com.example.demo.controller;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
@Tag(name = "Customer", description = "API quản lý khách hàng")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Lấy danh sách khách hàng, có thể filter theo tên")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String name) {
        try {
            List<Customer> list = customerService.getAll(name);
            return ResponseEntity.ok(new CustomerResponse("Lấy danh sách khách hàng thành công", list));
        } catch (RuntimeException e) {
            throw e; // ném ra để GlobalExceptionHandler xử lý
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
    }

    @Operation(summary = "Lấy chi tiết khách hàng theo id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            return customerService.getById(id)
                    .map(c -> ResponseEntity.ok(new CustomerResponse("Chi tiết khách hàng", c)))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết khách hàng: " + e.getMessage());
        }
    }

    @Operation(summary = "Thêm khách hàng mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerRequest request) {
        try {
            Customer newCustomer = customerService.create(request);
            return ResponseEntity.ok(new CustomerResponse("Thêm khách hàng thành công", newCustomer));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm khách hàng: " + e.getMessage());
        }
    }

    @Operation(summary = "Cập nhật khách hàng theo id")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CustomerRequest request) {
        try {
            return customerService.update(id, request)
                    .map(c -> ResponseEntity.ok(new CustomerResponse("Cập nhật khách hàng thành công", c)))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật khách hàng: " + e.getMessage());
        }
    }

    @Operation(summary = "Xóa khách hàng theo id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            boolean deleted = customerService.delete(id);
            if (!deleted) {
                throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
            }
            return ResponseEntity.ok(new CustomerResponse("Xóa khách hàng thành công", null));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa khách hàng: " + e.getMessage());
        }
    }

    @Operation(summary = "Lấy danh sách xe của khách hàng")
    @GetMapping("/{id}/cars")
    public ResponseEntity<?> getCars(@PathVariable String id) {
        try {
            Optional<Customer> customerOpt = customerService.getById(id);
            if (customerOpt.isEmpty()) {
                throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
            }
            return ResponseEntity.ok(new CustomerResponse("Danh sách xe của khách hàng", customerOpt.get().getCars()));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách xe: " + e.getMessage());
        }
    }

    @Operation(summary = "Thêm xe mới cho khách hàng")
    @PostMapping("/{id}/cars")
    public ResponseEntity<?> addCar(@PathVariable String id, @RequestBody Car car) {
        try {
            Optional<Car> added = customerService.addCar(id, car);
            if (added.isEmpty()) {
                throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
            }
            return ResponseEntity.ok(new CustomerResponse("Thêm xe mới cho khách hàng thành công", added.get()));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm xe cho khách hàng: " + e.getMessage());
        }
    }
}
