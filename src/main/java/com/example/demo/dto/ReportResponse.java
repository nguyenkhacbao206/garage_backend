package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReportResponse {

    private List<ImportPartResponse> parts;
    private List<ImportServiceResponse> services;
    private List<MonthlySummary> monthlyRevenueSummary;
    private List<MonthlyDetail> monthlyRevenueDetail;

    private ImportServiceResponse mostUsedService;
    private ImportPartResponse mostUsedPart;
    // PART RESPONSE
    public static class ImportPartResponse {
        private String partId;
        private String partName;
        private BigDecimal totalRevenue;
        private BigDecimal averageRevenue;
        private BigDecimal maxRevenue;
        private BigDecimal minRevenue;
        private long usageCount;

        // Getter & Setter
        public String getPartId() { return partId; }
        public void setPartId(String partId) { this.partId = partId; }

        public String getPartName() { return partName; }
        public void setPartName(String partName) { this.partName = partName; }

        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

        public BigDecimal getAverageRevenue() { return averageRevenue; }
        public void setAverageRevenue(BigDecimal averageRevenue) { this.averageRevenue = averageRevenue; }

        public BigDecimal getMaxRevenue() { return maxRevenue; }
        public void setMaxRevenue(BigDecimal maxRevenue) { this.maxRevenue = maxRevenue; }

        public BigDecimal getMinRevenue() { return minRevenue; }
        public void setMinRevenue(BigDecimal minRevenue) { this.minRevenue = minRevenue; }

        public long getUsageCount() { return usageCount; }
        public void setUsageCount(long usageCount) { this.usageCount = usageCount; }
    }

    // SERVICE RESPONSE
    public static class ImportServiceResponse {
        private String serviceId;
        private String serviceName;
        private BigDecimal totalRevenue;
        private BigDecimal averageRevenue;
        private BigDecimal maxRevenue;
        private BigDecimal minRevenue;
        private long usageCount;

        // Getter & Setter
        public String getServiceId() { return serviceId; }
        public void setServiceId(String serviceId) { this.serviceId = serviceId; }

        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }

        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

        public BigDecimal getAverageRevenue() { return averageRevenue; }
        public void setAverageRevenue(BigDecimal averageRevenue) { this.averageRevenue = averageRevenue; }

        public BigDecimal getMaxRevenue() { return maxRevenue; }
        public void setMaxRevenue(BigDecimal maxRevenue) { this.maxRevenue = maxRevenue; }

        public BigDecimal getMinRevenue() { return minRevenue; }
        public void setMinRevenue(BigDecimal minRevenue) { this.minRevenue = minRevenue; }

        public long getUsageCount() { return usageCount; }
        public void setUsageCount(long usageCount) { this.usageCount = usageCount; }
    }

    // MONTHLY SUMMARY
    public static class MonthlySummary {
        private String month; // yyyy-MM
        private BigDecimal totalRevenue;

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }

        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    }

    // MONTHLY DETAIL
    public static class MonthlyDetail {
        private String paymentId;
        private String repairOrderId;
        private BigDecimal amount;
        private LocalDateTime createdAt;

        public String getPaymentId() { return paymentId; }
        public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

        public String getRepairOrderId() { return repairOrderId; }
        public void setRepairOrderId(String repairOrderId) { this.repairOrderId = repairOrderId; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    // GETTER & SETTER
    public List<ImportPartResponse> getParts() { return parts; }
    public void setParts(List<ImportPartResponse> parts) { this.parts = parts; }

    public List<ImportServiceResponse> getServices() { return services; }
    public void setServices(List<ImportServiceResponse> services) { this.services = services; }

    public List<MonthlySummary> getMonthlyRevenueSummary() { return monthlyRevenueSummary; }
    public void setMonthlyRevenueSummary(List<MonthlySummary> monthlyRevenueSummary) { this.monthlyRevenueSummary = monthlyRevenueSummary; }

    public List<MonthlyDetail> getMonthlyRevenueDetail() { return monthlyRevenueDetail; }
    public void setMonthlyRevenueDetail(List<MonthlyDetail> monthlyRevenueDetail) { this.monthlyRevenueDetail = monthlyRevenueDetail; }

    public ImportServiceResponse getMostUsedService() { return mostUsedService; }
    public void setMostUsedService(ImportServiceResponse mostUsedService) { this.mostUsedService = mostUsedService; }

    public ImportPartResponse getMostUsedPart() { return mostUsedPart; }
    public void setMostUsedPart(ImportPartResponse mostUsedPart) { this.mostUsedPart = mostUsedPart; }
}
