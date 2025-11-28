package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "repair_order_items")
public class RepairOrderItem {

    @Id
    private String id;

    private String repairOrderId;   
    private LocalDateTime createdAt; 

    private String name;          
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal total;

    public RepairOrderItem() {}

    // getters & setters
    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

    public String getRepairOrderId() { 
        return repairOrderId; 
    }
    public void setRepairOrderId(String repairOrderId) { 
        this.repairOrderId = repairOrderId; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public BigDecimal getUnitPrice() { 
        return unitPrice; 
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        recalcTotal();
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        recalcTotal();
    }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    // tính tổng = unitPrice * quantity
    public void recalcTotal() {
        if (unitPrice != null && quantity != null) {
            this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
