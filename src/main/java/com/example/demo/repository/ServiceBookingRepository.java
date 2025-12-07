package com.example.demo.repository;

import com.example.demo.entity.ServiceBooking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ServiceBookingRepository extends MongoRepository<ServiceBooking, String> {

    // Tìm theo số điện thoại khách hàng
    List<ServiceBooking> findByCustomerPhone(String customerPhone);

    // Tìm theo serviceId nằm trong list serviceIds
    List<ServiceBooking> findByServiceIdsContaining(String serviceId);
}
