package com.example.demo.service;

import com.example.demo.dto.PartRequest;
import com.example.demo.dto.PartResponse;
import com.example.demo.entity.Part;
import com.example.demo.repository.PartRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartService {

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public List<PartResponse> getAll() {
        return partRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PartResponse getById(String id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng"));
        return mapToResponse(part);
    }

    public PartResponse create(PartRequest req) {
        Part part = new Part(
                req.getName(),
                req.getUnit(),
                req.getPrice(),
                req.getStock(),
                req.getDescription(),
                req.getSupplierId()
        );
        partRepository.save(part);
        return mapToResponse(part);
    }

    public PartResponse update(String id, PartRequest req) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng"));

        if (req.getName() != null) part.setName(req.getName());
        if (req.getUnit() != null) part.setUnit(req.getUnit());
        if (req.getPrice() != null) part.setPrice(req.getPrice());
        if (req.getStock() != null) part.setStock(req.getStock());
        if (req.getDescription() != null) part.setDescription(req.getDescription());

        partRepository.save(part);
        return mapToResponse(part);
    }

    public void delete(String id) {
        if (!partRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phụ tùng");
        }
        partRepository.deleteById(id);
    }

    private PartResponse mapToResponse(Part part) {
        return new PartResponse(
                part.getId(),
                part.getName(),
                part.getUnit(),
                part.getPrice(),
                part.getStock(),
                part.getDescription(),
                part.getSupplierId()
        );
    }
}
