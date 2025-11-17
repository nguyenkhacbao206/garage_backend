package com.example.demo.service;

import com.example.demo.entity.RepairOrder;
import com.example.demo.repository.RepairOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RepairOrderService {

    private final RepairOrderRepository repository;

    public RepairOrderService(RepairOrderRepository repository) {
        this.repository = repository;
    }

    // Tạo RepairOrder mới
    public RepairOrder createRepairOrder(RepairOrder order) {
        if (order.getDateReceived() == null) {
            order.setDateReceived(LocalDateTime.now());
        }
        if (order.getStatus() == null) {
            order.setStatus("PENDING");
        }
        if (order.getOrderCode() == null) {
            order.setOrderCode("ORD-" + System.currentTimeMillis());
        }
        return repository.save(order);
    }

    // Lấy tất cả RepairOrder
    public List<RepairOrder> getAllOrders() {
        return repository.findAll();
    }

    // Lấy RepairOrder theo id
    public Optional<RepairOrder> getOrderById(String id) {
        return repository.findById(id);
    }

    // Cập nhật RepairOrder 
    public RepairOrder updateOrder(String id, RepairOrder updatedOrder) {
        return repository.findById(id).map(order -> {
            if (updatedOrder.getStatus() != null) order.setStatus(updatedOrder.getStatus());
            if (updatedOrder.getItems() != null) order.setItems(updatedOrder.getItems());
            if (updatedOrder.getDateReturned() != null) order.setDateReturned(updatedOrder.getDateReturned());
            return repository.save(order);
        }).orElseThrow(() -> new RuntimeException("RepairOrder not found with id: " + id));
    }

    // Đánh dấu sửa xong
    public RepairOrder completeOrder(String id) {
        return repository.findById(id).map(order -> {
            order.setStatus("COMPLETED");
            if (order.getDateReturned() == null) {
                order.setDateReturned(LocalDateTime.now());
            }
            return repository.save(order);
        }).orElseThrow(() -> new RuntimeException("RepairOrder not found with id: " + id));
    }

    // Thanh toán
    public RepairOrder payOrder(String id) {
        return repository.findById(id).map(order -> {
            order.setStatus("PAID");
            if (order.getDateReturned() == null) {
                order.setDateReturned(LocalDateTime.now());
            }
            return repository.save(order);
        }).orElseThrow(() -> new RuntimeException("RepairOrder not found with id: " + id));
    }

    // Xóa đơn
    public void deleteOrder(String id) {
        repository.deleteById(id);
    }
}
