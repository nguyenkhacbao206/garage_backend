package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    List<Customer> findByNameContainingIgnoreCase(String name);

    List<Customer> findByPhoneContaining(String phone);

    List<Customer> findByEmailContainingIgnoreCase(String email);

    // Tìm theo nhiều trường cùng lúc
    List<Customer> findByNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCase(
            String name, String phone, String email);

    boolean existsByCustomerCode(String customerCode);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
