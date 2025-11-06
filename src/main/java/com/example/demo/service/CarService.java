package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.entity.Car;
import com.example.demo.repository.CarRepository;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CarResponse getCarById(String id) {
        return carRepository.findById(id)
                .map(this::convertToResponse)
                .orElse(null);
    }

    public List<CarResponse> getCarsByCustomerId(String customerId) {
        return carRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Create new car
    public CarResponse createCar(CarRequest request) {
        Car car = new Car();
        car.setPlate(request.getPlate());
        car.setModel(request.getModel());
        car.setManufacturer(request.getManufacturer());
        car.setDescription(request.getDescription());
        car.setCustomerId(request.getCustomerId());
        return convertToResponse(carRepository.save(car));
    }

    // Update existing car
    public CarResponse updateCar(String id, CarRequest request) {
        Car car = carRepository.findById(id).orElse(null);
        if (car == null) return null;

        car.setPlate(request.getPlate());
        car.setModel(request.getModel());
        car.setManufacturer(request.getManufacturer());
        car.setDescription(request.getDescription());
        car.setCustomerId(request.getCustomerId());

        return convertToResponse(carRepository.save(car));
    }

    public void deleteCar(String id) {
        carRepository.deleteById(id);
    }

    // Convert Entity -> DTO
    private CarResponse convertToResponse(Car car) {
        CarResponse res = new CarResponse();
        res.setId(car.getId());
        res.setPlate(car.getPlate());
        res.setModel(car.getModel());
        res.setManufacturer(car.getManufacturer());
        res.setDescription(car.getDescription());
        res.setCustomerId(car.getCustomerId());
        return res;
    }
}
