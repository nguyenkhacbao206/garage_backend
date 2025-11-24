package com.example.demo.dto;

import java.math.BigDecimal;

public class ImportItemRequest {

    private String importInvoiceId; // Id invoice liên kết
    private String partId;          // Id part
    private Integer quantity;       // số lượng nhập
    private BigDecimal unitPrice;   // giá nhập
    private BigDecimal sellPrice;   // giá bán

    public ImportItemRequest() {}

    public ImportItemRequest(String importInvoiceId, String partId,Integer quantity, BigDecimal unitPrice, BigDecimal sellPrice) {
        this.importInvoiceId = importInvoiceId;
        this.partId = partId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sellPrice = sellPrice;
    }

    // Getters & Setters
    public String getImportInvoiceId() { return importInvoiceId; }
    public void setImportInvoiceId(String importInvoiceId) { this.importInvoiceId = importInvoiceId; }

    public String getPartId() { return partId; }
    public void setPartId(String partId) { this.partId = partId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getSellPrice() { return sellPrice; }
    public void setSellPrice(BigDecimal sellPrice) { this.sellPrice = sellPrice; }
}
