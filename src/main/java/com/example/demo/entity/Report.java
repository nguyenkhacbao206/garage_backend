package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    private String customerName;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    private List<ServiceInfo> services;
    private List<PartInfo> parts;


    // SERVICE
    public static class ServiceInfo {
        private String id;
        private String name;
        private BigDecimal price;
        private Integer quantity;

        public ServiceInfo() {}

        public ServiceInfo(String id, String name, BigDecimal price, Integer quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        // getter & setter
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }


    // PART
    public static class PartInfo {
        private String id;
        private String name;
        private BigDecimal totalPrice;
        private Integer quantity;

        public PartInfo() {}

        public PartInfo(String id, String name, BigDecimal totalPrice, Integer quantity) {
            this.id = id;
            this.name = name;
            this.totalPrice = totalPrice;
            this.quantity = quantity;
        }

        // getter & setter
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class TopUserStatistic {
    private String userId;
    private String userName; // thêm tên khách hàng
    private String userCode; // thêm mã khách hàng
    private int totalServices;
    private int totalParts;
    private BigDecimal totalSpent;

    // getters & setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserCode() { return userCode; }
    public void setUserCode(String userCode) { this.userCode = userCode; }

    public int getTotalServices() { return totalServices; }
    public void setTotalServices(int totalServices) { this.totalServices = totalServices; }

    public int getTotalParts() { return totalParts; }
    public void setTotalParts(int totalParts) { this.totalParts = totalParts; }

    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }
}


    // REPORT FIELDS
    public Report() {}

    public Report(String customerName, BigDecimal totalAmount, LocalDateTime createdAt) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ServiceInfo> getServices() { return services; }
    public void setServices(List<ServiceInfo> services) { this.services = services; }

    public List<PartInfo> getParts() { return parts; }
    public void setParts(List<PartInfo> parts) { this.parts = parts; }
}
