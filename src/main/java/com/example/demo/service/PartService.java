package com.example.demo.service;

import com.example.demo.dto.PartRequest;
import com.example.demo.dto.PartResponse;
import com.example.demo.entity.Part;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartService {

    private final PartRepository partRepository;
    private final SupplierRepository supplierRepository;

    public PartService(PartRepository partRepository, SupplierRepository supplierRepository) {
        this.partRepository = partRepository;
        this.supplierRepository = supplierRepository;
    }

    private String generatePartCode() {
        Part lastPart = partRepository.findTopByOrderByPartCodeDesc();

        if (lastPart == null || lastPart.getPartCode() == null) {
            return "PT-001";
        }

        String lastCode = lastPart.getPartCode().substring(3);
        int next = Integer.parseInt(lastCode) + 1;

        return String.format("PT-%03d", next);
    }

    // GET ALL with sorting
    public List<PartResponse> getAll(String order) {

        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by(Sort.Direction.ASC, "createdAt")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        return partRepository.findAll(sort)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public PartResponse getById(String id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng"));

        return mapToResponse(part);
    }

    // SEARCH
    public List<PartResponse> search(String keyword) {
        List<Part> parts = partRepository
                .findByNameContainingIgnoreCaseOrPartCodeContainingIgnoreCase(keyword, keyword);

        return parts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // CREATE
    public PartResponse create(PartRequest req) {

        Supplier supplier = supplierRepository.findById(req.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy supplier"));

        Part part = new Part(
                generatePartCode(),
                req.getName(),
                req.getSalePrice(),
                req.getStock(),
                req.getDescription(),
                req.getSupplierId(),
                supplier
        );

        LocalDateTime now = LocalDateTime.now();
        part.setCreatedAt(now);
        part.setUpdatedAt(now);

        partRepository.save(part);

        return mapToResponse(part);
    }

    // UPDATE
    public PartResponse update(String id, PartRequest req) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng"));

        if (req.getName() != null) part.setName(req.getName());
        // if (req.getPrice() != null) part.setPrice(req.getPrice());
        if (req.getStock() != null) part.setStock(req.getStock());
        if (req.getDescription() != null) part.setDescription(req.getDescription());

        if (req.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(req.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy supplier"));
            part.setSupplierId(req.getSupplierId());
            part.setSupplier(supplier);
        }

        part.setUpdatedAt(LocalDateTime.now());

        partRepository.save(part);
        return mapToResponse(part);
    }

    // DELETE
    public void delete(String id) {
        if (!partRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phụ tùng");
        }
        partRepository.deleteById(id);
    }

    // MAPPING
    private PartResponse mapToResponse(Part part) {
        PartResponse res = new PartResponse(
                part.getId(),
                part.getPartCode(),
                part.getName(),
                // part.getPrice(),
                part.getSalePrice(),
                part.getStock(),
                part.getDescription(),
                part.getSupplierId(),
                part.getSupplier()
        );

        res.setCreatedAt(part.getCreatedAt());
        res.setUpdatedAt(part.getUpdatedAt());

        return res;
    }
}
