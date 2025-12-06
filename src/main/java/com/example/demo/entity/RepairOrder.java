package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "repair_orders")
public class RepairOrder {

    @Id
    private String id;
    private String orderCode;

    // customer
    private String customerId;
    private Object customer;

    // car
    private String carId;
    private Object car;
    private CarSnapshot carSnapshot;  //  lưu snapshot xe

    // technicians
    private List<String> technicianIds;
    private List<Object> technician;

    private String note;
    private List<RepairOrderItem> parts = new ArrayList<>();
    private List<String> serviceIds;
    private List<RepairOrderItem> service = new ArrayList<>();

    private BigDecimal estimatedTotal = BigDecimal.ZERO;
    private BigDecimal serviceFee = BigDecimal.ZERO;
    private String status; // PENDING, IN_PROGRESS, COMPLETED, PAID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateReceived;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateReturned;

    public RepairOrder() {}

   
    public static class CarSnapshot {
        private String id;
        private String plate;
        private String model;
        private String manufacturer;
        private String customerId;

        public CarSnapshot() {}

        public CarSnapshot(String id, String plate, String model, String manufacturer, String customerId) {
            this.id = id;
            this.plate = plate;
            this.model = model;
            this.manufacturer = manufacturer;
            this.customerId = customerId;
        }

        // getters & setters
        public String getId() { 
            return id;
        }
        public void setId(String id) { 
            this.id = id;
        }
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
        public String getCustomerId() { 
            return customerId;
        }
        public void setCustomerId(String customerId) { 
            this.customerId = customerId;
        }
   
    }

   
    public String getId() { 
        return id;
    }
    public void setId(String id) { 
        this.id = id;
    }

    public String getOrderCode() { 
        return orderCode;
    }
    public void setOrderCode(String orderCode) { 
        this.orderCode = orderCode;
    }

    public String getCustomerId() { 
        return customerId;
    }
    public void setCustomerId(String customerId) { 
        this.customerId = customerId;
    }

    public Object getCustomer() { 
        return customer;
    }
    public void setCustomer(Object customer) { 
        this.customer = customer;
    }

    public String getCarId() { 
        return carId;
    }
    public void setCarId(String carId) { 
        this.carId = carId;
    }

    public Object getCar() { 
        return car;
    }
    public void setCar(Object car) { 
        this.car = car;
    }

    public CarSnapshot getCarSnapshot() { 
        return carSnapshot;
    }
    public void setCarSnapshot(CarSnapshot carSnapshot) { 
        this.carSnapshot = carSnapshot;
    }

    public List<String> getTechnicianIds() { 
        return technicianIds;
    }
    public void setTechnicianIds(List<String> technicianIds) { 
        this.technicianIds = technicianIds;
    }

    public List<Object> getTechnician() { 
        return technician;
    }
    public void setTechnician(List<Object> technician) { 
        this.technician = technician;
    }

    public String getNote() { 
        return note;
    }
    public void setNote(String note) { 
        this.note = note;
    }

    public List<RepairOrderItem> getParts() { 
        return parts;
    }
    public void setParts(List<RepairOrderItem> parts) { 
        this.parts = parts;
    }

    public List<String> getServiceIds() { 
        return serviceIds;
    }
    public void setServiceIds(List<String> serviceIds) { 
        this.serviceIds = serviceIds;
    }

    public List<RepairOrderItem> getService() { 
        return service;
    }
    public void setService(List<RepairOrderItem> service) { 
        this.service = service;
    }

    public BigDecimal getEstimatedTotal() { 
        return estimatedTotal;
    }
    public void setEstimatedTotal(BigDecimal estimatedTotal) { 
        this.estimatedTotal = estimatedTotal;
    }

    public BigDecimal getServiceFee() { 
        return serviceFee;
    }
    public void setServiceFee(BigDecimal serviceFee) { 
        this.serviceFee = serviceFee;
    }

    public String getStatus() { 
        return status;
    }
    public void setStatus(String status) { 
        this.status = status;
    }

    public LocalDateTime getDateReceived() { 
        return dateReceived;
    }
    public void setDateReceived(LocalDateTime dateReceived) { 
        this.dateReceived = dateReceived;
    }

    public LocalDateTime getDateReturned() { 
        return dateReturned;
    }
    public void setDateReturned(LocalDateTime dateReturned) { 
        this.dateReturned = dateReturned;
    }

  
    // Calculate total
    
    public BigDecimal calculateEstimatedTotal() {
        BigDecimal partTotal = BigDecimal.ZERO;
        BigDecimal serviceTotal = BigDecimal.ZERO;

        if (parts != null) {
            for (RepairOrderItem it : parts) {
                if (it != null) {
                    if (it.getTotal() == null) it.recalcTotal();
                    partTotal = partTotal.add(it.getTotal());
                }
            }
        }

        if (service != null) {
            for (RepairOrderItem it : service) {
                if (it != null) {
                    if (it.getTotal() == null) it.recalcTotal();
                    serviceTotal = serviceTotal.add(it.getTotal());
                }
            }
        }

        BigDecimal fee = serviceFee != null ? serviceFee : BigDecimal.ZERO;

        BigDecimal total = partTotal.add(serviceTotal).add(fee);
        this.estimatedTotal = total;
        return total;
    }
}
