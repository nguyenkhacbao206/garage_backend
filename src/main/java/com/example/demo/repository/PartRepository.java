package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.entity.Part;

public interface PartRepository extends MongoRepository<Part, String> {
    List<Part> findByNameContainingIgnoreCaseOrPartCodeContainingIgnoreCase(
            String name,
            String partCode
    );

    Part findTopByOrderByPartCodeDesc();
}
