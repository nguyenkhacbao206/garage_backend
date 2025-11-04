package com.example.demo.controller;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.dto.CustomerResponse;
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
        List<Customer> list = customerService.getAll(name);
        return ResponseEntity.ok(new CustomerResponse("Lấy danh sách khách hàng thành công", list));
    }

    @Operation(summary = "Lấy chi tiết khách hàng theo id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return customerService.getById(id)
                .map(c -> ResponseEntity.ok(new CustomerResponse("Chi tiết khách hàng", c)))
                .orElse(ResponseEntity.status(404).body(new CustomerResponse("Không tìm thấy khách hàng", null)));
    }

    @Operation(summary = "Thêm khách hàng mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerRequest request) {
        Customer newCustomer = customerService.create(request);
        return ResponseEntity.ok(new CustomerResponse("Thêm khách hàng thành công", newCustomer));
    }

    @Operation(summary = "Cập nhật khách hàng theo id")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CustomerRequest request) {
        return customerService.update(id, request)
                .map(c -> ResponseEntity.ok(new CustomerResponse("Cập nhật khách hàng thành công", c)))
                .orElse(ResponseEntity.status(404).body(new CustomerResponse("Không tìm thấy khách hàng", null)));
    }

    @Operation(summary = "Xóa khách hàng theo id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = customerService.delete(id);
        if (deleted)
            return ResponseEntity.ok(new CustomerResponse("Xóa khách hàng thành công", null));
        else
            return ResponseEntity.status(404).body(new CustomerResponse("Không tìm thấy khách hàng", null));
    }

    @Operation(summary = "Lấy danh sách xe của khách hàng")
    @GetMapping("/{id}/cars")
    public ResponseEntity<?> getCars(@PathVariable String id) {
        Optional<Customer> customerOpt = customerService.getById(id);
        if (customerOpt.isPresent()) {
            return ResponseEntity.ok(customerOpt.get().getCars());
        }
        return ResponseEntity.status(404).body("Không tìm thấy khách hàng");
    }

    @Operation(summary = "Thêm xe mới cho khách hàng")
    @PostMapping("/{id}/cars")
    public ResponseEntity<?> addCar(@PathVariable String id, @RequestBody Customer.Car car) {
        Optional<Customer.Car> added = customerService.addCar(id, car);
        if (added.isPresent()) {
            return ResponseEntity.ok(new CustomerResponse("Thêm xe mới cho khách hàng thành công", added.get()));
        }
        return ResponseEntity.status(404).body(new CustomerResponse("Không tìm thấy khách hàng", null));
    }
}
