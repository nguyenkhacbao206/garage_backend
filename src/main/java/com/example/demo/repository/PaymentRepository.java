package com.example.demo.repository;

import com.example.demo.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByRepairOrderId(String repairOrderId);
    List<Payment> findByStatus(String status);
}
