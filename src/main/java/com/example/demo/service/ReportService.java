package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Report;

import com.example.demo.dto.ReportResponse;
import com.example.demo.entity.RepairOrder;
import com.example.demo.entity.RepairOrderItem;
import com.example.demo.repository.RepairOrderItemRepository;
import com.example.demo.repository.RepairOrderRepository;
import com.example.demo.repository.CustomerRepository;

@Service
public class ReportService {

    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderItemRepository repairOrderItemRepository;
    private final CustomerRepository customerRepository;

    public ReportService(RepairOrderRepository repairOrderRepository,RepairOrderItemRepository repairOrderItemRepository,CustomerRepository customerRepository) {
        this.repairOrderRepository = repairOrderRepository;
        this.repairOrderItemRepository = repairOrderItemRepository;
        this.customerRepository = customerRepository;
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
        revenue.setSubText("+18.2% so với năm trước");
        revenue.setIsGrowth(true);
        list.add(revenue);

        ReportResponse.DashboardKPI orders = new ReportResponse.DashboardKPI();
        orders.setId(2);
        orders.setType("total_orders");
        orders.setTitle("Tổng đơn hàng");
        orders.setValue(String.valueOf(totalOrders));
        orders.setSubText("+156 đơn so với năm trước");
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

    // Thống kê dịch vụ & phụ tùng
public Map<String, List<ReportResponse.ServicePartStatistic>> getServicePartStatistics() {

    List<RepairOrder> orders = repairOrderRepository.findAll();

    // Lấy tất cả item từ parts
    List<RepairOrderItem> allItems = orders.stream()
            .filter(o -> o.getParts() != null)
            .flatMap(o -> o.getParts().stream())
            .collect(Collectors.toList());

    // Gom nhóm theo itemId của part
    Map<String, List<RepairOrderItem>> groupedByItemId = allItems.stream()
            .collect(Collectors.groupingBy(RepairOrderItem::getId));

    List<ReportResponse.ServicePartStatistic> serviceStats = new ArrayList<>();
    List<ReportResponse.ServicePartStatistic> partStats = new ArrayList<>();

    for (String itemId : groupedByItemId.keySet()) {

        List<RepairOrderItem> items = groupedByItemId.get(itemId);

        // LẤY ĐÚNG DỮ LIỆU PART
        RepairOrderItem sample = items.get(0);

        int quantity = items.stream()
                .mapToInt(i -> Optional.ofNullable(i.getQuantity()).orElse(0))
                .sum();

        BigDecimal totalRevenue = items.stream()
                .map(i -> Optional.ofNullable(i.getTotal()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ReportResponse.ServicePartStatistic stat = new ReportResponse.ServicePartStatistic();
        stat.setId(sample.getId());        // LẤY ID TỪ PART
        // stat.setCode(sample.getId());
        stat.setName(sample.getName());    // LẤY TÊN TỪ PART
        stat.setQuantity(quantity);
        stat.setTotalRevenue(totalRevenue);

        // Check xem item thuộc SERVICE hay PART
        boolean isService = orders.stream()
                .anyMatch(o -> o.getServiceIds() != null && o.getServiceIds().contains(sample.getId()));

        if (isService) {
            serviceStats.add(stat);
        } else {
            partStats.add(stat);
        }
    }

    // Sắp xếp
    Comparator<ReportResponse.ServicePartStatistic> sortByQty =
            Comparator.comparing(ReportResponse.ServicePartStatistic::getQuantity).reversed();

    serviceStats.sort(sortByQty);
    partStats.sort(sortByQty);

    // Gán rank
    int rank = 1;
    for (ReportResponse.ServicePartStatistic s : serviceStats) {
        s.setRank(rank++);
    }

    rank = 1;
    for (ReportResponse.ServicePartStatistic p : partStats) {
        p.setRank(rank++);
    }

    // Trả kết quả
    Map<String, List<ReportResponse.ServicePartStatistic>> result = new HashMap<>();
    result.put("serviceStatistics", serviceStats);
    result.put("partStatistics", partStats);

    return result;
}



    //LẤY ĐÚNG DỮ LIỆU TRONG NGÀY
    public Map<String, Object> getDailyReport(LocalDate date) {

    LocalDateTime start = date.atStartOfDay();
    LocalDateTime end = date.plusDays(1).atStartOfDay();

    List<RepairOrder> orders = repairOrderRepository
            .findByDateReceivedBetween(start, end);

    int totalServiceCount = 0;
    int totalPartCount = 0;
    BigDecimal totalRevenue = BigDecimal.ZERO;

    for (RepairOrder order : orders) {

        // LẤY DỊCH VỤ
        List<RepairOrderItem> services = order.getService();
        if (services != null) {
            for (RepairOrderItem s : services) {
                totalServiceCount++;
                BigDecimal total = Optional.ofNullable(s.getTotal()).orElse(BigDecimal.ZERO);
                totalRevenue = totalRevenue.add(total);
            }
        }

        // LẤY PHỤ TÙNG
        List<RepairOrderItem> parts = order.getParts();
        if (parts != null) {
            for (RepairOrderItem p : parts) {
                int qty = Optional.ofNullable(p.getQuantity()).orElse(0);
                totalPartCount += qty;
                BigDecimal total = Optional.ofNullable(p.getTotal()).orElse(BigDecimal.ZERO);
                totalRevenue = totalRevenue.add(total);
            }
        }
    }

    Map<String, Object> result = new HashMap<>();
    result.put("date", date.toString());
    result.put("totalServicesInDay", totalServiceCount);
    result.put("totalPartsInDay", totalPartCount);
    result.put("totalRevenueInDay", totalRevenue);

    return result;
}
public List<Report.TopUserStatistic> getTopUsersReport() {

    List<RepairOrder> allOrders = repairOrderRepository.findAll();

    // Nhóm orders theo customerId
    Map<String, List<RepairOrder>> ordersByCustomer = allOrders.stream()
            .filter(o -> o.getCustomerId() != null)
            .collect(Collectors.groupingBy(RepairOrder::getCustomerId));

    List<Report.TopUserStatistic> result = new ArrayList<>();

    for (String customerId : ordersByCustomer.keySet()) {
        List<RepairOrder> customerOrders = ordersByCustomer.get(customerId);

        int totalServices = 0;
        int totalParts = 0;
        BigDecimal totalSpent = BigDecimal.ZERO;

        String userName = "";
        String userCode = "";

        // Lấy thông tin khách hàng từ repository
        com.example.demo.entity.Customer cust = customerRepository.findById(customerId).orElse(null);
        if (cust != null) {
            userName = cust.getName();
            userCode = cust.getCustomerCode();
        }

        // Tính tổng dịch vụ, phụ tùng và chi phí
        for (RepairOrder order : customerOrders) {

            // Dịch vụ
            if (order.getService() != null) {
                totalServices += order.getService().size();
                totalSpent = totalSpent.add(
                        order.getService().stream()
                                .map(i -> Optional.ofNullable(i.getTotal()).orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
            }

            // Phụ tùng
            if (order.getParts() != null) {
                totalParts += order.getParts().stream()
                        .mapToInt(i -> Optional.ofNullable(i.getQuantity()).orElse(0))
                        .sum();
                totalSpent = totalSpent.add(
                        order.getParts().stream()
                                .map(i -> Optional.ofNullable(i.getTotal()).orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
            }
        }

        // Tạo đối tượng thống kê
        Report.TopUserStatistic stat = new Report.TopUserStatistic();
        stat.setUserId(customerId);
        stat.setUserName(userName);
        stat.setUserCode(userCode);
        stat.setTotalServices(totalServices);
        stat.setTotalParts(totalParts);
        stat.setTotalSpent(totalSpent);

        result.add(stat);
    }

    // Sắp xếp theo tổng chi tiêu giảm dần
    result.sort(Comparator.comparing(Report.TopUserStatistic::getTotalSpent).reversed());

    return result;
}




    // Thống kê doanh thu 12 tháng
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
                revenueMap.put(ym,
                        revenueMap.getOrDefault(ym, BigDecimal.ZERO)
                                .add(order.getEstimatedTotal()));
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
