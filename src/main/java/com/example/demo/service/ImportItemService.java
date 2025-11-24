package com.example.demo.service;

import com.example.demo.dto.ImportItemRequest;
import com.example.demo.dto.ImportItemResponse;
import com.example.demo.entity.ImportItem;
import com.example.demo.entity.ImportInvoice;
import com.example.demo.entity.Part;
import com.example.demo.repository.ImportItemRepository;
import com.example.demo.repository.ImportInvoiceRepository;
import com.example.demo.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImportItemService {

    @Autowired
    private ImportItemRepository itemRepo;

    @Autowired
    private ImportInvoiceRepository invoiceRepo;

    @Autowired
    private PartRepository partRepo;

    private ImportItemResponse convertToResponse(ImportItem item) {
        ImportItemResponse res = new ImportItemResponse();
        res.setId(item.getId());
        res.setImportInvoiceId(item.getImportInvoiceId());
        res.setPartId(item.getPartId());
        res.setQuantity(item.getQuantity());
        res.setUnitPrice(item.getUnitPrice());
        res.setSellPrice(item.getSellPrice());
        res.setTotal(item.getTotal());
        res.setCreatedAt(item.getCreatedAt());
        res.setUpdatedAt(item.getUpdatedAt());

        // Lấy tên Part
        partRepo.findById(item.getPartId()).ifPresent(p -> res.setPartName(p.getName()));
        return res;
    }

    // GET ALL
    public List<ImportItemResponse> getAll() {
        return itemRepo.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public Optional<ImportItemResponse> getById(String id) {
        return itemRepo.findById(id)
                .map(this::convertToResponse);
    }

    // GET BY Invoice ID
    public List<ImportItemResponse> getByInvoiceId(String invoiceId) {
        return itemRepo.findByImportInvoiceId(invoiceId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // CREATE
    public ImportItemResponse create(ImportItemRequest req) {
        // Validate invoice
        ImportInvoice invoice = invoiceRepo.findById(req.getImportInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice không tồn tại!"));

        // Validate part
        Part part = partRepo.findById(req.getPartId())
                .orElseThrow(() -> new RuntimeException("Part không tồn tại!"));

        // Tạo item mới
        ImportItem item = new ImportItem(
                req.getImportInvoiceId(),
                req.getPartId(),
                req.getQuantity(),
                req.getUnitPrice(),
                req.getSellPrice()
        );

        // Update stock part: cộng tồn kho
        int newStock = (part.getStock() != null ? part.getStock() : 0) + req.getQuantity();
        part.setStock(newStock);
        partRepo.save(part);

        // Lưu item
        ImportItem saved = itemRepo.save(item);
        return convertToResponse(saved);
    }

    // UPDATE
    public Optional<ImportItemResponse> update(String id, ImportItemRequest req) {
        Optional<ImportItem> opt = itemRepo.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        ImportItem item = opt.get();

        // Validate part
        Part part = partRepo.findById(req.getPartId())
                .orElseThrow(() -> new RuntimeException("Part không tồn tại!"));

        // Update stock: trừ stock cũ, cộng stock mới
        int oldQty = item.getQuantity() != null ? item.getQuantity() : 0;
        int diff = req.getQuantity() - oldQty;
        int newStock = (part.getStock() != null ? part.getStock() : 0) + diff;
        part.setStock(newStock);
        partRepo.save(part);

        // Cập nhật item
        item.setQuantity(req.getQuantity());
        item.setUnitPrice(req.getUnitPrice());
        item.setSellPrice(req.getSellPrice());
        item.recalcTotal();
        item.setUpdatedAt(LocalDateTime.now());

        ImportItem updated = itemRepo.save(item);
        return Optional.of(convertToResponse(updated));
    }

    // DELETE
    public boolean delete(String id) {
        Optional<ImportItem> opt = itemRepo.findById(id);
        if (opt.isEmpty()) return false;

        ImportItem item = opt.get();

        // Trừ stock
        Part part = partRepo.findById(item.getPartId()).orElse(null);
        if (part != null && part.getStock() != null) {
            part.setStock(part.getStock() - item.getQuantity());
            partRepo.save(part);
        }

        itemRepo.deleteById(id);
        return true;
    }
}
