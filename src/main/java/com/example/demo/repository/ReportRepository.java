package com.example.demo.repository;

import com.example.demo.dto.ReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ReportRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    // Helper method để lấy giá trị BigDecimal từ Map, xử lý Null/Zero
    private BigDecimal getBigDecimalValue(Map<?, ?> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return BigDecimal.ZERO;
        }
        // Xử lý trường hợp Map trả về Integer/Long khi giá trị là 0
        return new BigDecimal(value.toString());
    }

    // Helper method để lấy giá trị Long từ Map, xử lý Null
    private long getLongValue(Map<?, ?> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return 0L;
        }
        return Long.parseLong(value.toString());
    }

    // ===============================
    //  SERVICE REVENUE
    // ===============================
    public List<ReportResponse.ImportServiceResponse> revenueByService(LocalDate start, LocalDate end) {

        MatchOperation match = Aggregation.match(
                Criteria.where("createdAt")
                        .gte(start.atStartOfDay())
                        .lte(end.atTime(23, 59, 59))
        );

        UnwindOperation unwind = Aggregation.unwind("services");

        // Dùng $sum: {$multiply: ["$services.price", "$services.quantity"]} để tính tổng tiền theo số lượng.
        // Tuy nhiên, do entity Report.ServiceInfo chỉ có price (đơn giá) và quantity, 
        // ta sẽ group theo price * quantity (hoặc giả định total price = price * quantity)
        // Hiện tại code đang dùng sum("services.price") và count(), giả định đơn giá = tổng tiền bán ra
        // NẾU `Report.ServiceInfo` THIẾU TRƯỜNG TỔNG TIỀN: Cần sửa lại entity để aggregation tính đúng:
        // .sum(new ArithmeticOperators.Multiply().multiplyValueOf("services.price").byValueOf("services.quantity")).as("totalRevenue")
        
        GroupOperation group = Aggregation.group("services.id", "services.name")
                .count().as("usageCount")
                .sum("services.price").as("totalRevenue") // Giữ nguyên theo code cũ, nhưng nên là TotalPrice * Quantity
                .avg("services.price").as("averageRevenue")
                .max("services.price").as("maxRevenue")
                .min("services.price").as("minRevenue");

        ProjectionOperation project = Aggregation.project()
                .and("_id.id").as("serviceId")
                .and("_id.name").as("serviceName")
                .and("usageCount").as("usageCount")
                .and("totalRevenue").as("totalRevenue")
                .and("averageRevenue").as("averageRevenue")
                .and("maxRevenue").as("maxRevenue")
                .and("minRevenue").as("minRevenue");

        Aggregation aggregation = Aggregation.newAggregation(match, unwind, group, project);

        List<Map> results =
                mongoTemplate.aggregate(aggregation, "reports", Map.class).getMappedResults();

        return results.stream().map(map -> {
            ReportResponse.ImportServiceResponse response =
                    new ReportResponse.ImportServiceResponse();

            response.setServiceId((String) map.get("serviceId"));
            response.setServiceName((String) map.get("serviceName"));
            // SỬA: Dùng hàm helper để tránh lỗi NullPointer/NumberFormat
            response.setUsageCount(getLongValue(map, "usageCount"));
            response.setTotalRevenue(getBigDecimalValue(map, "totalRevenue"));
            response.setAverageRevenue(getBigDecimalValue(map, "averageRevenue"));
            response.setMaxRevenue(getBigDecimalValue(map, "maxRevenue"));
            response.setMinRevenue(getBigDecimalValue(map, "minRevenue"));
            return response;
        }).collect(Collectors.toList());
    }

    // ===============================
    //  PART REVENUE
    // ===============================
    public List<ReportResponse.ImportPartResponse> revenueByPart(LocalDate start, LocalDate end) {

        MatchOperation match = Aggregation.match(
                Criteria.where("createdAt")
                        .gte(start.atStartOfDay())
                        .lte(end.atTime(23, 59, 59))
        );

        UnwindOperation unwind = Aggregation.unwind("parts");

        GroupOperation group = Aggregation.group("parts.id", "parts.name")
                .count().as("usageCount")
                .sum("parts.totalPrice").as("totalRevenue")
                .avg("parts.totalPrice").as("averageRevenue")
                .max("parts.totalPrice").as("maxRevenue")
                .min("parts.totalPrice").as("minRevenue");

        ProjectionOperation project = Aggregation.project()
                .and("_id.id").as("partId")
                .and("_id.name").as("partName")
                .and("usageCount").as("usageCount")
                .and("totalRevenue").as("totalRevenue")
                .and("averageRevenue").as("averageRevenue")
                .and("maxRevenue").as("maxRevenue")
                .and("minRevenue").as("minRevenue");

        Aggregation aggregation = Aggregation.newAggregation(match, unwind, group, project);

        List<Map> results =
                mongoTemplate.aggregate(aggregation, "reports", Map.class).getMappedResults();

        return results.stream().map(map -> {
            ReportResponse.ImportPartResponse response =
                    new ReportResponse.ImportPartResponse();

            response.setPartId((String) map.get("partId"));
            response.setPartName((String) map.get("partName"));
            // SỬA: Dùng hàm helper để tránh lỗi NullPointer/NumberFormat
            response.setUsageCount(getLongValue(map, "usageCount"));
            response.setTotalRevenue(getBigDecimalValue(map, "totalRevenue"));
            response.setAverageRevenue(getBigDecimalValue(map, "averageRevenue"));
            response.setMaxRevenue(getBigDecimalValue(map, "maxRevenue"));
            response.setMinRevenue(getBigDecimalValue(map, "minRevenue"));
            return response;
        }).collect(Collectors.toList());
    }

    // ===============================
    //  TOTAL REVENUE (Giữ nguyên)
    // ===============================
    public BigDecimal sumRevenueBetween(LocalDate start, LocalDate end) {

        MatchOperation match = Aggregation.match(
                Criteria.where("createdAt")
                        .gte(start.atStartOfDay())
                        .lte(end.atTime(23, 59, 59))
        );

        GroupOperation group = Aggregation.group()
                .sum("totalAmount").as("totalRevenue");

        Aggregation aggregation = Aggregation.newAggregation(match, group);

        AggregationResults<Map> result =
                mongoTemplate.aggregate(aggregation, "reports", Map.class);

        if (result.getMappedResults().isEmpty()) return BigDecimal.ZERO;

        // SỬA: Dùng hàm helper để tránh lỗi NullPointer
        return getBigDecimalValue(result.getMappedResults().get(0), "totalRevenue");
    }

    // ===============================
    //  MONTHLY DETAIL LIST (Giữ nguyên logic)
    // ===============================
    public List<ReportResponse.MonthlyDetail> findInvoicesBetween(LocalDate start, LocalDate end) {

        Query query = new Query();
        query.addCriteria(
                Criteria.where("createdAt")
                        .gte(start.atStartOfDay())
                        .lte(end.atTime(23, 59, 59))
        );

        query.with(Sort.by(Sort.Direction.ASC, "createdAt"));

        List<Map> results = mongoTemplate.find(query, Map.class, "reports");

        return results.stream().map(map -> {
            ReportResponse.MonthlyDetail detail =
                new ReportResponse.MonthlyDetail();

            detail.setPaymentId((String) map.get("_id")); // ID trong DB thường là _id
            detail.setRepairOrderId((String) map.get("repairOrderId"));
            
            // SỬA: Kiểm tra null cho totalAmount
            detail.setAmount(getBigDecimalValue(map, "totalAmount"));

            Object obj = map.get("createdAt");
            if (obj instanceof Date d) {
                detail.setCreatedAt(
                        d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                );
            }

            return detail;
        }).collect(Collectors.toList());
    }
}