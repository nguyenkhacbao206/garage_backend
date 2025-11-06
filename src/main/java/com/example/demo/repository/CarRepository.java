package com.example.demo.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.entity.Car;

public interface CarRepository extends MongoRepository<Car, String> {
    List<Car> findByCustomerId(String customerId);
}
