package com.example.demo.repository;

import com.example.demo.entity.Supplier;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsBySupplierCode(String supplierCode);
    List<Supplier> findByNameContainingIgnoreCase(String name);
}
