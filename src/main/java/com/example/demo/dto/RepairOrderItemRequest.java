package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class RepairOrderItemRequest {

    @Schema(description = "Tên dịch vụ hoặc phụ tùng được sử dụng", example = "Thay nhớt động cơ")
    private String name;

    @Schema(description = "Đơn giá của mục sửa chữa", example = "150000")
    private BigDecimal unitPrice;

    @Schema(description = "Số lượng", example = "2")
    private Integer quantity;

    public RepairOrderItemRequest() {}

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
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
