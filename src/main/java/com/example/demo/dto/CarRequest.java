package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CarRequest {
    @Schema(description = "Biển số xe", example = "29A-12345")
    private String plate;

    @Schema(description = "Dòng xe / Model", example = "Vios 2020")
    private String model;

    @Schema(description = "Hãng sản xuất", example = "Toyota")
    private String manufacturer;

    @Schema(description = "Mô tả thêm về xe", example = "Xe mới bảo dưỡng, màu trắng")
    private String description;

     @Schema(description = "ID khách hàng (MongoDB ObjectId)", example = "6712abf3c9834a23b6cd9012")
    private String customerId; 

    @Schema(description = "Trạng thái xe", example = "true")
    private Boolean active;    

    public CarRequest() {}

    public String getPlate() { 
        return plate; 
    }
    public void setPlate(String plate) { 
        this.plate = plate; 
    }

    public String getModel() { 
        return model; 
    }
    public void setModel(String model) { 
        this.model = model; 
    }

    public String getManufacturer() { 
        return manufacturer; 
    }
    public void setManufacturer(String manufacturer) { 
        this.manufacturer = manufacturer; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }

    public String getCustomerId() { 
        return customerId; 
    }
    public void setCustomerId(String customerId) { 
        this.customerId = customerId; 
    }

    public Boolean getActive() { 
        return active; 
    }
    public void setActive(Boolean active) { 
        this.active = active; 
    }
}
