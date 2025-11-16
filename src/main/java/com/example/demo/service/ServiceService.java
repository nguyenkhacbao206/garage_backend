package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ServiceRequest;
import com.example.demo.dto.ServiceResponse;
import com.example.demo.entity.GarageService;
import com.example.demo.repository.ServiceRepository;

@Service
public class ServiceService {

    @Autowired
    private final ServiceRepository serviceRepository;
    
    public ServiceService(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    
    public List<GarageService> searchServices(String serviceCode , String name) {
        boolean hasCode = serviceCode != null && !serviceCode.isEmpty();
        boolean hasName = name != null && !name.isEmpty();

        if (!hasName&& !hasCode) {
            return serviceRepository.findAll();
        }

         // Chỉ code
    if (hasCode) {
        return serviceRepository.findByServiceCodeContainingIgnoreCase(serviceCode);
    }

    // Chỉ name
    return serviceRepository.findByNameContainingIgnoreCase(name);
    }

    private ServiceResponse convertToResponse(GarageService service){
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setServiceCode(service.getServiceCode());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setPrice(service.getPrice());
        return response;
    }

    

    // Lấy danh sách dịch vụ (có thể lọc theo tên)
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Lấy dịch vụ theo ID
    public Optional<ServiceResponse> getServiceById(String id) {
        return serviceRepository.findById(id)
                .map(this::convertToResponse);
    }

    // Tạo dịch vụ mới
    public GarageService createService(ServiceRequest request) {

        if (serviceRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên dịch vụ đã tồn tại!");
        }

        GarageService service = new GarageService();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        //tạo mã DV
        GarageService lastService = serviceRepository.findFirstByOrderByServiceCodeDesc();
        String newCode;

        if (lastService == null || lastService.getServiceCode() == null) {
        newCode = "DV-001";
        } else {
        String maxCode = lastService.getServiceCode();
        //cắt phần chữ độc lấy số
        String numberPart = maxCode.substring(3);
        int number = Integer.parseInt(numberPart);
        //tăng lên 1
        number++;
        //khi vượt quá 3 số vẫn tăng
        newCode = "DV-" + String.format("%03d", number);
    }

    service.setServiceCode(newCode);

    return serviceRepository.save(service);

    }

    // Cập nhật dịch vụ
    public Optional<ServiceResponse> updateService(String id, ServiceRequest request) {
        Optional<GarageService> existingOpt = serviceRepository.findById(id);

        if (existingOpt.isPresent()) {
            GarageService service = existingOpt.get();

            if (request.getName() != null && !request.getName().equals(service.getName())
                    && serviceRepository.existsByName(request.getName())) {
                throw new RuntimeException("Tên dịch vụ đã tồn tại!");
            }

            if (request.getName() != null) service.setName(request.getName());
            if (request.getDescription() != null) service.setDescription(request.getDescription());
            if (request.getPrice() != null) service.setPrice(request.getPrice());

            serviceRepository.save(service);

            return Optional.of(convertToResponse(service));
        }

        return Optional.empty();
    }

    // Xóa dịch vụ
    public boolean deleteService(String id) {
        if (!serviceRepository.existsById(id)) {
            return false;
        }
        serviceRepository.deleteById(id);
        return true;
    }
}
