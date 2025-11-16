package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    // Tìm kiếm riêng lẻ theo từng trường
    List<Customer> findByNameContainingIgnoreCase(String name);

    List<Customer> findByPhoneContaining(String phone);

    List<Customer> findByEmailContainingIgnoreCase(String email);

    List<Customer> findByCustomerCode(String customerCode);

    // Tìm kiếm theo nhiều trường cùng lúc 
    List<Customer> findByCustomerCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCase(
            String customerCode, String name, String phone, String email);

    // Kiểm tra tồn tại
    boolean existsByName(String name);

    boolean existsByCustomerCode(String customerCode);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Customer findTopByOrderByCustomerCodeDesc();
}
