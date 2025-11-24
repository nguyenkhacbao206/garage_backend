package com.example.demo.repository;

import com.example.demo.entity.ImportItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ImportItemRepository extends MongoRepository<ImportItem, String> {

    ImportItem findFirstByOrderByIdDesc();

    List<ImportItem> findByImportInvoiceId(String invoiceId);
    // Tìm theo partId
    List<ImportItem> findByPartId(String partId);
    // Tìm theo số lượng lớn hơn
    List<ImportItem> findByQuantityGreaterThan(Integer quantity);
    // Tìm theo tổng tiền lớn 
    List<ImportItem> findByTotalGreaterThan(BigDecimal total);

    List<ImportItem> findByUnitPriceGreaterThan(BigDecimal unitPrice);

    List<ImportItem> findBySellPriceGreaterThan(BigDecimal sellPrice);

    List<ImportItem> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<ImportItem> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);
}
