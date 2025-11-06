package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    @Indexed(unique = true)
    private String customerCode;

    private String name;

    @Indexed(unique = true)
    private String phone;

    @Indexed(unique = true)
    private String email;

    private String address;
    private String note;

    private List<Car> cars = new ArrayList<>();

    public static class Car {
        private String id;
        private String plate;
        private String model;
        private String manufacturer;
        private String description;

        // getters / setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getPlate() { return plate; }
        public void setPlate(String plate) { this.plate = plate; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public List<Car> getCars() { return cars; }
    public void setCars(List<Car> cars) { this.cars = cars; }
}
