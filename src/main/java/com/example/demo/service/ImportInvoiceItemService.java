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

    private ImportInvoiceItemResponse toResponse(ImportInvoiceItem e) {
        ImportInvoiceItemResponse res = new ImportInvoiceItemResponse();
        res.setId(e.getId());
        res.setInvoiceId(e.getInvoiceId());
        res.setDate(e.getDate());
        res.setQuantity(e.getQuantity());
        res.setUnitPrice(e.getUnitPrice());
        res.setTotal(e.getTotal());
        res.setInvoiceTotal(e.getInvoiceTotal());
        res.setCreatedAt(e.getCreatedAt());
        res.setUpdatedAt(e.getUpdatedAt());

        partRepo.findById(e.getPartId()).ifPresent(p -> {
        ImportInvoiceItemResponse.ImportPartResponse part =
                new ImportInvoiceItemResponse.ImportPartResponse();
            part.setPartId(p.getId());
            part.setPartName(p.getName());
            part.setPartCode(p.getPartCode());
            part.setPrice(p.getPrice());
            part.setStock(p.getStock());
            part.setDescription(p.getDescription());

            
            supplierRepo.findById(e.getSupplierId()).ifPresent(s -> {
                ImportInvoiceItemResponse.SupplierResponse sup =
                    new ImportInvoiceItemResponse.SupplierResponse();
                sup.setSupplierId(s.getId());
                sup.setSupplierName(s.getName());
                sup.setSupplierCode(s.getSupplierCode());
                sup.setSupplierAddress(s.getAddress());
                sup.setSupplierEmail(s.getEmail());
                sup.setSupplierPhone(s.getPhone());
                sup.setSupplierDescription(s.getDescription());
                part.setSupplier(sup);
            });
            
            res.setPart(part);
        });

        return res;
    }

    private BigDecimal calculateTotal(ImportInvoiceItemRequest req) {
        return req.getUnitPrice().multiply(BigDecimal.valueOf(req.getQuantity()));
    }

    private BigDecimal calculateInvoiceTotal(String invoiceId) {
        List<ImportInvoiceItem> items = repo.findByInvoiceId(invoiceId);
        return items.stream()
                .map(ImportInvoiceItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ImportInvoiceItemResponse create(ImportInvoiceItemRequest req) {
        BigDecimal total = calculateTotal(req);
        LocalDateTime now = LocalDateTime.now();

        ImportInvoiceItem e = new ImportInvoiceItem();
        e.setInvoiceId(req.getInvoiceId());
        e.setSupplierId(req.getSupplierId());
        e.setPartId(req.getPartId());
        e.setDate(req.getDate());
        e.setDate(now);
        e.setQuantity(req.getQuantity());
        e.setUnitPrice(req.getUnitPrice());
        e.setTotal(total);
        e.setCreatedAt(now);
        e.setUpdatedAt(now);

        repo.save(e);

        // Cập nhật invoiceTotal
        BigDecimal invoiceTotal = calculateInvoiceTotal(req.getInvoiceId());
        e.setInvoiceTotal(invoiceTotal);
        repo.save(e);

        return toResponse(e);
    }

    public List<ImportInvoiceItemResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ImportInvoiceItemResponse> getByInvoiceId(String invoiceId) {
        return repo.findByInvoiceId(invoiceId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<ImportInvoiceItemResponse> getById(String id) {
        return repo.findById(id).map(this::toResponse);
    }

    public Optional<ImportInvoiceItemResponse> update(String id, ImportInvoiceItemRequest req) {
        Optional<ImportInvoiceItem> opt = repo.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        ImportInvoiceItem e = opt.get();
        BigDecimal total = calculateTotal(req);
        e.setInvoiceId(req.getInvoiceId());
        e.setSupplierId(req.getSupplierId());
        e.setPartId(req.getPartId());
        e.setQuantity(req.getQuantity());
        e.setUnitPrice(req.getUnitPrice());
        e.setTotal(total);
        e.setUpdatedAt(LocalDateTime.now());

        repo.save(e);

        // Cập nhật invoiceTotal
        BigDecimal invoiceTotal = calculateInvoiceTotal(req.getInvoiceId());
        e.setInvoiceTotal(invoiceTotal);
        repo.save(e);

        return Optional.of(toResponse(e));
    }

    public boolean delete(String id) {
        Optional<ImportInvoiceItem> opt = repo.findById(id);
        if (opt.isEmpty()) return false;

        String invoiceId = opt.get().getInvoiceId();
        repo.deleteById(id);

        // Update invoiceTotal cho các item còn lại
        List<ImportInvoiceItem> items = repo.findByInvoiceId(invoiceId);
        BigDecimal invoiceTotal = items.stream()
                .map(ImportInvoiceItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        items.forEach(item -> {
            item.setInvoiceTotal(invoiceTotal);
            repo.save(item);
        });

        return true;
    }

    public List<ImportInvoiceItemResponse> sortByCreatedAt(List<ImportInvoiceItemResponse> items, boolean asc) {
        Comparator<ImportInvoiceItemResponse> comp = Comparator.comparing(
                ImportInvoiceItemResponse::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder())
        );
        if (!asc) comp = comp.reversed();
        items.sort(comp);
        return items;
    }
}
