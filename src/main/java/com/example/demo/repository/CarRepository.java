package com.example.demo.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.entity.Car;

public interface CarRepository extends MongoRepository<Car, String> {
    List<Car> findByCustomerId(String customerId);

    // kiểm tra trùng biển
    boolean existsByPlate(String plate);

     // Tìm theo biển số (search keyword)
    List<Car> findByPlateContainingIgnoreCase(String plate);

    // Tìm theo biển số trong phạm vi 1 khách hàng (customerCode)
    List<Car> findByPlateContainingIgnoreCaseAndCustomerCode(String plate, String customerCode);
}
