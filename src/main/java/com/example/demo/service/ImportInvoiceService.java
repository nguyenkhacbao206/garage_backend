package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ImportInvoiceRequest;
import com.example.demo.dto.ImportInvoiceResponse;
import com.example.demo.entity.ImportInvoice;
import com.example.demo.entity.Supplier;
import com.example.demo.repository.ImportInvoiceRepository;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ImportInvoiceService {

    @Autowired
    private ImportInvoiceRepository importInvoiceRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    private final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    // Helper: Convert entity → response
    private ImportInvoiceResponse convertToResponse(ImportInvoice invoice) {
        ImportInvoiceResponse res = new ImportInvoiceResponse();
        res.setId(invoice.getId());
        res.setSupplierId(invoice.getSupplierId());
        res.setDate(invoice.getDate());
        res.setTotal(invoice.getTotal());

        // Lấy tên Supplier
        supplierRepository.findById(invoice.getSupplierId())
                .ifPresent(s -> res.setSupplierName(s.getName()));
        return res;
    }

    // GET ALL
    public List<ImportInvoiceResponse> getAll() {
        return importInvoiceRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public Optional<ImportInvoiceResponse> getById(String id) {
        return importInvoiceRepository.findById(id)
                .map(this::convertToResponse);
    }

    // CREATE
    public ImportInvoiceResponse create(ImportInvoiceRequest request) {
        // Validate Supplier
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier không tồn tại!"));

        ImportInvoice invoice = new ImportInvoice();
        invoice.setSupplierId(supplier.getId());
        invoice.setDate(LocalDateTime.parse(request.getDate(), formatter));
        invoice.setTotal(request.getTotal());

        ImportInvoice savedInvoice = importInvoiceRepository.save(invoice);
        return convertToResponse(savedInvoice);
    }
    // UPDATE

    public Optional<ImportInvoiceResponse> update(String id, ImportInvoiceRequest request) {
        Optional<ImportInvoice> existingOpt = importInvoiceRepository.findById(id);

        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        ImportInvoice invoice = existingOpt.get();

        // Validate Supplier
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier không tồn tại!"));

        invoice.setSupplierId(supplier.getId());
        invoice.setDate(LocalDateTime.parse(request.getDate(), formatter));
        invoice.setTotal(request.getTotal());

        ImportInvoice updatedInvoice = importInvoiceRepository.save(invoice);
        return Optional.of(convertToResponse(updatedInvoice));
    }

    // DELETE

    public boolean delete(String id) {
        if (importInvoiceRepository.existsById(id)) {
            importInvoiceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
