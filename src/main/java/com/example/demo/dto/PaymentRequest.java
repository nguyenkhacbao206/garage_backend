package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentRequest {

    @Schema(description = "ID của repair order", example = "652a1a2b3c4d5e6f7a8b9c0d")
    private String repairOrderId;

    @Schema(description = "ID cashier thực hiện thu tiền", example = "67123abc9087bcff1234aa12")
    private String cashierId;

    @Schema(description = "Phương thức thanh toán", example = "CASH")
    private String method;

    public PaymentRequest() {}

    public String getRepairOrderId() { 
        return repairOrderId; 
    }
    public void setRepairOrderId(String repairOrderId) { 
        this.repairOrderId = repairOrderId; 
    }

    public String getCashierId() { 
        return cashierId; 
    }
    public void setCashierId(String cashierId) { 
        this.cashierId = cashierId; 
    }

    public String getMethod() { 
        return method; 
    }
    public void setMethod(String method) { 
        this.method = method; 
    }
}
