package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ServiceResponse;
import com.example.demo.dto.SupplierRequest;
import com.example.demo.dto.SupplierResponse;
import com.example.demo.entity.Customer;
import com.example.demo.entity.GarageService;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // Lấy tất cả supplier hoặc tìm theo tên
    public List<Supplier> getAll(String name) {
        if (name != null && !name.isEmpty()) {
            return supplierRepository.findByNameContainingIgnoreCase(name);
        }
        return supplierRepository.findAll();
    }
    
    public List<Supplier> searchSuppliers(String supplierCode , String name) {
        boolean hasCode = supplierCode != null && !supplierCode.isEmpty();
        boolean hasName = name != null && !name.isEmpty();

        if (!hasName&& !hasCode) {
            return supplierRepository.findAll();
        }

    // Chỉ code
    if (hasCode) {
        return supplierRepository.findBysupplierCodeContainingIgnoreCase(supplierCode);
    }

    // Chỉ name
    return supplierRepository.findByNameContainingIgnoreCase(name);
    }

    private SupplierResponse convertToResponse(Supplier supplier) {
    SupplierResponse response = new SupplierResponse();
        response.setId(supplier.getId());
        response.setSupplierCode(supplier.getSupplierCode());
        response.setName(supplier.getName());
        response.setAddress(supplier.getAddress());
        response.setEmail(supplier.getEmail());
        response.setPhone(supplier.getPhone());
        response.setDescription(supplier.getDescription());
        return response;
    }

    // Lấy supplier theo id
    public Optional<Supplier> getById(String id) {
        return supplierRepository.findById(id);
    }

    // Tạo mới supplier
    public Supplier create(SupplierRequest request) {
        if (supplierRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại!");
        }
        if (supplierRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        Supplier s = new Supplier();
        s.setName(request.getName());
        s.setAddress(request.getAddress());
        s.setEmail(request.getEmail());
        s.setPhone(request.getPhone());
        s.setDescription(request.getDescription());

        // Tạo mã nhà cung cấp ngẫu nhiên (NCC-001)
        String randomCode;
        do {
            randomCode = String.format("NCC-%03d", ThreadLocalRandom.current().nextInt(0, 1000));
        } while (supplierRepository.existsBySupplierCode(randomCode));

        s.setSupplierCode(randomCode);

        return supplierRepository.save(s);
    }

    // Cập nhật supplier
    public Optional<Supplier> update(String id, SupplierRequest request) {
        Optional<Supplier> existing = supplierRepository.findById(id);
        if (existing.isPresent()) {
            Supplier s = existing.get();

            if (request.getPhone() != null && !request.getPhone().equals(s.getPhone())
                    && supplierRepository.existsByPhone(request.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }

            if (request.getEmail() != null && !request.getEmail().equals(s.getEmail())
                    && supplierRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }

            if (request.getName() != null) s.setName(request.getName());
            if (request.getAddress() != null) s.setAddress(request.getAddress());
            if (request.getEmail() != null) s.setEmail(request.getEmail());
            if (request.getPhone() != null) s.setPhone(request.getPhone());
            if (request.getDescription() != null) s.setDescription(request.getDescription());

            supplierRepository.save(s);
            return Optional.of(s);
        }
        return Optional.empty();
    }

    // Xóa supplier
    public boolean delete(String id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
