package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CustomerRepository;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    private static final String PLATE_REGEX = "^[0-9]{2}[A-Z]{1,2}-[0-9]{4,5}$";

    public CarService(CarRepository carRepository, CustomerRepository customerRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    // Lấy tất cả xe
    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Lấy xe theo ID
    public CarResponse getCarById(String id) {
        return carRepository.findById(id)
                .map(this::convertToResponse)
                .orElse(null);
    }

    // Lấy xe theo customerId
    public List<CarResponse> getCarsByCustomerId(String customerId) {
        return carRepository.findAll().stream()
                .filter(car -> customerId.equals(car.getCustomerId()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Thêm xe mới 
    public CarResponse createCar(CarRequest request) {
        validateCarRequest(request);

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));

        Car car = new Car();
        car.setPlate(request.getPlate());
        car.setModel(request.getModel());
        car.setManufacturer(request.getManufacturer());
        car.setDescription(request.getDescription());
        car.setCustomerId(customer.getId());
        car.setCustomerCode(customer.getCustomerCode());
        car.setActive(request.getActive() != null ? request.getActive() : false); // mặc định false

        return convertToResponse(carRepository.save(car));
    }

    // Cập nhật xe
    public CarResponse updateCar(String id, CarRequest request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + id));

        validateCarRequest(request);

        if (!car.getPlate().equals(request.getPlate()) && carRepository.existsByPlate(request.getPlate())) {
            throw new RuntimeException("Biển số xe đã tồn tại!");
        }

        car.setPlate(request.getPlate());
        car.setModel(request.getModel());
        car.setManufacturer(request.getManufacturer());
        car.setDescription(request.getDescription());

        // Nếu đổi customer thì cập nhật lại customerCode
        if (request.getCustomerId() != null && !request.getCustomerId().equals(car.getCustomerId())) {
            Customer newCustomer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));
            car.setCustomerId(newCustomer.getId());
            car.setCustomerCode(newCustomer.getCustomerCode());
        }

        // Cập nhật trạng thái active nếu có
        if (request.getActive() != null) {
            car.setActive(request.getActive());
        }

        return convertToResponse(carRepository.save(car));
    }

    // Xóa xe
    public void deleteCar(String id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy xe để xóa với ID: " + id);
        }
        carRepository.deleteById(id);
    }

    // Kiểm tra biển số hợp lệ, ID khách hàng
    private void validateCarRequest(CarRequest request) {
        if (request.getCustomerId() == null || request.getCustomerId().isEmpty()) {
            throw new RuntimeException("Thiếu customerId khi tạo xe!");
        }
        if (!request.getPlate().matches(PLATE_REGEX)) {
            throw new RuntimeException("Biển số xe không hợp lệ! (VD: 30A-12345)");
        }
        if (carRepository.existsByPlate(request.getPlate())) {
            throw new RuntimeException("Biển số xe đã tồn tại!");
        }
    }

    // Tìm xe theo biển số theo customerCode
    public List<CarResponse> searchCarsByPlateAndCustomerCode(String plate, String customerCode) {
        return carRepository.findAll().stream()
                .filter(car -> car.getCustomerCode() != null && car.getCustomerCode().equalsIgnoreCase(customerCode.trim()))
                .filter(car -> car.getPlate() != null && car.getPlate().equalsIgnoreCase(plate.trim()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
        return res;
    }
}
