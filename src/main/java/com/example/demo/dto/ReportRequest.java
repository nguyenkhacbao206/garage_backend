package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "Request lọc doanh thu theo khoảng thời gian")
public class ReportRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Ngày bắt đầu", example = "2025-11-01")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Ngày kết thúc", example = "2025-11-30")
    private LocalDate endDate;

    // Getter & Setter
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
