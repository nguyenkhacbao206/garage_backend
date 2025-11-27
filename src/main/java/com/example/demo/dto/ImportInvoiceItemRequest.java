package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ImportInvoiceItemRequest {

    @Schema(description = "ID nhóm hóa đơn", example = "INV001")
    @NotBlank
    private String invoiceId;

    @Schema(description = "ID nhà cung cấp", example = "SUP001")
    @NotBlank
    private String supplierId;

    @Schema(description = "Ngày nhập", example = "2025-11-24T10:15:00")
    @NotNull
    private LocalDateTime date;

    @Schema(description = "Danh sách phụ tùng nhập")
    @NotEmpty
    private List<PartInfo> parts;

    public static class PartInfo {
        @Schema(description = "ID linh kiện", example = "P123")
        @NotBlank
        private String partId;

        @Schema(description = "Số lượng", example = "20")
        @NotNull
        @Min(1)
        private Integer quantity;

        @Schema(description = "Đơn giá", example = "150000")
        @NotNull
        @DecimalMin("0.0")
        private BigDecimal unitPrice;

        // GET/SET
        public String getPartId() { return partId; }
        public void setPartId(String partId) { this.partId = partId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }

    // GET/SET cho Request
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public List<PartInfo> getParts() { return parts; }
    public void setParts(List<PartInfo> parts) { this.parts = parts; }
}
