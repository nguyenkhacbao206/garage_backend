package com.example.demo.repository;

import com.example.demo.entity.GarageService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends MongoRepository<GarageService, String> {
    // kiểm tra xen tên dịch vụ đã tồn tại chưa
    boolean existsByName(String name);
}
