package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.entity.Part;

public interface PartRepository extends MongoRepository<Part, String> {
}
