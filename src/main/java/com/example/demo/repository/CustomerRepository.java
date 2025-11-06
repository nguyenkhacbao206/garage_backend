package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByNameContainingIgnoreCase(String name);

    // kiểm tra xem mã khách hàng tồn tại chưa
    boolean existsByCustomerCode(String customerCode);

    // kiểm tra sđt và email khách hàng
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    
    
}