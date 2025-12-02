package com.example.demo.service;

import com.example.demo.entity.RepairOrder;
import com.example.demo.entity.RepairOrderItem;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RepairOrderItemRepository;
import com.example.demo.repository.RepairOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepairOrderService {

    private final RepairOrderRepository repository;
    private final RepairOrderItemRepository itemRepository;

    public RepairOrderService(RepairOrderRepository repository,
                              RepairOrderItemRepository itemRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
    }

    
    public RepairOrder createRepairOrder(RepairOrder order) {

        if (order.getStatus() == null)
            order.setStatus("PENDING");

        if (order.getOrderCode() == null)
            order.setOrderCode("ORD-" + System.currentTimeMillis());

        order.setDateReceived(LocalDateTime.now());

        // tránh null list
        if (order.getParts() == null) order.setParts(new ArrayList<>());
        if (order.getServices() == null) order.setServices(new ArrayList<>());

        order.calculateEstimatedTotal();
        return repository.save(order);
    }

    public List<RepairOrder> getAllOrders() {
        return repository.findAll();
    }

    public RepairOrder getOrderById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found with id: " + id));
    }

   
    public RepairOrder updateOrder(String id, RepairOrder updated) {
        return repository.findById(id).map(order -> {

            if (updated.getStatus() != null) order.setStatus(updated.getStatus());
            if (updated.getNote() != null) order.setNote(updated.getNote());
            if (updated.getCustomerId() != null) order.setCustomerId(updated.getCustomerId());
            if (updated.getCarId() != null) order.setCarId(updated.getCarId());
            if (updated.getTechnicianIds() != null) order.setTechnicianIds(updated.getTechnicianIds());
            if (updated.getParts() != null) order.setParts(updated.getParts());
            if (updated.getServices() != null) order.setServices(updated.getServices());
            if (updated.getDateReceived() != null) order.setDateReceived(updated.getDateReceived());
            if (updated.getDateReturned() != null) order.setDateReturned(updated.getDateReturned());

            order.calculateEstimatedTotal();
            return repository.save(order);

        }).orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found with id: " + id));
    }

    public void deleteOrder(String id) {
        repository.deleteById(id);
    }

    public RepairOrder completeOrder(String id) {
        RepairOrder order = getOrderById(id);
        order.setStatus("COMPLETED");
        order.setDateReturned(LocalDateTime.now());
        return repository.save(order);
    }

    public RepairOrder payOrder(String id) {
        RepairOrder order = getOrderById(id);
        order.setStatus("PAID");
        order.setDateReturned(LocalDateTime.now());
        return repository.save(order);
    }

    
    public RepairOrderItem addItem(String repairOrderId, RepairOrderItem item, boolean isPart) {
        RepairOrder order = getOrderById(repairOrderId);

        item.setRepairOrderId(order.getId());
        item.setCreatedAt(LocalDateTime.now());
        item.recalcTotal();

        RepairOrderItem savedItem = itemRepository.save(item);

        if (isPart) order.getParts().add(savedItem);
        else order.getServices().add(savedItem);

        order.calculateEstimatedTotal();
        repository.save(order);

        return savedItem;
    }

   
    public RepairOrderItem updateItem(String itemId, RepairOrderItem updatedItem) {
        RepairOrderItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        if (updatedItem.getName() != null) item.setName(updatedItem.getName());
        if (updatedItem.getUnitPrice() != null) item.setUnitPrice(updatedItem.getUnitPrice());
        if (updatedItem.getQuantity() != null) item.setQuantity(updatedItem.getQuantity());

        item.recalcTotal();
        itemRepository.save(item);

        // update tổng đơn
        RepairOrder order = getOrderById(item.getRepairOrderId());
        order.calculateEstimatedTotal();
        repository.save(order);

        return item;
    }

    
    public void deleteItem(String repairOrderId, String itemId, boolean isPart) {
        RepairOrder order = getOrderById(repairOrderId);

        itemRepository.deleteById(itemId);

        if (isPart) order.getParts().removeIf(i -> i.getId().equals(itemId));
        else order.getServices().removeIf(i -> i.getId().equals(itemId));

        order.calculateEstimatedTotal();
        repository.save(order);
    }

    public List<RepairOrderItem> getItems(String repairOrderId) {
        return itemRepository.findByRepairOrderId(repairOrderId);
    }
}
