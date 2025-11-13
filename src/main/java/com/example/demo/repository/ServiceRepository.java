package com.example.demo.repository;

import com.example.demo.entity.Customer;
import com.example.demo.entity.GarageService;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends MongoRepository<GarageService, String> {
    // kiểm tra xen tên dịch vụ đã tồn tại chưa
    boolean existsByName(String name);
    boolean existsByServiceCode(String serviceCode);

    //tìm kiếm riêng lẻ từng trường
    List<GarageService> findByNameContainingIgnoreCase(String name);

    List<GarageService> findByServiceCode(String serviceCode);
    
    // Tìm kiếm theo nhiều trường cùng lúc
    List<GarageService> findByServiceCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            String serviceCode, String name);
}
