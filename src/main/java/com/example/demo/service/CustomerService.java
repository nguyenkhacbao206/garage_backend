package com.example.demo.service;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarRepository carRepository;

    // Lấy tất cả khách hàng, filter theo tên
    public List<Customer> getAll(String name) {
        List<Customer> customers;
        if (name != null && !name.isEmpty()) {
            customers = customerRepository.findByNameContainingIgnoreCase(name);
        } else {
            customers = customerRepository.findAll();
        }

        // Load danh sách xe cho từng customer
        for (Customer c : customers) {
            List<Car> cars = carRepository.findByCustomerId(c.getId());
            c.setCars(cars);
        }

        return customers;
    }
    
    // sort by created decrease, increase
    public List<Customer> sortByCreatedAt(List<Customer> customers, boolean asc) {

        Comparator<Customer> comp = Comparator.comparing(
                Customer::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder())
        );

        if (!asc) {
            comp = comp.reversed();
        }

        customers.sort(comp);
        return customers;
    }


    // Tìm kiếm theo keyword
    public List<Customer> searchCustomers(String input) {
        if (input == null || input.trim().isEmpty()) {
            return getAll(null);
        }

        String[] tokens = input.toLowerCase().split("[^a-z0-9À-ỹ]+");
        List<Customer> all = customerRepository.findAll();
        List<Customer> matched = new ArrayList<>();

        for (Customer c : all) {
            String code = Optional.ofNullable(c.getCustomerCode()).orElse("").toLowerCase();
            String name = Optional.ofNullable(c.getName()).orElse("").toLowerCase();
            String phone = Optional.ofNullable(c.getPhone()).orElse("").toLowerCase();
            String email = Optional.ofNullable(c.getEmail()).orElse("").toLowerCase();

            for (String token : tokens) {
                if (token.isEmpty()) continue;
                if (code.contains(token) || name.contains(token) || phone.contains(token) || email.contains(token)) {
                    // Load danh sách xe
                    List<Car> cars = carRepository.findByCustomerId(c.getId());
                    c.setCars(cars);
                    matched.add(c);
                    break;
                }
            }
        }

        return matched;
    }

    // Lấy customer theo ID kèm danh sách xe
    public Optional<Customer> getByIdWithCars(String id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            List<Car> cars = carRepository.findByCustomerId(customer.getId());
            customer.setCars(cars);
            return Optional.of(customer);
        }
        return Optional.empty();
    }

    private String generateCustomerCode() {
        Customer last = customerRepository.findTopByOrderByCustomerCodeDesc();
        if (last == null || last.getCustomerCode() == null) {
            return "KH-001";
        }
        String lastCode = last.getCustomerCode();
        int number = Integer.parseInt(lastCode.replace("KH-", ""));
        number += 1;
        int digits = Math.max(3, String.valueOf(number).length());
        return String.format("KH-%0" + digits + "d", number);
    }

    // Thêm mới khách hàng
    public Customer create(CustomerRequest request) {
        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already exists!");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        Customer c = new Customer();
        c.setName(request.getName());
        c.setPhone(request.getPhone());
        c.setEmail(request.getEmail());
        c.setAddress(request.getAddress());
        c.setNote(request.getNote());
        c.setCustomerCode(generateCustomerCode());
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());

        Customer saved = customerRepository.save(c);
        saved.setCars(new ArrayList<>());
        return saved;
    }

    // Cập nhật customer
    public Optional<Customer> update(String id, CustomerRequest request) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isPresent()) {
            Customer c = existing.get();

            if (request.getPhone() != null && !request.getPhone().equals(c.getPhone())
                    && customerRepository.existsByPhone(request.getPhone())) {
                throw new RuntimeException("Phone number already exists!");
            }

            if (request.getEmail() != null && !request.getEmail().equals(c.getEmail())
                    && customerRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists!");
            }

            if (request.getName() != null) c.setName(request.getName());
            if (request.getPhone() != null) c.setPhone(request.getPhone());
            if (request.getEmail() != null) c.setEmail(request.getEmail());
            if (request.getAddress() != null) c.setAddress(request.getAddress());
            if (request.getNote() != null) c.setNote(request.getNote());

            c.setUpdatedAt(LocalDateTime.now());

            Customer updated = customerRepository.save(c);

            // Load danh sách xe
            List<Car> cars = carRepository.findByCustomerId(updated.getId());
            updated.setCars(cars);

            return Optional.of(updated);
        }
        return Optional.empty();
    }

    // Xóa customer
    public boolean delete(String id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Thêm xe mới cho customer
    public Optional<Car> addCar(String customerId, Car car) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        car.setCustomerId(customer.getId());
        car.setCustomerCode(customer.getCustomerCode());
        car.setActive(true);

        Car savedCar = carRepository.save(car);

        // Cập nhật danh sách xe cho customer
        List<Car> cars = carRepository.findByCustomerId(customer.getId());
        customer.setCars(cars);
        customerRepository.save(customer);

        return Optional.of(savedCar);
    }
}
