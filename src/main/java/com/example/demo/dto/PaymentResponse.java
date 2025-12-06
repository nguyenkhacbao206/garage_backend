package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.PaymentHistoryItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Thông tin thanh toán trả về cho client")
public class PaymentResponse {

    @Schema(example = "675abc123def456")
    private String id;

    @Schema(example = "674aaa222bbb111")
    private String repairOrderId;

    @Schema(description = "Chi tiết Repair Order đầy đủ")
    private RepairOrderResponse repairOrder;

    @Schema(example = "1500000")
    private BigDecimal amount;

    @Schema(example = "CASH")
    private String method;

    @Schema(example = "PAID")
    private String status;

    @Schema(example = "2025-12-06T06:30:41.066Z")
    private LocalDateTime createdAt;

    @Schema(example = "2025-12-06T06:30:41.066Z")
    private LocalDateTime updatedAt;

    @Schema(description = "Lịch sử cập nhật trạng thái thanh toán")
    private List<PaymentHistoryItem> history;

    public PaymentResponse() {}

    public PaymentResponse(String id, String repairOrderId,
                           RepairOrderResponse repairOrder,
                           BigDecimal amount, String method,
                           String status, LocalDateTime createdAt,
                           LocalDateTime updatedAt,
                           List<PaymentHistoryItem> history) {

        this.id = id;
        this.repairOrderId = repairOrderId;
        this.repairOrder = repairOrder;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.history = history;
    }

    // GETTERS & SETTERS
    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

    public String getRepairOrderId() { 
        return repairOrderId; 
    }
    public void setRepairOrderId(String repairOrderId) { 
        this.repairOrderId = repairOrderId; 
    }

    public RepairOrderResponse getRepairOrder() { 
        return repairOrder; 
    }
    public void setRepairOrder(RepairOrderResponse repairOrder) { 
        this.repairOrder = repairOrder; 
    }

    public BigDecimal getAmount() { 
        return amount; 
    }
    public void setAmount(BigDecimal amount) { 
        this.amount = amount; 
    }

    public String getMethod() { 
        return method; 
    }
    public void setMethod(String method) { 
        this.method = method; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }

    public List<PaymentHistoryItem> getHistory() { 
        return history; 
    }
    public void setHistory(List<PaymentHistoryItem> history) { 
        this.history = history; 
    }
}
