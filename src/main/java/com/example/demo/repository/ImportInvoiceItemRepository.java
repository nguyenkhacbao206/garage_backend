package com.example.demo.repository;

import com.example.demo.entity.ImportInvoiceItem;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImportInvoiceItemRepository extends MongoRepository<ImportInvoiceItem, String> {

    List<ImportInvoiceItem> findByInvoiceId(String invoiceId);
    ImportInvoiceItem findFirstByOrderByImportInvoiceItemCodeDesc();


}