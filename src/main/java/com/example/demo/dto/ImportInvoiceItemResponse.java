package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ImportInvoiceItemResponse {

    private String id;
    private String invoiceId;
    private String importInvoiceItemCode;

    private SupplierResponse supplier;
    // private List<ImportPartResponse> parts;

    // (SUPPLIER (NHÚNG TRONG PART))ngược lại
    public static class SupplierResponse {
        private String supplierId;
        private String supplierName;
        private String supplierCode;
        private String supplierAddress;
        private String supplierEmail;
        private String supplierPhone;
        private String supplierDescription;

        private List<ImportPartResponse> parts;

        public String getSupplierId() { return supplierId; }
        public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
        public String getSupplierCode() { return supplierCode; }
        public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
        public String getSupplierAddress() { return supplierAddress; }
        public void setSupplierAddress(String supplierAddress) { this.supplierAddress = supplierAddress; }
        public String getSupplierEmail() { return supplierEmail; }
        public void setSupplierEmail(String supplierEmail) { this.supplierEmail = supplierEmail; }
        public String getSupplierPhone() { return supplierPhone; }
        public void setSupplierPhone(String supplierPhone) { this.supplierPhone = supplierPhone; }
        public String getSupplierDescription() { return supplierDescription; }
        public void setSupplierDescription(String supplierDescription) { this.supplierDescription = supplierDescription; }

        public List<ImportPartResponse> getParts() { return parts; }
        public void setParts(List<ImportPartResponse> partList) { this.parts = partList; }
    }

    // PART
    public static class ImportPartResponse {
        private String partId;
        private String partName;
        private String partCode;
        private Double price;
        private Integer stock;
        private String description;


        public String getPartId() { return partId; }
        public void setPartId(String partId) { this.partId = partId; }
        public String getPartName() { return partName; }
        public void setPartName(String partName) { this.partName = partName; }
        public String getPartCode() { return partCode; }
        public void setPartCode(String partCode) { this.partCode = partCode; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    private ImportPartResponse part;

    // ITEM FIELDS
    private LocalDateTime date;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private BigDecimal invoiceTotal;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // GETTER / SETTER

    // public List<ImportPartResponse> getParts() { return parts; }
    // public void setParts(List<ImportPartResponse> partList) { this.parts = partList; }

    public SupplierResponse getSupplier(){ return supplier;}
    public void setSupplier(SupplierResponse supplier) {this.supplier = supplier;}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    
    public String getImportInvoiceItemCode(){return importInvoiceItemCode;}
    public void setImportInvoiceItemCode(String importInvoiceItemCode) {this.importInvoiceItemCode = importInvoiceItemCode;}


    public String getNote(){return note;}
    public void setNote(String note) {this.note = note;}

    public ImportPartResponse getPart() { return part; }
    public void setPart(ImportPartResponse part) { this.part = part; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getInvoiceTotal() { return invoiceTotal; }
    public void setInvoiceTotal(BigDecimal invoiceTotal) { this.invoiceTotal = invoiceTotal; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}