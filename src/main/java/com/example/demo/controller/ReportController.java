package com.example.demo.controller;

import com.example.demo.dto.ReportResponse;
import com.example.demo.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "API thống kê")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard KPI")
    public ResponseEntity<ReportResponse> getDashboardKPI() {
        List<ReportResponse.DashboardKPI> kpis = reportService.getDashboardKPI();
        return ResponseEntity.ok(new ReportResponse(kpis));
    }

    @GetMapping("/statistics")
    @Operation(summary = "Thống kê dịch vụ và phụ tùng")
    public ResponseEntity<ReportResponse> getServicePartStatistics() {
        Map<String, List<ReportResponse.ServicePartStatistic>> stats = reportService.getServicePartStatistics();
        return ResponseEntity.ok(new ReportResponse(stats));
    }
    
    @GetMapping("/daily")
    public ResponseEntity<?> getDailyReport(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
        ) {
        return ResponseEntity.ok(reportService.getDailyReport(date));
    }


    @GetMapping("/monthly-revenue")
    @Operation(summary = "Doanh thu 12 tháng gần nhất")
    public ResponseEntity<ReportResponse> getMonthlyRevenue() {
        ReportResponse.RevenueChart chart = reportService.getMonthlyRevenueChart();
        return ResponseEntity.ok(new ReportResponse(chart));
    }
}
