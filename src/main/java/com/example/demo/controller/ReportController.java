package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ReportRequest;
import com.example.demo.dto.ReportResponse;
import com.example.demo.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Report API", description = "Thống kê dịch vụ, phụ tùng và doanh thu")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // GET REPORT
    @Operation(summary = "Thống kê doanh thu theo khoảng thời gian")
    @GetMapping
    public ApiResponse<ReportResponse> getReport(@ModelAttribute ReportRequest request) {
        try {
            // Nếu client không truyền ngày => mặc định 30 ngày gần nhất
            if (request.getStartDate() == null || request.getEndDate() == null) {
                LocalDate end = LocalDate.now();
                LocalDate start = end.minusDays(30);

                // Chuyển LocalDate sang String format yyyy-MM-dd
                request.setStartDate(start);
                request.setEndDate(end);
            }

            ReportResponse response = reportService.getReport(request);
            return new ApiResponse<>("Thành công", response);

        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>("Lỗi tham số: " + ex.getMessage(), null);
        } catch (RuntimeException ex) {
            return new ApiResponse<>("Lỗi nghiệp vụ: " + ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null);
        }
    }

    // MONTHLY DETAIL
    @Operation(summary = "Chi tiết hóa đơn theo tháng (yyyy-MM)")
    @GetMapping("/monthly-revenue/{month}")
    public ApiResponse<List<ReportResponse.MonthlyDetail>> monthlyRevenueDetail(@PathVariable String month) {
        try {
            // Kiểm tra định dạng yyyy-MM
            if (!month.matches("\\d{4}-\\d{2}")) {
                throw new IllegalArgumentException("Tháng không hợp lệ, format đúng là yyyy-MM");
            }

            String[] parts = month.split("-");
            int year = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);

            if (m < 1 || m > 12) {
                throw new IllegalArgumentException("Tháng phải nằm trong khoảng 1-12");
            }

            List<ReportResponse.MonthlyDetail> details = reportService.getMonthlyRevenueDetail(year, m);
            return new ApiResponse<>("Thành công", details);

        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>("Lỗi tham số: " + ex.getMessage(), null);
        } catch (RuntimeException ex) {
            return new ApiResponse<>("Lỗi nghiệp vụ: " + ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>("Lỗi hệ thống: " + ex.getMessage(), null);
        }
    }
}
