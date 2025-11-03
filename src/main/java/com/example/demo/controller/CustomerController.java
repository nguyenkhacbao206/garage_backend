package com.example.demo.controller;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // get all customers
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String name) {
        List<Customer> list = customerService.getAll(name);
        return ResponseEntity.ok(new CustomerResponse("Lấy danh sách khách hàng thành công", list));
    }

    // get customer by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return customerService.getById(id)
                .map(c -> ResponseEntity.ok(new CustomerResponse("Chi tiết khách hàng", c)))
                .orElse(ResponseEntity.status(404).body(new CustomerResponse("Không tìm thấy khách hàng", null)));
    }

    // create new customer
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerRequest request) {
        Customer newCustomer = customerService.create(request);
        return ResponseEntity.ok(new CustomerResponse("Thêm khách hàng thành công", newCustomer));
    }

    // Update existing customer
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CustomerRequest request) {
        return customerService.update(id, request)
                .map(c -> ResponseEntity.ok(new CustomerResponse("Cập nhật khách hàng thành công", c)))
                .orElse(ResponseEntity.status(404).body(new CustomerResponse("Không tìm thấy khách hàng", null)));
    }

    // Delete a customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = customerService.delete(id);
        if (deleted)
            return ResponseEntity.ok(new CustomerResponse("Xóa khách hàng thành công", null));
        else
            return ResponseEntity.status(404).body(new CustomerResponse("Không tìm thấy khách hàng", null));
    }

    // Get all cars belonging to a specific customer
    @GetMapping("/{id}/cars")
    public ResponseEntity<?> getCars(@PathVariable String id) {
        Optional<Customer> customerOpt = customerService.getById(id);
        if (customerOpt.isPresent()) {
            return ResponseEntity.ok(customerOpt.get().getCars());
        }
        return ResponseEntity.status(404).body("Không tìm thấy khách hàng");
    }

    // Add a new car to a customer
    @PostMapping("/{id}/cars")
    public ResponseEntity<?> addCar(@PathVariable String id, @RequestBody Customer.Car car) {
        Optional<Customer.Car> added = customerService.addCar(id, car);
        if (added.isPresent()) {
            return ResponseEntity.ok(new CustomerResponse("Thêm xe mới cho khách hàng thành công", added.get()));
        }
        return ResponseEntity.status(404).body(new CustomerResponse("Không tìm thấy khách hàng", null));
    }
}
