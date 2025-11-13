package com.example.demo.repository;

import com.example.demo.entity.Supplier;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {
    //kiểm tra sdt và email của nhà cung cấp
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    //kiểm tra mã nhà cung cấp đã tồn tại chưa
    boolean existsBySupplierCode(String supplierCode);
    List<Supplier> findByNameContainingIgnoreCase(String name);
}
