package com.example.demo.service;

import com.example.demo.dto.PartRequest;
import com.example.demo.dto.PartResponse;
import com.example.demo.entity.Part;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.SupplierRepository;

import org.springframework.stereotype.Service;

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
        String lastCode = lastPart.getPartCode().replace("PT-", "");
        int number = Integer.parseInt(lastCode);
        int next = number + 1;
        if (next < 1000) {
            return String.format("PT-%03d", next);
        }
        return "PT-" + next;
    }

    // get all
    public List<PartResponse> getAll() {
        return partRepository.findAll()
                .stream()
                .filter(p -> p.getPartCode() != null)  // bỏ phần tử null
                .sorted((a, b) -> b.getPartCode().compareTo(a.getPartCode()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // get by id
    public PartResponse getById(String id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng"));
        return mapToResponse(part);
    }

    // new create
    public PartResponse create(PartRequest req) {

        Supplier supplier = supplierRepository.findById(req.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy supplier"));

        String newPartCode = generatePartCode();

        Part part = new Part(
                newPartCode,
                req.getName(),
                req.getPrice(),
                req.getStock(),
                req.getDescription(),
                req.getSupplierId(),
                supplier 
        );

        partRepository.save(part);
        return mapToResponse(part);
    }

    // update
    public PartResponse update(String id, PartRequest req) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng"));

        if (req.getName() != null) part.setName(req.getName());
        if (req.getPrice() != null) part.setPrice(req.getPrice());
        if (req.getStock() != null) part.setStock(req.getStock());
        if (req.getDescription() != null) part.setDescription(req.getDescription());

        if (req.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(req.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy supplier"));
            part.setSupplierId(req.getSupplierId());
            part.setSupplier(supplier); 
        }

        partRepository.save(part);
        return mapToResponse(part);
    }

    // delete
    public void delete(String id) {
        if (!partRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phụ tùng");
        }
        partRepository.deleteById(id);
    }

    // mapping
    private PartResponse mapToResponse(Part part) {
        return new PartResponse(
                part.getId(),
                part.getPartCode(),
                part.getName(),
                part.getPrice(),
                part.getStock(),
                part.getDescription(),
                part.getSupplierId(),
                part.getSupplier()
        );
    }
}
