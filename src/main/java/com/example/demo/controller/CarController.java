package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.service.CarService;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService){
        this.carService = carService;
    }

    @GetMapping("/customer/{customerId}")
    public List<CarResponse> getCarsByCustomer(@PathVariable String customerId) {
        return carService.getCarsByCustomerId(customerId);
    }

    @GetMapping
    public List<CarResponse> getCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public CarResponse getCar(@PathVariable String id) {
        return carService.getCarById(id);
    }

    @PostMapping
    public CarResponse createCar(@RequestBody CarRequest request) {
        return carService.createCar(request);
    }

    @PutMapping("/{id}")
    public CarResponse updateCar(@PathVariable String id, @RequestBody CarRequest request) {
        return carService.updateCar(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable String id) {
        carService.deleteCar(id);
    }
}
