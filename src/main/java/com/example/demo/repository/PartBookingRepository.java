package com.example.demo.repository;

import com.example.demo.entity.PartBooking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartBookingRepository extends MongoRepository<PartBooking, String> {
    
}
