package com.example.demo.repository;

import com.example.demo.entity.Technician;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicianRepository extends MongoRepository<Technician, String> {

    boolean existsByPhone(String phone);

    Technician findTopByOrderByTechCodeDesc(); // lấy mã lớn nhất

   // Search theo techCode, name, phone
    List<Technician> findByTechCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String techCode, String name, String phone);
}
