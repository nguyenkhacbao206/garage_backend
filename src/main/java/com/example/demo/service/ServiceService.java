package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ServiceRequest;
import com.example.demo.entity.GarageService;
import com.example.demo.repository.ServiceRepository;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Lấy tất cả dịch vụ (raw) để Controller wrap vào ServiceResponse
    public List<GarageService> getAllServicesRaw() {
        return serviceRepository.findAll().stream()
                .sorted((a, b) -> {
                    String codeA = a.getServiceCode() == null ? "" : a.getServiceCode();
                    String codeB = b.getServiceCode() == null ? "" : b.getServiceCode();
                    return codeB.compareTo(codeA);
                })
                .collect(Collectors.toList());
    }

    // Lấy dịch vụ theo ID
    public GarageService getServiceByIdRaw(String id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));
    }

    // Tạo dịch vụ
    public GarageService createService(ServiceRequest request) {
        if (serviceRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên dịch vụ đã tồn tại!");
        }

        GarageService service = new GarageService();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());

        // Tạo mã DV tự động
        GarageService lastService = serviceRepository.findFirstByOrderByServiceCodeDesc();
        String newCode;
        if (lastService == null || lastService.getServiceCode() == null) {
            newCode = "DV-001";
        } else {
            String numberPart = lastService.getServiceCode().substring(3);
            int number = Integer.parseInt(numberPart) + 1;
            newCode = "DV-" + String.format("%03d", number);
        }
        service.setServiceCode(newCode);

        return serviceRepository.save(service);
    }

    // Cập nhật dịch vụ
    public GarageService updateService(String id, ServiceRequest request) {
        GarageService service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại!"));

        if (request.getName() != null && !request.getName().equals(service.getName())
                && serviceRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên dịch vụ đã tồn tại!");
        }

        if (request.getName() != null) service.setName(request.getName());
        if (request.getDescription() != null) service.setDescription(request.getDescription());
        if (request.getPrice() != null) service.setPrice(request.getPrice());
        service.setUpdatedAt(LocalDateTime.now());

        return serviceRepository.save(service);
    }

    // Xóa dịch vụ
    public GarageService deleteService(String id) {
        GarageService service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại!"));
        serviceRepository.deleteById(id);
        return service;
    }

    // Search dịch vụ
    public List<GarageService> searchServicesRaw(String serviceCode, String name) {
        boolean hasCode = serviceCode != null && !serviceCode.isEmpty();
        boolean hasName = name != null && !name.isEmpty();

        List<GarageService> list;
        if (!hasCode && !hasName) {
            list = serviceRepository.findAll();
        } else if (hasCode && hasName) {
            list = serviceRepository.findByServiceCodeContainingIgnoreCaseOrNameContainingIgnoreCase(serviceCode, name);
        } else if (hasCode) {
            list = serviceRepository.findByServiceCodeContainingIgnoreCase(serviceCode);
        } else {
            list = serviceRepository.findByNameContainingIgnoreCase(name);
        }

        return list.stream()
                .sorted((a, b) -> {
                    String codeA = a.getServiceCode() == null ? "" : a.getServiceCode();
                    String codeB = b.getServiceCode() == null ? "" : b.getServiceCode();
                    return codeB.compareTo(codeA);
                })
                .collect(Collectors.toList());
    }
}
