package com.example.demo.service;

import com.example.demo.dto.ReportResponse;
import com.example.demo.entity.RepairOrder;
import com.example.demo.entity.RepairOrderItem;
import com.example.demo.repository.RepairOrderRepository;
import com.example.demo.repository.RepairOrderItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderItemRepository repairOrderItemRepository;

    public ReportService(RepairOrderRepository repairOrderRepository,
                         RepairOrderItemRepository repairOrderItemRepository) {
        this.repairOrderRepository = repairOrderRepository;
        this.repairOrderItemRepository = repairOrderItemRepository;
    }

    // Dashboard KPI
    public List<ReportResponse.DashboardKPI> getDashboardKPI() {
        List<RepairOrder> allOrders = repairOrderRepository.findAll();

        BigDecimal totalRevenue = allOrders.stream()
                .map(RepairOrder::getEstimatedTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalOrders = allOrders.size();
        long servicesDone = allOrders.stream().mapToLong(o -> o.getService() != null ? o.getService().size() : 0).sum();
        long partsSold = allOrders.stream().mapToLong(o -> o.getParts() != null ? o.getParts().size() : 0).sum();

        List<ReportResponse.DashboardKPI> list = new ArrayList<>();

        ReportResponse.DashboardKPI revenue = new ReportResponse.DashboardKPI();
        revenue.setId(1);
        revenue.setType("total_revenue");
        revenue.setTitle("Tổng doanh thu năm");
        revenue.setValue(formatMoney(totalRevenue));
        revenue.setSubText("+18.2% so với năm trước"); // demo
        revenue.setIsGrowth(true);
        list.add(revenue);

        ReportResponse.DashboardKPI orders = new ReportResponse.DashboardKPI();
        orders.setId(2);
        orders.setType("total_orders");
        orders.setTitle("Tổng đơn hàng");
        orders.setValue(String.valueOf(totalOrders));
        orders.setSubText("+156 đơn so với năm trước"); // demo
        orders.setIsGrowth(true);
        list.add(orders);

        ReportResponse.DashboardKPI services = new ReportResponse.DashboardKPI();
        services.setId(3);
        services.setType("services_done");
        services.setTitle("Dịch vụ đã thực hiện");
        services.setValue(String.valueOf(servicesDone));
        services.setSubText("Trung bình " + (servicesDone / 12) + "/tháng");
        services.setIsGrowth(false);
        list.add(services);

        ReportResponse.DashboardKPI parts = new ReportResponse.DashboardKPI();
        parts.setId(4);
        parts.setType("parts_sold");
        parts.setTitle("Phụ tùng đã bán");
        parts.setValue(String.valueOf(partsSold));
        parts.setSubText("Trung bình " + (partsSold / 12) + "/tháng");
        parts.setIsGrowth(false);
        list.add(parts);

        return list;
    }

    // Thống kê doanh thu dịch vụ và phụ tùng
    public Map<String, List<ReportResponse.ServicePartStatistic>> getServicePartStatistics() {
        List<RepairOrderItem> allItems = repairOrderItemRepository.findAll();

        // serviceItems: filter dựa theo repairOrder.service
        List<ReportResponse.ServicePartStatistic> services = new ArrayList<>();
        List<ReportResponse.ServicePartStatistic> parts = new ArrayList<>();

        Map<String, List<RepairOrderItem>> groupedByName = allItems.stream()
                .collect(Collectors.groupingBy(RepairOrderItem::getName));

        int rank = 1;
        for (Map.Entry<String, List<RepairOrderItem>> entry : groupedByName.entrySet()) {
            String name = entry.getKey();
            List<RepairOrderItem> items = entry.getValue();
            int quantity = items.stream().mapToInt(i -> i.getQuantity() != null ? i.getQuantity() : 0).sum();
            BigDecimal totalRevenue = items.stream().map(i -> i.getTotal() != null ? i.getTotal() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            ReportResponse.ServicePartStatistic stat = new ReportResponse.ServicePartStatistic();
            stat.setId(String.valueOf(rank));
            stat.setRank(rank);
            stat.setCode("CODE-" + rank);
            stat.setName(name);
            stat.setQuantity(quantity);
            stat.setTotalRevenue(totalRevenue);

            // demo: tách dựa trên đơn giản (giả sử quantity <= 1 là dịch vụ)
            if (items.get(0).getQuantity() <= 1) services.add(stat);
            else parts.add(stat);

            rank++;
        }

        Map<String, List<ReportResponse.ServicePartStatistic>> result = new HashMap<>();
        result.put("serviceStatistics", services);
        result.put("partStatistics", parts);
        return result;
    }

    // Thống kê doanh thu theo tháng
    public ReportResponse.RevenueChart getMonthlyRevenueChart() {
        List<RepairOrder> allOrders = repairOrderRepository.findAll();
        Map<YearMonth, BigDecimal> revenueMap = new HashMap<>();

        for (int i = 0; i < 12; i++) {
            YearMonth ym = YearMonth.now().minusMonths(11 - i);
            revenueMap.put(ym, BigDecimal.ZERO);
        }

        for (RepairOrder order : allOrders) {
            if (order.getDateReceived() != null && order.getEstimatedTotal() != null) {
                YearMonth ym = YearMonth.from(order.getDateReceived());
                revenueMap.put(ym, revenueMap.getOrDefault(ym, BigDecimal.ZERO).add(order.getEstimatedTotal()));
            }
        }

        List<ReportResponse.RevenueChartData> chartData = revenueMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    ReportResponse.RevenueChartData data = new ReportResponse.RevenueChartData();
                    data.setMonth("T" + e.getKey().getMonthValue() + "/" + e.getKey().getYear());
                    data.setValue(e.getValue());
                    return data;
                }).collect(Collectors.toList());

        ReportResponse.RevenueChart chart = new ReportResponse.RevenueChart();
        chart.setTitle("Doanh thu 12 tháng gần nhất");
        chart.setSubTitle("So sánh doanh thu theo tháng");
        chart.setCurrencyUnit("VND");
        chart.setChartData(chartData);

        return chart;
    }

    private String formatMoney(BigDecimal money) {
        if (money == null) return "0 đ";
        double value = money.doubleValue();
        if (value >= 1_000_000_000)
            return String.format("%.2fB đ", value / 1_000_000_000);
        if (value >= 1_000_000)
            return String.format("%.2fM đ", value / 1_000_000);
        if (value >= 1_000)
            return String.format("%.2fK đ", value / 1_000);
        return String.format("%.0f đ", value);
    }
}
