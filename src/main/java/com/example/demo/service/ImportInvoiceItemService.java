package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ImportInvoiceItemRequest;
import com.example.demo.dto.ImportInvoiceItemResponse;
import com.example.demo.entity.ImportInvoiceItem;
import com.example.demo.repository.ImportInvoiceItemRepository;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.SupplierRepository;

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


    // STOCK HELPER
    private void increaseStock(String partId, int quantity) {
        partRepo.findById(partId).ifPresent(p -> {
            p.setStock(p.getStock() + quantity);
            partRepo.save(p);
        });
    }

    private void decreaseStock(String partId, int quantity) {
        partRepo.findById(partId).ifPresent(p -> {
            p.setStock(Math.max(0, p.getStock() - quantity));
            partRepo.save(p);
        });
    }



    // Tạo mã tự động MHD-001
    private String generateNewCode() {
        ImportInvoiceItem last = repo.findFirstByOrderByImportInvoiceItemCodeDesc();

        if (last == null || last.getImportInvoiceItemCode() == null) {
            return "MHD-001";
        }

        String maxCode = last.getImportInvoiceItemCode();
        int number = Integer.parseInt(maxCode.substring(4));
        return "MHD-" + String.format("%03d", number + 1);
    }


    // Convert entity -> response
    private ImportInvoiceItemResponse toResponse(ImportInvoiceItem e) {
        ImportInvoiceItemResponse res = new ImportInvoiceItemResponse();

        res.setId(e.getId());
        res.setInvoiceId(e.getInvoiceId());
        res.setDate(e.getDate());
        res.setTotal(e.getTotal());
        res.setInvoiceTotal(e.getInvoiceTotal());
        res.setImportInvoiceItemCode(e.getImportInvoiceItemCode());
        res.setNote(e.getNote());
        res.setCreatedAt(e.getCreatedAt());
        res.setUpdatedAt(e.getUpdatedAt());

        // Supplier Response
        ImportInvoiceItemResponse.SupplierResponse supplier = new ImportInvoiceItemResponse.SupplierResponse();

        supplierRepo.findById(e.getSupplierId()).ifPresent(s -> {
            supplier.setSupplierId(s.getId());
            supplier.setSupplierName(s.getName());
            supplier.setSupplierCode(s.getSupplierCode());
            supplier.setSupplierAddress(s.getAddress());
            supplier.setSupplierEmail(s.getEmail());
            supplier.setSupplierPhone(s.getPhone());
            supplier.setSupplierDescription(s.getDescription());
        });


        // List Parts
        List<ImportInvoiceItemResponse.ImportPartResponse> partList = new ArrayList<>();

        if (e.getParts() != null) {
            for (ImportInvoiceItem.PartInfo pi : e.getParts()) {

                partRepo.findById(pi.getPartId()).ifPresent(p -> {

                    ImportInvoiceItemResponse.ImportPartResponse part =
                            new ImportInvoiceItemResponse.ImportPartResponse();

                    part.setPartId(p.getId());
                    part.setPartName(p.getName());
                    part.setPartCode(p.getPartCode());
                    part.setPrice(p.getPrice());
                    part.setStock(p.getStock());
                    part.setDescription(p.getDescription());

                    // mapping quantity + unitPrice
                    res.setQuantity(pi.getQuantity());
                    res.setUnitPrice(pi.getUnitPrice());

                    partList.add(part);
                });
            }
        }

        supplier.setParts(partList);
        res.setSupplier(supplier);

        return res;
    }

    // CREATE
    public ImportInvoiceItemResponse create(ImportInvoiceItemRequest req) {

        LocalDateTime now = LocalDateTime.now();

        ImportInvoiceItem e = new ImportInvoiceItem();
        e.setInvoiceId(req.getInvoiceId());
        e.setSupplierId(req.getSupplierId());
        e.setDate(req.getDate());
        e.setCreatedAt(now);
        e.setUpdatedAt(now);

        e.setImportInvoiceItemCode(generateNewCode());

        // Map parts
        List<ImportInvoiceItem.PartInfo> partList =
                req.getParts() != null ?
                        req.getParts().stream().map(p -> {
                            ImportInvoiceItem.PartInfo pi = new ImportInvoiceItem.PartInfo();
                            pi.setPartId(p.getPartId());
                            pi.setName(p.getName());
                            pi.setQuantity(p.getQuantity());
                            pi.setUnitPrice(p.getUnitPrice());
                            return pi;
                        }).collect(Collectors.toList()) :
                        new ArrayList<>();

        e.setParts(partList);

        // Tổng tiền
        BigDecimal total = partList.stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        e.setTotal(total);
        e.setInvoiceTotal(total);


        // Update stock khi tạo mới
        for (ImportInvoiceItem.PartInfo p : partList) {
            increaseStock(p.getPartId(), p.getQuantity());
        }


        repo.save(e);

        return toResponse(e);
    }
    //===========




    public List<ImportInvoiceItemResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<ImportInvoiceItemResponse> getById(String id) {
        return repo.findById(id).map(this::toResponse);
    }



    // UPDATE
    public Optional<ImportInvoiceItemResponse> update(String id, ImportInvoiceItemRequest req) {

        Optional<ImportInvoiceItem> opt = repo.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        LocalDateTime now = LocalDateTime.now();

        ImportInvoiceItem e = opt.get();

        // Rollback stock cũ
        if (e.getParts() != null) {
            for (ImportInvoiceItem.PartInfo old : e.getParts()) {
                decreaseStock(old.getPartId(), old.getQuantity());
            }
        }


        e.setInvoiceId(req.getInvoiceId());
        e.setSupplierId(req.getSupplierId());
        e.setDate(req.getDate());
        e.setUpdatedAt(now);


        List<ImportInvoiceItem.PartInfo> partList =
                req.getParts() != null ?
                        req.getParts().stream().map(p -> {
                            ImportInvoiceItem.PartInfo pi = new ImportInvoiceItem.PartInfo();
                            pi.setPartId(p.getPartId());
                            pi.setQuantity(p.getQuantity());
                            pi.setUnitPrice(p.getUnitPrice());
                            return pi;
                        }).collect(Collectors.toList())
                        : new ArrayList<>();

        e.setParts(partList);


        // Cập nhật tồn kho mới
        for (ImportInvoiceItem.PartInfo p : partList) {
            increaseStock(p.getPartId(), p.getQuantity());
        }


        BigDecimal total = partList.stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        e.setTotal(total);
        e.setInvoiceTotal(total);

        repo.save(e);

        return Optional.of(toResponse(e));
    }
    // DELETE
    public boolean delete(String id) {

        Optional<ImportInvoiceItem> opt = repo.findById(id);
        if (opt.isEmpty()) return false;

        // Rollback stock khi xóa
        ImportInvoiceItem e = opt.get();
        if (e.getParts() != null) {
            for (ImportInvoiceItem.PartInfo p : e.getParts()) {
                decreaseStock(p.getPartId(), p.getQuantity());
            }
        }

        repo.deleteById(id);
        return true;
    }
    // SORT
    public List<ImportInvoiceItemResponse> sortByCreatedAt(List<ImportInvoiceItemResponse> items, boolean asc) {
        Comparator<ImportInvoiceItemResponse> comp =
                Comparator.comparing(
                        ImportInvoiceItemResponse::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );

        if (!asc) comp = comp.reversed();

        items.sort(comp);
        return items;
    }
}
