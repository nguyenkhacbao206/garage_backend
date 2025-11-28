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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ReportRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    // SERVICE
    public List<ReportResponse.ImportServiceResponse> revenueByService(LocalDate start, LocalDate end) {

        MatchOperation match = Aggregation.match(
                Criteria.where("createdAt")
                        .gte(start.atStartOfDay())
                        .lte(end.atTime(23, 59, 59))
        );

        UnwindOperation unwind = Aggregation.unwind("services");

        GroupOperation group = Aggregation.group("services.id", "services.name")
                .count().as("usageCount")
                .sum("services.price").as("totalRevenue")
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
                mongoTemplate.aggregate(aggregation, "invoices", Map.class).getMappedResults();

        return results.stream().map(map -> {
            ReportResponse.ImportServiceResponse response =
                    new ReportResponse.ImportServiceResponse();

            response.setServiceId((String) map.get("serviceId"));
            response.setServiceName((String) map.get("serviceName"));
            response.setUsageCount(Long.parseLong(map.get("usageCount").toString()));
            response.setTotalRevenue(new BigDecimal(map.get("totalRevenue").toString()));
            response.setAverageRevenue(new BigDecimal(map.get("averageRevenue").toString()));
            response.setMaxRevenue(new BigDecimal(map.get("maxRevenue").toString()));
            response.setMinRevenue(new BigDecimal(map.get("minRevenue").toString()));
            return response;
        }).collect(Collectors.toList());
    }

    // PART
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
                mongoTemplate.aggregate(aggregation, "invoices", Map.class).getMappedResults();

        return results.stream().map(map -> {
            ReportResponse.ImportPartResponse response =
                    new ReportResponse.ImportPartResponse();

            response.setPartId((String) map.get("partId"));
            response.setPartName((String) map.get("partName"));
            response.setUsageCount(Long.parseLong(map.get("usageCount").toString()));
            response.setTotalRevenue(new BigDecimal(map.get("totalRevenue").toString()));
            response.setAverageRevenue(new BigDecimal(map.get("averageRevenue").toString()));
            response.setMaxRevenue(new BigDecimal(map.get("maxRevenue").toString()));
            response.setMinRevenue(new BigDecimal(map.get("minRevenue").toString()));
            return response;
        }).collect(Collectors.toList());
    }


    // TOTAL
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
                mongoTemplate.aggregate(aggregation, "invoices", Map.class);

        if (result.getMappedResults().isEmpty()) return BigDecimal.ZERO;

        return new BigDecimal(result.getMappedResults().get(0).get("totalRevenue").toString());
    }

    // MONTHLY DETAIL
    public List<ReportResponse.MonthlyDetail> findInvoicesBetween(LocalDate start, LocalDate end) {

        Query query = new Query();
        query.addCriteria(
                Criteria.where("createdAt")
                        .gte(start.atStartOfDay())
                        .lte(end.atTime(23, 59, 59))
        );

        query.with(Sort.by(Sort.Direction.ASC, "createdAt"));

        List<Map> results =
                mongoTemplate.find(query, Map.class, "invoices");

        return results.stream().map(map -> {
            ReportResponse.MonthlyDetail detail =
                    new ReportResponse.MonthlyDetail();

            detail.setPaymentId((String) map.get("id"));
            detail.setRepairOrderId((String) map.get("repairOrderId"));
            detail.setAmount(new BigDecimal(map.get("totalAmount").toString()));

            Object dateObj = map.get("createdAt");
            if (dateObj instanceof Date date) {
                detail.setCreatedAt(
                        date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                );
            }

            return detail;
        }).collect(Collectors.toList());
    }
}
