package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ServiceRequest;
import com.example.demo.dto.ServiceResponse;
import com.example.demo.entity.GarageService;
import com.example.demo.repository.ServiceRepository;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // SORT BY CREATED
    public List<ServiceResponse> sortServicesByCreatedAt(boolean asc) {
        List<GarageService> list = serviceRepository.findAll();
        Comparator<GarageService> comp = Comparator.comparing(
                GarageService::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder())
        );
        if (!asc) {
            comp = comp.reversed();
        }
        return list.stream()
                .sorted(comp)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // SEARCH
    public List<GarageService> searchServices(String serviceCode, String name) {
        boolean hasCode = serviceCode != null && !serviceCode.isEmpty();
        boolean hasName = name != null && !name.isEmpty();

        if (!hasCode && !hasName) {
            return serviceRepository.findAll();
        } else if (hasCode && hasName) {
            return serviceRepository.findByServiceCodeContainingIgnoreCaseOrNameContainingIgnoreCase(serviceCode, name);
        } else if (hasCode) {
            return serviceRepository.findByServiceCodeContainingIgnoreCase(serviceCode);
        } else {
            return serviceRepository.findByNameContainingIgnoreCase(name);
        }
    }

    // CONVERT
    private ServiceResponse convertToResponse(GarageService service) {
        return new ServiceResponse("Thành công", service);
    }

    // GET ALL
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .sorted((a, b) -> {
                    String codeA = a.getServiceCode() == null ? "" : a.getServiceCode();
                    String codeB = b.getServiceCode() == null ? "" : b.getServiceCode();
                    return codeB.compareTo(codeA);
                })
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public Optional<ServiceResponse> getServiceById(String id) {
        return serviceRepository.findById(id)
                .map(this::convertToResponse);
    }

    // CREATE
    public ServiceResponse createService(ServiceRequest request) {
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

        GarageService saved = serviceRepository.save(service);
        return convertToResponse(saved);
    }

    // UPDATE
    public ServiceResponse updateService(String id, ServiceRequest request) {
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

        GarageService updated = serviceRepository.save(service);
        return convertToResponse(updated);
    }

    // DELETE
    public void deleteService(String id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Dịch vụ không tồn tại!");
        }
        serviceRepository.deleteById(id);
    }
}
