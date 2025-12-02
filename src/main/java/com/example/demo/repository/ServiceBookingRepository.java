package com.example.demo.repository;

import com.example.demo.entity.ServiceBooking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ServiceBookingRepository extends MongoRepository<ServiceBooking, String> {

    List<ServiceBooking> findByCustomerId(String customerId);

    List<ServiceBooking> findByServiceId(String serviceId);
}
