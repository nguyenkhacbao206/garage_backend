package com.example.demo.service;

import com.example.demo.dto.ReportRequest;
import com.example.demo.dto.ReportResponse;
import com.example.demo.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepo;

    public ReportResponse getReport(ReportRequest request) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        ReportResponse response = new ReportResponse();

        // Thống kê dịch vụ
        List<ReportResponse.ImportServiceResponse> services =
                reportRepo.revenueByService(start, end);
        response.setServices(services);

        // Thống kê phụ tùng
        List<ReportResponse.ImportPartResponse> parts =
                reportRepo.revenueByPart(start, end);
        response.setParts(parts);

        // Tổng doanh thu 12 tháng gần nhất
        List<ReportResponse.MonthlySummary> monthlySummary = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 11; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            BigDecimal total = reportRepo.sumRevenueBetween(monthStart, monthEnd);

            ReportResponse.MonthlySummary summary = new ReportResponse.MonthlySummary();
            summary.setMonth(monthStart.toString().substring(0, 7)); // yyyy-MM
            summary.setTotalRevenue(total);

            monthlySummary.add(summary);
        }
        response.setMonthlyRevenueSummary(monthlySummary);

        // Chi tiết hóa đơn theo khoảng
        List<ReportResponse.MonthlyDetail> monthlyDetail =
                reportRepo.findInvoicesBetween(start, end);
        response.setMonthlyRevenueDetail(monthlyDetail);

        return response;
    }

    public List<ReportResponse.MonthlyDetail> getMonthlyRevenueDetail(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return reportRepo.findInvoicesBetween(start, end);
    }
}
