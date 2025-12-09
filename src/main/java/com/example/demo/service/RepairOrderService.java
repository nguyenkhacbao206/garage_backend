package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;

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


    private void setTechniciansActive(List<String> technicianIds) {
        if (technicianIds == null || technicianIds.isEmpty()) return;

        List<Technician> techs = technicianRepository.findAllById(technicianIds);
        for (Technician t : techs) {
            t.setActive(true);
            t.setUpdatedAt(LocalDateTime.now());
        }
        technicianRepository.saveAll(techs);
    }

    private void setTechniciansInactive(List<String> technicianIds) {
        if (technicianIds == null || technicianIds.isEmpty()) return;

        List<Technician> techs = technicianRepository.findAllById(technicianIds);
        for (Technician t : techs) {
            t.setActive(false);
            t.setUpdatedAt(LocalDateTime.now());
        }
        technicianRepository.saveAll(techs);
    }

    // Load service info để convert serviceId → RepairOrderItem
    private RepairOrderItem buildServiceItemFromServiceId(String serviceId) {
        GarageService sv = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + serviceId));

        RepairOrderItem item = new RepairOrderItem();
        item.setId(sv.getId());
        item.setName(sv.getName());
        item.setUnitPrice(sv.getPrice());
        item.setQuantity(1);
        item.recalcTotal();
        return item;
    }

    // create
    public RepairOrderResponse createRepairOrder(RepairOrderRequest request) {

        final RepairOrder order = new RepairOrder(); // final để lambda có thể dùng

        // Thông tin cơ bản
        order.setCustomerId(request.getCustomerId());
        order.setCarId(request.getCarId());
        order.setTechnicianIds(request.getTechnicianIds());
        order.setNote(request.getNote());
        order.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");
        order.setOrderCode("ORD-" + System.currentTimeMillis());
        order.setDateReceived(LocalDateTime.now());
        order.setServiceFee(request.getServiceFee());

        // Lưu snapshot xe
        if (request.getCarId() != null) {
            carRepository.findById(request.getCarId()).ifPresent(car -> {
                order.setCarSnapshot(new RepairOrder.CarSnapshot(
                    car.getId(),
                    car.getPlate(),
                    car.getModel(),
                    car.getManufacturer(),
                    car.getCustomerId()
                ));
            });
        }

        // Parts
        order.setParts(new ArrayList<>());
        if (request.getParts() != null) {
            for (RepairOrderItemRequest itemReq : request.getParts()) {
                RepairOrderItem item = new RepairOrderItem();
                item.setId(itemReq.getId());
                item.setName(itemReq.getName());
                item.setUnitPrice(itemReq.getUnitPrice());
                item.setQuantity(itemReq.getQuantity());
                item.setTotal(itemReq.getTotal());
                order.getParts().add(item);
            }
        }

        // Services
        order.setService(new ArrayList<>());
        order.setServiceIds(new ArrayList<>());

        if (request.getServiceIds() != null) {
            for (String sid : request.getServiceIds()) {
                order.getService().add(buildServiceItemFromServiceId(sid));
                order.getServiceIds().add(sid);
            }
        }

        // Tính tổng
        order.calculateEstimatedTotal();

        // Lưu vào database
        RepairOrder savedOrder = repository.save(order);

        // đổi trang thái làm việc cho technician
        setTechniciansActive(order.getTechnicianIds());

        // Trả về response
        return convertToResponse(savedOrder);
    }


    // UPDATE ORDER
    public RepairOrderResponse updateOrder(String id, RepairOrderRequest request) {
        RepairOrder patch = convertRequestToEntity(request);

        RepairOrder saved = repository.findById(id).map(order -> {

            if (patch.getStatus() != null) order.setStatus(patch.getStatus());
            if (patch.getNote() != null) order.setNote(patch.getNote());
            if (patch.getCustomerId() != null) order.setCustomerId(patch.getCustomerId());

            if (patch.getCarId() != null && !patch.getCarId().equals(order.getCarId())) {
                order.setCarId(patch.getCarId());
                carRepository.findById(patch.getCarId()).ifPresent(car -> {
                    order.setCarSnapshot(new RepairOrder.CarSnapshot(
                        car.getId(),
                        car.getPlate(),
                        car.getModel(),
                        car.getManufacturer(),
                        car.getCustomerId()
                    ));
                });
            }

            if (patch.getTechnicianIds() != null) {
                setTechniciansInactive(order.getTechnicianIds());
                order.setTechnicianIds(patch.getTechnicianIds());
                setTechniciansActive(patch.getTechnicianIds());
            }

            if (patch.getServiceFee() != null) order.setServiceFee(patch.getServiceFee());

            if (patch.getParts() != null) order.setParts(patch.getParts());

            if (patch.getServiceIds() != null) {
                order.setServiceIds(patch.getServiceIds());
                List<RepairOrderItem> serviceItems = patch.getServiceIds().stream()
                        .map(this::buildServiceItemFromServiceId)
                        .collect(Collectors.toList());
                order.setService(serviceItems);
            }

            if (patch.getDateReceived() != null) order.setDateReceived(patch.getDateReceived());
            if (patch.getDateReturned() != null) order.setDateReturned(patch.getDateReturned());

            order.calculateEstimatedTotal();
            return repository.save(order);

        }).orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found with id: " + id));

        return convertToResponse(saved);
    }

    // Convert request → entity cho update
    public RepairOrder convertRequestToEntity(RepairOrderRequest req) {
        RepairOrder order = new RepairOrder();
        order.setCustomerId(req.getCustomerId());
        order.setCarId(req.getCarId());
        order.setTechnicianIds(req.getTechnicianIds());
        order.setNote(req.getNote());
        order.setStatus(req.getStatus());
        order.setServiceIds(req.getServiceIds());
        order.setServiceFee(req.getServiceFee());   
        order.setDateReceived(req.getDateReceived());
        order.setDateReturned(req.getDateReturned());

        if (req.getParts() != null) {
            List<RepairOrderItem> partItems = req.getParts().stream().map(i -> {
                RepairOrderItem item = new RepairOrderItem();
                item.setId(i.getId());
                item.setName(i.getName());
                item.setUnitPrice(i.getUnitPrice());
                item.setQuantity(i.getQuantity());
                item.setTotal(i.getTotal());
                return item;
            }).collect(Collectors.toList());
            order.setParts(partItems);
        }

        return order;
    }

    // Convert → response
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
        res.setServiceFee(order.getServiceFee());

        if (order.getParts() != null) {
            res.setParts(order.getParts().stream().map(i -> {
                RepairOrderItemResponse dto = new RepairOrderItemResponse();
                dto.setId(i.getId());
                dto.setName(i.getName());
                dto.setUnitPrice(i.getUnitPrice());
                dto.setQuantity(i.getQuantity());
                dto.setTotal(i.getTotal());
                return dto;
            }).collect(Collectors.toList()));
        }

        if (order.getServiceIds() != null) {
            List<GarageServiceResponse> serviceList = order.getServiceIds().stream().map(sid ->
                serviceRepository.findById(sid).map(sv -> {
                    GarageServiceResponse dto = new GarageServiceResponse();
                    dto.setId(sv.getId());
                    dto.setServiceCode(sv.getServiceCode());
                    dto.setName(sv.getName());
                    dto.setDescription(sv.getDescription());
                    dto.setPrice(sv.getPrice());
                    dto.setCreatedAt(sv.getCreatedAt());
                    dto.setUpdatedAt(sv.getUpdatedAt());
                    return dto;
                }).orElseGet(() -> {
                    GarageServiceResponse dto = new GarageServiceResponse();
                    dto.setId(sid);
                    dto.setName("Service đã bị xóa");
                    return dto;
                })
            ).collect(Collectors.toList());
            res.setService(serviceList);
        }

        if (order.getTechnicianIds() != null) {
            List<TechnicianResponse> techList = order.getTechnicianIds().stream().map(tid ->
                technicianRepository.findById(tid).map(t -> {
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
                }).orElseGet(() -> {
                    TechnicianResponse dto = new TechnicianResponse();
                    dto.setId(tid);
                    dto.setName("Technician đã bị xóa");
                    return dto;
                })
            ).collect(Collectors.toList());
            res.setTechnician(techList);
        }

        // Customer
        if (order.getCustomerId() != null) {
            CustomerResponse cr = customerRepository.findById(order.getCustomerId())
                    .map(c -> {
                        CustomerResponse r = new CustomerResponse();
                        r.setMessage("Lấy thông tin khách hàng thành công");
                        r.setData(c);
                        return r;
                    }).orElseGet(() -> {
                        CustomerResponse r = new CustomerResponse();
                        r.setMessage("Khách hàng đã bị xóa");
                        r.setData(null);
                        return r;
                    });
            res.setCustomer(cr);
        }

        // Car: ưu tiên snapshot
        if (order.getCarSnapshot() != null) {
            RepairOrder.CarSnapshot snap = order.getCarSnapshot();
            CarResponse carResp = new CarResponse();
            carResp.setId(snap.getId());
            carResp.setPlate(snap.getPlate());
            carResp.setModel(snap.getModel());
            carResp.setManufacturer(snap.getManufacturer());
            carResp.setCustomerId(snap.getCustomerId());
            res.setCar(carResp);
        } else if (order.getCarId() != null) {
            carRepository.findById(order.getCarId()).ifPresentOrElse(c -> {
                CarResponse dto = new CarResponse();
                dto.setId(c.getId());
                dto.setPlate(c.getPlate());
                dto.setModel(c.getModel());
                dto.setManufacturer(c.getManufacturer());
                dto.setCustomerId(c.getCustomerId());
                res.setCar(dto);
            }, () -> {
                CarResponse dto = new CarResponse();
                dto.setId(order.getCarId());
                dto.setPlate("Xe đã bị xóa");
                res.setCar(dto);
            });
        }

        return res;
    }

    // GET ALL
    public List<RepairOrder> getAllOrders() {
        return repository.findAll();
    }

    // GET BY ID
    public RepairOrder getOrderById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RepairOrder not found with id: " + id));
    }

    // DELETE
    public void deleteOrder(String id) {
        repository.deleteById(id);
    }

    // MARK COMPLETED
    public RepairOrder completeOrder(String id) {
        RepairOrder order = getOrderById(id);
        order.setStatus("COMPLETED");
        order.setDateReturned(LocalDateTime.now());

        // trả technician về rảnh
        setTechniciansInactive(order.getTechnicianIds());

        return repository.save(order);
    }

    public RepairOrder payOrder(String id) {
        RepairOrder order = getOrderById(id);
        order.setStatus("PAID");
        order.setDateReturned(LocalDateTime.now());

        // trả technician về rảnh
        setTechniciansInactive(order.getTechnicianIds());

        return repository.save(order);
    }


    // ADD ITEM
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

    // UPDATE ITEM
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

    // DELETE ITEM
    public void deleteItem(String repairOrderId, String itemId, boolean isPart) {
        RepairOrder order = getOrderById(repairOrderId);
        itemRepository.deleteById(itemId);

        if (isPart)
            order.getParts().removeIf(i -> i.getId().equals(itemId));
        else
            order.getService().removeIf(i -> i.getId().equals(itemId));

        order.calculateEstimatedTotal();
        repository.save(order);
    }

    // GET ITEMS
    public List<RepairOrderItem> getItems(String repairOrderId) {
        return itemRepository.findByRepairOrderId(repairOrderId);
    }
}
