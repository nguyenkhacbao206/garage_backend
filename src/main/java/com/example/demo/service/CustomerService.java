package com.example.demo.service;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Get all customers or filter by name
    public List<Customer> getAll(String name) {
        if (name != null && !name.isEmpty()) {
            return customerRepository.findByNameContainingIgnoreCase(name);
        }
        return customerRepository.findAll();
    }

    // Advanced search by name, phone, email
    public List<Customer> searchCustomers(String customerCode , String name, String phone, String email) {
        boolean hasCode = customerCode != null && !customerCode.isEmpty();
        boolean hasName = name != null && !name.isEmpty();
        boolean hasPhone = phone != null && !phone.isEmpty();
        boolean hasEmail = email != null && !email.isEmpty();

        // If no search criteria, return all customers
        if (!hasName && !hasPhone && !hasEmail) {
            return customerRepository.findAll();
        }

        // Search by any of the provided fields
        return customerRepository.findByCustomerCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCase(
            hasCode ? customerCode : "",
            hasName ? name : "",
            hasPhone ? phone : "",
            hasEmail ? email : ""
    );
    }

    // Get customer by ID
    public Optional<Customer> getById(String id) {
        return customerRepository.findById(id);
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

        // Generate a random customer code, ensure uniqueness
        String randomCode;
        do {
            randomCode = String.format("KH-%03d", ThreadLocalRandom.current().nextInt(0, 1000));
        } while (customerRepository.existsByCustomerCode(randomCode));

        c.setCustomerCode(randomCode);

        return customerRepository.save(c);
    }

    // Update an existing customer
    public Optional<Customer> update(String id, CustomerRequest request) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isPresent()) {
            Customer c = existing.get();

            // Validate unique phone
            if (request.getPhone() != null && !request.getPhone().equals(c.getPhone())
                    && customerRepository.existsByPhone(request.getPhone())) {
                throw new RuntimeException("Phone number already exists!");
            }

            // Validate unique email
            if (request.getEmail() != null && !request.getEmail().equals(c.getEmail())
                    && customerRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists!");
            }

            // Update fields if provided
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