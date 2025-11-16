package com.example.demo.service;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    // Get all customers or filter by name
    public List<Customer> getAll(String name) {
        if (name != null && !name.isEmpty()) {
            return customerRepository.findByNameContainingIgnoreCase(name);
        }
        return customerRepository.findAll();
    }

    // Search customers
    public List<Customer> searchCustomers(String input) {
        if (input == null || input.trim().isEmpty()) {
            return customerRepository.findAll();
        }

        // Tách input thành các token, chỉ giữ chữ và số
        String[] tokens = input.toLowerCase().split("[^a-z0-9À-ỹ]+");

        List<Customer> all = customerRepository.findAll();
        List<Customer> matched = new ArrayList<>();

        for (Customer c : all) {
            String code = c.getCustomerCode() != null ? c.getCustomerCode().toLowerCase() : "";
            String name = c.getName() != null ? c.getName().toLowerCase() : "";
            String phone = c.getPhone() != null ? c.getPhone().toLowerCase() : "";
            String email = c.getEmail() != null ? c.getEmail().toLowerCase() : "";

            for (String token : tokens) {
                if (token.isEmpty()) continue;

                if (code.contains(token) || name.contains(token) || phone.contains(token) || email.contains(token)) {
                    matched.add(c);
                    break;
                }
            }
        }

        return matched;
    }

    // Get customer by ID
    public Optional<Customer> getById(String id) {
        return customerRepository.findById(id);
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

    // Create a new customer
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

        return customerRepository.save(c);
    }

    // Update an existing customer
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

            customerRepository.save(c);
            return Optional.of(c);
        }
        return Optional.empty();
    }

    // Delete a customer by ID
    public boolean delete(String id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Add a car to a customer
    public Optional<Car> addCar(String customerId, Car car) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            car.setId(UUID.randomUUID().toString());
            customer.getCars().add(car);
            customerRepository.save(customer);
            return Optional.of(car);
        }
        return Optional.empty();
    }
}
