package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public class ReportResponse {

    private String status = "success";
    private Object data;

    public ReportResponse() {}
    public ReportResponse(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    // ===== Nested DTOs =====

    public static class DashboardKPI {
        private Integer id;
        private String type;
        private String title;
        private String value;
        private String subText;
        private Boolean isGrowth;

        // getters/setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getSubText() { return subText; }
        public void setSubText(String subText) { this.subText = subText; }
        public Boolean getIsGrowth() { return isGrowth; }
        public void setIsGrowth(Boolean isGrowth) { this.isGrowth = isGrowth; }
    }

    public static class ServicePartStatistic {
        private String id;
        private Integer rank;
        private String code;
        private String name;
        private Integer quantity;
        private BigDecimal totalRevenue;

        // getters/setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public Integer getRank() { return rank; }
        public void setRank(Integer rank) { this.rank = rank; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    }

    public static class RevenueChartData {
        private String month;
        private BigDecimal value;

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }
        public BigDecimal getValue() { return value; }
        public void setValue(BigDecimal value) { this.value = value; }
    }

    public static class RevenueChart {
        private String title;
        private String subTitle;
        private String currencyUnit;
        private List<RevenueChartData> chartData;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getSubTitle() { return subTitle; }
        public void setSubTitle(String subTitle) { this.subTitle = subTitle; }
        public String getCurrencyUnit() { return currencyUnit; }
        public void setCurrencyUnit(String currencyUnit) { this.currencyUnit = currencyUnit; }
        public List<RevenueChartData> getChartData() { return chartData; }
        public void setChartData(List<RevenueChartData> chartData) { this.chartData = chartData; }
    }
}
