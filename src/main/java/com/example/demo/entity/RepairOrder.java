package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "repair_orders")
public class RepairOrder {

    @Id
    private String id;

    private String orderCode;
    private String status; // PENDING, IN_PROGRESS, PAID, ...
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateReceived;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateReturned;

    private List<RepairOrderItem> items = new ArrayList<>();

    public RepairOrder() {}

    // getters / setters
    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

    public String getOrderCode() { 
        return orderCode; 
    }
    public void setOrderCode(String orderCode) { 
        this.orderCode = orderCode; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public LocalDateTime getDateReceived() { 
        return dateReceived; 
    }
    public void setDateReceived(LocalDateTime dateReceived) { 
        this.dateReceived = dateReceived; 
    }

    public LocalDateTime getDateReturned() { 
        return dateReturned; 
    }
    public void setDateReturned(LocalDateTime dateReturned) { 
        this.dateReturned = dateReturned; 
    }

    public List<RepairOrderItem> getItems() { 
        return items; 
    }
    public void setItems(List<RepairOrderItem> items) { 
        this.items = items; 
    }

    // Tính tổng tiền
    public BigDecimal calculateTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        if (items != null) {
            for (RepairOrderItem it : items) {
                if (it.getTotal() != null) sum = sum.add(it.getTotal());
                else if (it.getUnitPrice() != null && it.getQuantity() != null) {
                    sum = sum.add(it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
                }
            }
        }
        return sum;
    }
}
