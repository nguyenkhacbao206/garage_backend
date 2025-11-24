package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "import_items")
public class ImportItem {

    @Id
    private String id;

    private String importInvoiceId;
    private String partId;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal sellPrice;
    private BigDecimal total;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ImportItem() {}

    public ImportItem(String importInvoiceId, String partId, Integer quantity,BigDecimal unitPrice, BigDecimal sellPrice) {
        this.importInvoiceId = importInvoiceId;
        this.partId = partId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sellPrice = sellPrice;
        this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // GETTERS & SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Cập nhật total tự động
    public void recalcTotal() {
        if (unitPrice != null && quantity != null) {
            this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
