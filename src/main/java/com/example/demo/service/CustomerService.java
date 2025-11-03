package com.example.demo.service;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // get all customers
    public List<Customer> getAll(String name) {
        if (name != null && !name.isEmpty()) {
            return customerRepository.findByNameContainingIgnoreCase(name);
        }
        return customerRepository.findAll();
    }

    // get customer by id
    public Optional<Customer> getById(String id) {
        return customerRepository.findById(id);
    }

    // create new customer
    public Customer create(CustomerRequest request) {
        Customer c = new Customer();
        c.setName(request.getName());
        c.setPhone(request.getPhone());
        c.setAddress(request.getAddress());
        c.setNote(request.getNote());
        return customerRepository.save(c);
    }

    //  Update existing customer
    public Optional<Customer> update(String id, CustomerRequest request) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isPresent()) {
            Customer c = existing.get();
            if (request.getName() != null) c.setName(request.getName());
            if (request.getPhone() != null) c.setPhone(request.getPhone());
            if (request.getAddress() != null) c.setAddress(request.getAddress());
            if (request.getNote() != null) c.setNote(request.getNote());
            customerRepository.save(c);
            return Optional.of(c);
        }
        return Optional.empty();
    }

    // delete customer by id
    public boolean delete(String id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get all cars belonging to a specific customer 
    public Optional<Customer.Car> addCar(String customerId, Customer.Car car) {
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
