package com.example.demo.service;

import java.security.Provider;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairOrderService {

    private final RepairOrderRepository repository;
    private final RepairOrderItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;
    private final ServiceRepository serviceRepository;
    private final TechnicianRepository technicianRepository;

    public RepairOrderService(RepairOrderRepository repository,
                              RepairOrderItemRepository itemRepository,
                              CustomerRepository customerRepository,
                              CarRepository carRepository,
                            ServiceRepository serviceRepository,
                        TechnicianRepository technicianRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
        this.serviceRepository = serviceRepository;
        this.technicianRepository = technicianRepository;
    }

    
    public RepairOrderResponse createRepairOrder(RepairOrderRequest request) {
        RepairOrder order = new RepairOrder();
        order.setCustomerId(request.getCustomerId());
        order.setCarId(request.getCarId());
        order.setTechnicianIds(request.getTechnicianIds());
        order.setNote(request.getNote());
        order.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");
        order.setOrderCode("ORD-" + System.currentTimeMillis());
        order.setDateReceived(LocalDateTime.now());

        order.setParts(new ArrayList<>());
        if (request.getParts() != null) {
            for (RepairOrderItemRequest itemReq : request.getParts()) {
                RepairOrderItem item = new RepairOrderItem();
                item.setId(itemReq.getId());
                item.setName(item.getName());
                item.setUnitPrice(item.getUnitPrice());
                item.setQuantity(itemReq.getQuantity());
                item.setTotal(item.getTotal());
                order.getParts().add(item);
            }
        }

        order.setService(new ArrayList<>());
        order.setServiceIds(new ArrayList<>());
        if (request.getServiceIds() != null) {
            for (String serviceId : request.getServiceIds()) {
                RepairOrderItem item = new RepairOrderItem();
                item.setId(serviceId);
                // item.setQuantity(1);
                order.getService().add(item);
                order.getServiceIds().add(serviceId);
            }
        }



        order.calculateEstimatedTotal();
        order = repository.save(order);
        return convertToResponse(order);
    }

    
    public RepairOrderResponse updateOrder(String id, RepairOrderRequest request) {
        RepairOrder updated = convertRequestToEntity(request);
        RepairOrder saved = repository.findById(id).map(order -> {
            if (updated.getStatus() != null) order.setStatus(updated.getStatus());
            if (updated.getNote() != null) order.setNote(updated.getNote());
            if (updated.getCustomerId() != null) order.setCustomerId(updated.getCustomerId());
            if (updated.getCarId() != null) order.setCarId(updated.getCarId());
            if (updated.getTechnicianIds() != null) order.setTechnicianIds(updated.getTechnicianIds());
            if (updated.getParts() != null) order.setParts(updated.getParts());
            if (updated.getServiceIds() != null) order.setServiceIds(updated.getServiceIds());
            if (updated.getDateReceived() != null) order.setDateReceived(updated.getDateReceived());
            if (updated.getDateReturned() != null) order.setDateReturned(updated.getDateReturned());
            order.calculateEstimatedTotal();
            return repository.save(order);
        }).orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found with id: " + id));
        return convertToResponse(saved);
    }

    
    public RepairOrder convertRequestToEntity(RepairOrderRequest req) {
        RepairOrder order = new RepairOrder();
        order.setId(req.getId());
        order.setOrderCode(req.getOrderCode());
        order.setCustomerId(req.getCustomerId());
        order.setCarId(req.getCarId());
        order.setTechnicianIds(req.getTechnicianIds());
        order.setServiceIds(req.getServiceIds());
        order.setNote(req.getNote());
        order.setStatus(req.getStatus());

        if (req.getParts() != null) {
            List<RepairOrderItem> partItems = req.getParts().stream().map(i -> {
                RepairOrderItem item = new RepairOrderItem();
                item.setId(i.getId());
                item.setQuantity(i.getQuantity());
                return item;
            }).toList();
            order.setParts(partItems);
        }

        if (req.getServiceIds() != null && !req.getServiceIds().isEmpty()) {

            List<Object> serviceItems = req.getServiceIds().stream()
                    .map(id -> {
                        RepairOrderItem item = new RepairOrderItem();
                        item.setId(id);
                        return (Object) item;  
                    })
                    .collect(Collectors.toList()); 

            order.setService(serviceItems); 
        }


        return order;
    }

    
    public RepairOrderResponse convertToResponse(RepairOrder order) {
        RepairOrderResponse res = new RepairOrderResponse();
        res.setId(order.getId());
        res.setOrderCode(order.getOrderCode());
        res.setCustomerId(order.getCustomerId());
        res.setCarId(order.getCarId());
        res.setTechnicianIds(order.getTechnicianIds());
        res.setServiceIds(order.getServiceIds());
        res.setNote(order.getNote());
        res.setStatus(order.getStatus());
        res.setDateReceived(order.getDateReceived());
        res.setDateReturned(order.getDateReturned());
        res.setEstimatedTotal(order.getEstimatedTotal());

        // MAP PARTS
        if (order.getParts() != null) {
            List<RepairOrderItemResponse> parts = order.getParts().stream().map(i -> {
                RepairOrderItemResponse item = new RepairOrderItemResponse();
                item.setId(i.getId());
                item.setName(i.getName());
                item.setUnitPrice(i.getUnitPrice());
                item.setQuantity(i.getQuantity());
                item.setTotal(i.getTotal());
                return item;
            }).toList();
            res.setParts(parts);
        }

        // map service
        // if (order.getServiceIds() != null) {
        //     List<Object> sv = serviceRepository.findAllById(order.getServiceIds())
        //             .stream()
        //             .map(s -> (Object) s)
        //             .toList();
        //     res.setService(sv);
        // }

        // map technician
        if (order.getTechnicianIds() != null) {
        List<TechnicianResponse> techs = technicianRepository.findAllById(order.getTechnicianIds())
                .stream()
                .map(t -> {
                    TechnicianResponse dto = new TechnicianResponse();
                    dto.setId(t.getId());
                    dto.setTechCode(t.getTechCode());
                    dto.setName(t.getName());
                    dto.setPhone(t.getPhone());
                    dto.setBaseSalary(t.getSalaryBase());
                    dto.setPosition(t.getPosition());
                    dto.setUserId(t.getUserId());
                    dto.setActive(t.getActive());
                    dto.setCreatedAt(t.getCreatedAt());
                    dto.setUpdatedAt(t.getUpdatedAt());
                    return dto;
                }).toList();

            res.setTechnician(techs);
        }

        // map service
        if (order.getServiceIds() != null) {
        List<GarageServiceResponse> sv = serviceRepository.findAllById(order.getServiceIds())
                   .stream()
                   .map(s -> {
                    GarageServiceResponse dto = new GarageServiceResponse();
                   dto.setId(s.getId());
                    dto.setServiceCode(s.getServiceCode());
                    dto.setName(s.getName());
                    dto.setDescription(s.getDescription());
                    dto.setPrice(s.getPrice());
                    dto.setCreatedAt(s.getCreatedAt());
                    dto.setUpdatedAt(s.getUpdatedAt());
                    return dto;
                   }).toList();
            res.setService(sv);
        }

        // map customer
        if (order.getCustomerId() != null) {
            customerRepository.findById(order.getCustomerId()).ifPresent(customer -> {
                CustomerResponse cr = new CustomerResponse();
                cr.setMessage("Lấy thông tin khách hàng thành công");
                cr.setData(customer);
                res.setCustomer(cr);
            });
        }

        // MAP CAR
        if (order.getCarId() != null) {
            carRepository.findById(order.getCarId()).ifPresent(car -> {
                CarResponse carRes = new CarResponse();
                carRes.setId(car.getId());
                carRes.setPlate(car.getPlate());
                carRes.setModel(car.getModel());
                carRes.setManufacturer(car.getManufacturer());
                carRes.setCustomerId(car.getCustomerId());
                res.setCar(carRes);
            });
        }

        return res;
    }

    
    public List<RepairOrder> getAllOrders() {
        return repository.findAll();
    }

    public RepairOrder getOrderById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found with id: " + id));
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
        RepairOrderItem saved = itemRepository.save(item);
        if (isPart) order.getParts().add(saved);
        else order.getService().add(saved);
        order.calculateEstimatedTotal();
        repository.save(order);
        return saved;
    }

    public RepairOrderItem updateItem(String itemId, RepairOrderItem updatedItem) {
        RepairOrderItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));
        if (updatedItem.getName() != null) item.setName(updatedItem.getName());
        if (updatedItem.getUnitPrice() != null) item.setUnitPrice(updatedItem.getUnitPrice());
        if (updatedItem.getQuantity() != null) item.setQuantity(updatedItem.getQuantity());
        item.recalcTotal();
        itemRepository.save(item);

        RepairOrder order = getOrderById(item.getRepairOrderId());
        order.calculateEstimatedTotal();
        repository.save(order);
        return item;
    }

    public void deleteItem(String repairOrderId, String itemId, boolean isPart) {
        RepairOrder order = getOrderById(repairOrderId);
        itemRepository.deleteById(itemId);
        if (isPart) order.getParts().removeIf(i -> i.getId().equals(itemId));
        else order.getService().removeIf(i -> i instanceof RepairOrderItem it && it.getId().equals(itemId));
        order.calculateEstimatedTotal();
        repository.save(order);
    }

    public List<RepairOrderItem> getItems(String repairOrderId) {
        return itemRepository.findByRepairOrderId(repairOrderId);
    }
}
