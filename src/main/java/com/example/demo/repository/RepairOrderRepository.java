package com.example.demo.repository;

import com.example.demo.entity.RepairOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairOrderRepository extends MongoRepository<RepairOrder, String> {
}
