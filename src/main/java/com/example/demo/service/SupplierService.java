package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SupplierResponse;
import com.example.demo.dto.SupplierRequest;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;
    // sort by created decrease, increase
    public List<Supplier> sortByCreatedAt(List<Supplier> customers, boolean asc) {

        Comparator<Supplier> comp = Comparator.comparing(
                Supplier::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder())
        );

        if (!asc) {
            comp = comp.reversed();
        }

        customers.sort(comp);
        return customers;
    }
    // Get all cars
    public List<SupplierResponse> getAllCars() {
        return supplierRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Search nâng cao theo keyword (code, name, phone hoặc email)
public List<Supplier> searchSuppliers(String keyword) {
    if (keyword == null || keyword.trim().isEmpty()) {
        return supplierRepository.findAll();
    }
    //dùng để lấy query phần supplierreponsitori
    return supplierRepository.searchByKeyword(keyword);
}


    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    private SupplierResponse convertToResponse(Supplier supplier) {
        return new SupplierResponse("Lấy nhà cung cấp thành công", supplier);
    }

    // Lấy supplier theo id
    public Optional<Supplier> getById(String id) {
        return supplierRepository.findById(id);
    }

    // Tạo mới supplier
    public Supplier create(SupplierRequest request) {
        String phone = request.getPhone();
        if (phone == null || !phone.matches("0\\d{9}")) {
            throw new RuntimeException("Số điện thoại phải gồm đúng 10 chữ số và số 0  phải ở đầu!");
        }
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
        s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());

        // Tạo mã nhà cung cấp (NCC-001)
                Supplier lastSupplier = supplierRepository.findFirstByOrderBySupplierCodeDesc();
        String newCode;

        if (lastSupplier == null || lastSupplier.getSupplierCode() == null) {
        newCode = "NCC-001";
        } else {
        String maxCode = lastSupplier.getSupplierCode();
        //cắt phần chữ độc lấy số
        String numberPart = maxCode.substring(4);
        int number = Integer.parseInt(numberPart);
        //tăng lên 1
        number++;
        //khi vượt quá 3 số vẫn tăng
        newCode = "NCC-" + String.format("%03d", number);
    }

    s.setSupplierCode(newCode);

    return supplierRepository.save(s);

    }

    // Cập nhật supplier
    public Optional<Supplier> update(String id, SupplierRequest request) {
        Optional<Supplier> existing = supplierRepository.findById(id);
        if (existing.isPresent()) {
            Supplier s = existing.get();

            String phone = request.getPhone();
            if (phone != null) {
            if (!phone.matches("0\\d{9}")) {
                throw new RuntimeException("Số điện thoại phải gồm đúng 10 chữ số và số 0 phải ở đầu!");
            }
            if (!phone.equals(s.getPhone()) && supplierRepository.existsByPhone(phone)) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }
        s.setPhone(phone);
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
