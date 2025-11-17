package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "repair_order_items")
public class RepairOrderItem {

    @Id
    private String id;

    private String name; // tên service hoặc phụ tùng
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal total;

    public RepairOrderItem() {}

    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
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

    public Integer getQuantity() { 
        return quantity; 
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        recalcTotal();
    }

    public BigDecimal getTotal() { 
        return total; 
    }
    public void setTotal(BigDecimal total) { 
        this.total = total; 
    }

    public void recalcTotal() {
        if (unitPrice != null && quantity != null) {
            this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
