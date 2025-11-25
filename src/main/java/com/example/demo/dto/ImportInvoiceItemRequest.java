package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ImportInvoiceItemRequest {

    @Schema(description = "ID nhóm hóa đơn", example = "INV001")
    @NotBlank
    private String invoiceId;

    @Schema(description = "ID nhà cung cấp", example = "SUP001")
    @NotBlank
    private String supplierId;

    @Schema(description = "ID linh kiện", example = "P123")
    @NotBlank
    private String partId;

    @Schema(description = "Ngày nhập", example = "2025-11-24T10:15:00")
    @NotNull
    private LocalDateTime date;

    @Schema(description = "Số lượng", example = "20")
    @NotNull
    @Min(1)
    private Integer quantity;

    @Schema(description = "Đơn giá", example = "150000")
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal unitPrice;

    public ImportInvoiceItemRequest() {}

    // Getters & Setters
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getPartId() { return partId; }
    public void setPartId(String partId) { this.partId = partId; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
