package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "payments")
@Schema(description = "Thông tin thanh toán")
public class Payment {

    @Id
    @Schema(description = "ID của payment (Mongo ObjectId)", example = "653b1e8f3a2b4c0012a3b4c5")
    private String id;

    @Schema(description = "ID của RepairOrder (tham chiếu)", example = "652a1a2b3c4d5e6f7a8b9c0d")
    private String repairOrderId;

    // @Schema(description = "ID của cashier/user thực hiện thu tiền", example = "67123abc9087bcff1234aa12")
    // private String cashierId;

    @Schema(description = "Số tiền thanh toán", example = "150000.00")
    private BigDecimal amount;

    @Schema(description = "Phương thức thanh toán (COD, CASH, VNPAY, MOMO, BANK_TRANSFER, ...)", example = "CASH")
    private String method;

    @Schema(description = "Trạng thái thanh toán (PENDING, SUCCESS, FAILED)", example = "SUCCESS")
    private String status;

    @Schema(description = "Thời gian tạo")
    private LocalDateTime createdAt;

    @Schema(description = "Thời gian cập nhật")
    private LocalDateTime updatedAt;

    public Payment() {}

    // GETTERS / SETTERS 
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

    // public String getCashierId() { 
    //     return cashierId; 
    // }
    // public void setCashierId(String cashierId) { 
    //     this.cashierId = cashierId; 
    // }

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
}
