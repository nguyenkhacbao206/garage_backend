package com.example.demo.dto;

import java.math.BigDecimal;

public class ImportInvoiceRequest {

    private String supplierId;
    private String date;
    private BigDecimal total;

    public ImportInvoiceRequest() {}

    public ImportInvoiceRequest(String supplierId, String date, BigDecimal total) {
        this.supplierId = supplierId;
        this.date = date;
        this.total = total;
    }

    // Getters and Setters

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
