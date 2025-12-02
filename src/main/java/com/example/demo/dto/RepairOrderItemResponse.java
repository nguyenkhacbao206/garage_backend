package com.example.demo.dto;

import java.math.BigDecimal;

public class RepairOrderItemResponse {
    private String id;
    private String name;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal total;

    public RepairOrderItemResponse() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
