package com.example.demo.service;

import com.example.demo.dto.ImportInvoiceItemRequest;
import com.example.demo.dto.ImportInvoiceItemResponse;
import com.example.demo.entity.ImportInvoiceItem;
import com.example.demo.repository.ImportInvoiceItemRepository;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImportInvoiceItemService {

    private final ImportInvoiceItemRepository repo;
    private final SupplierRepository supplierRepo;
    private final PartRepository partRepo;

    public ImportInvoiceItemService(
            ImportInvoiceItemRepository repo,
            SupplierRepository supplierRepo,
            PartRepository partRepo
    ) {
        this.repo = repo;
        this.supplierRepo = supplierRepo;
        this.partRepo = partRepo;
    }

    // Chuyển entity sang response
    private ImportInvoiceItemResponse toResponse(ImportInvoiceItem e) {
        ImportInvoiceItemResponse res = new ImportInvoiceItemResponse();
        res.setId(e.getId());
        res.setInvoiceId(e.getInvoiceId());
        res.setDate(e.getDate());
        res.setTotal(e.getTotal());
        res.setInvoiceTotal(e.getInvoiceTotal());
        res.setCreatedAt(e.getCreatedAt());
        res.setUpdatedAt(e.getUpdatedAt());

        List<ImportInvoiceItemResponse.ImportPartResponse> partList = new ArrayList<>();

        for (ImportInvoiceItem.PartInfo pi : e.getParts()) {
            partRepo.findById(pi.getPartId()).ifPresent(p -> {
                ImportInvoiceItemResponse.ImportPartResponse part = new ImportInvoiceItemResponse.ImportPartResponse();
                part.setPartId(p.getId());
                part.setPartName(p.getName());
                part.setPartCode(p.getPartCode());
                part.setPrice(p.getPrice());
                part.setStock(p.getStock());
                part.setDescription(p.getDescription());

                // Supplier nằm trong mỗi part
                supplierRepo.findById(e.getSupplierId()).ifPresent(s -> {
                    ImportInvoiceItemResponse.SupplierResponse sup = new ImportInvoiceItemResponse.SupplierResponse();
                    sup.setSupplierId(s.getId());
                    sup.setSupplierName(s.getName());
                    sup.setSupplierCode(s.getSupplierCode());
                    sup.setSupplierAddress(s.getAddress());
                    sup.setSupplierEmail(s.getEmail());
                    sup.setSupplierPhone(s.getPhone());
                    sup.setSupplierDescription(s.getDescription());
                    part.setSupplier(sup);
                });

                partList.add(part);
            });
        }

        res.setParts(partList);
        return res;
    }

    // Tạo hóa đơn mới
    public ImportInvoiceItemResponse create(ImportInvoiceItemRequest req) {
        LocalDateTime now = LocalDateTime.now();

        ImportInvoiceItem e = new ImportInvoiceItem();
        e.setInvoiceId(req.getInvoiceId());
        e.setSupplierId(req.getSupplierId());
        e.setDate(req.getDate());
        e.setCreatedAt(now);
        e.setUpdatedAt(now);

        // Map danh sách part từ request
        List<ImportInvoiceItem.PartInfo> partList = req.getParts().stream().map(p -> {
            ImportInvoiceItem.PartInfo pi = new ImportInvoiceItem.PartInfo();
            pi.setPartId(p.getPartId());
            pi.setQuantity(p.getQuantity());
            pi.setUnitPrice(p.getUnitPrice());
            return pi;
        }).collect(Collectors.toList());
        e.setParts(partList);

        // Tính tổng tiền hóa đơn
        BigDecimal total = partList.stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        e.setTotal(total);
        e.setInvoiceTotal(total);

        repo.save(e);
        return toResponse(e);
    }

    // Lấy tất cả
    public List<ImportInvoiceItemResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Lấy theo ID
    public Optional<ImportInvoiceItemResponse> getById(String id) {
        return repo.findById(id).map(this::toResponse);
    }

    // Update hóa đơn
    public Optional<ImportInvoiceItemResponse> update(String id, ImportInvoiceItemRequest req) {
        Optional<ImportInvoiceItem> opt = repo.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        ImportInvoiceItem e = opt.get();
        e.setInvoiceId(req.getInvoiceId());
        e.setSupplierId(req.getSupplierId());
        e.setDate(req.getDate());
        e.setUpdatedAt(LocalDateTime.now());

        List<ImportInvoiceItem.PartInfo> partList = req.getParts().stream().map(p -> {
            ImportInvoiceItem.PartInfo pi = new ImportInvoiceItem.PartInfo();
            pi.setPartId(p.getPartId());
            pi.setQuantity(p.getQuantity());
            pi.setUnitPrice(p.getUnitPrice());
            return pi;
        }).collect(Collectors.toList());
        e.setParts(partList);

        BigDecimal total = partList.stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        e.setTotal(total);
        e.setInvoiceTotal(total);

        repo.save(e);
        return Optional.of(toResponse(e));
    }

    // Xóa hóa đơn
    public boolean delete(String id) {
        Optional<ImportInvoiceItem> opt = repo.findById(id);
        if (opt.isEmpty()) return false;

        repo.deleteById(id);
        return true;
    }

    // Sắp xếp theo createdAt
    public List<ImportInvoiceItemResponse> sortByCreatedAt(List<ImportInvoiceItemResponse> items, boolean asc) {
        Comparator<ImportInvoiceItemResponse> comp = Comparator.comparing(
                ImportInvoiceItemResponse::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder())
        );
        if (!asc) comp = comp.reversed();
        items.sort(comp);
        return items;
    }

    // Getter repositories nếu cần
    public ImportInvoiceItemRepository getRepo() { return repo; }
    public SupplierRepository getSupplierRepo() { return supplierRepo; }
    public PartRepository getPartRepo() { return partRepo; }
}
