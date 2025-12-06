package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CustomerRepository;

import java.util.Comparator;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    private static final String PLATE_REGEX = "^[0-9]{2}[A-Z]{1,2}-[0-9]{4,5}$";

    public CarService(CarRepository carRepository, CustomerRepository customerRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }
    
    // sort by createdAt
    public List<CarResponse> sortByCreatedAt(List<CarResponse> Cars, boolean asc) {

        Comparator<CarResponse> comp = Comparator.comparing(
                CarResponse::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder())
        );

        if (!asc) {
            comp = comp.reversed();
        }

        Cars.sort(comp);
        return Cars
        ;
    }

    // Search cars
    public List<CarResponse> searchCars(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCars();
        }

        String search = keyword.trim().toLowerCase();

        return carRepository.findAll().stream()
                .filter(car ->
                        (car.getPlate() != null && car.getPlate().toLowerCase().contains(search)) ||
                        (car.getModel() != null && car.getModel().toLowerCase().contains(search)) ||
                        (car.getManufacturer() != null && car.getManufacturer().toLowerCase().contains(search)) ||
                        (car.getCustomerCode() != null && car.getCustomerCode().toLowerCase().contains(search))
                )
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // Get all cars
    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get car by ID
    public CarResponse getCarById(String id) {
        return carRepository.findById(id)
                .map(this::convertToResponse)
                .orElse(null);
    }

    // Get cars by customerId
    public List<CarResponse> getCarsByCustomerId(String customerId) {
        return carRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Create car
    public CarResponse createCar(CarRequest request) {
        validateCarRequest(request);

        // Kiểm tra biển số xe đã tồn tại chưa
        if (carRepository.existsByPlate(request.getPlate())) {
            throw new RuntimeException("Biển số xe đã tồn tại!");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));

        Car car = new Car();
        car.setPlate(request.getPlate());
        car.setModel(request.getModel());
        car.setManufacturer(request.getManufacturer());
        car.setDescription(request.getDescription());
        car.setCustomerId(customer.getId());
        car.setCustomerCode(customer.getCustomerCode());
        car.setActive(request.getActive() != null ? request.getActive() : false);
        car.setCreatedAt(LocalDateTime.now());
        car.setUpdatedAt(LocalDateTime.now());

        return convertToResponse(carRepository.save(car));
    }


    // Update car
    public CarResponse updateCar(String id, CarRequest request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + id));

        // Cập nhật từng trường nếu request có gửi dữ liệu
        if (request.getPlate() != null) {
            car.setPlate(request.getPlate());
        }

        if (request.getModel() != null) {
            car.setModel(request.getModel());
        }

        if (request.getManufacturer() != null) {
            car.setManufacturer(request.getManufacturer());
        }

        if (request.getDescription() != null) {
            car.setDescription(request.getDescription());
        }

        // Update customer nếu có gửi customerId mới
        if (request.getCustomerId() != null && !request.getCustomerId().equals(car.getCustomerId())) {
            Customer newCustomer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));
            car.setCustomerId(newCustomer.getId());
            car.setCustomerCode(newCustomer.getCustomerCode());
        }

        // Update active 
        if (request.getActive() != null) {
            car.setActive(request.getActive());
        }

        car.setUpdatedAt(LocalDateTime.now());

        return convertToResponse(carRepository.save(car));
    }


    // Delete car
    public void deleteCar(String id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy xe để xóa với ID: " + id);
        }
        carRepository.deleteById(id);
    }

    // Validation
    private void validateCarRequest(CarRequest request) {
        if (request.getCustomerId() == null || request.getCustomerId().isEmpty()) {
            throw new RuntimeException("Thiếu customerId khi tạo xe!");
        }
        if (!request.getPlate().matches(PLATE_REGEX)) {
            throw new RuntimeException("Biển số xe không hợp lệ! (VD: 30A-12345)");
        }
    }

    // Convert entity -> DTO
    private CarResponse convertToResponse(Car car) {
        CarResponse res = new CarResponse();
        res.setId(car.getId());
        res.setPlate(car.getPlate());
        res.setModel(car.getModel());
        res.setManufacturer(car.getManufacturer());
        res.setDescription(car.getDescription());
        res.setCustomerId(car.getCustomerId());
        res.setCustomerCode(car.getCustomerCode());
        res.setActive(car.isActive());
        res.setCreatedAt(car.getCreatedAt());
        res.setUpdatedAt(car.getUpdatedAt());
        return res;
    }
}
