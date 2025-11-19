package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "import_invoices")
public class ImportInvoice {

    @Id
    private String id;

    private String supplierId;   // Không dùng JPA relation

    private LocalDateTime date;

    private BigDecimal total;

    private List<String> itemIds; // Hoặc lưu list object tùy bạn

    public ImportInvoice() {}

    public ImportInvoice(
            String id,
            String supplierId,
            LocalDateTime date,
            BigDecimal total,
            List<String> itemIds
    ) {
        this.id = id;
        this.supplierId = supplierId;
        this.date = date;
        this.total = total;
        this.itemIds = itemIds;
    }

    // GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }
}
