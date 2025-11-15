package com.example.demo.repository;

import com.example.demo.entity.Technician;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicianRepository extends MongoRepository<Technician, String> {

    boolean existsByPhone(String phone);
}
