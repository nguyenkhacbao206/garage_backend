package com.example.demo.repository;

import com.example.demo.entity.ImportInvoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportInvoiceRepository extends MongoRepository<ImportInvoice, String> {

    // Lấy invoice có mã lớn nhất (nếu bạn dùng mã riêng)
    ImportInvoice findFirstByOrderByIdDesc();

    // Tìm theo supplierId
    List<ImportInvoice> findBySupplierId(String supplierId);


    // Tìm theo ngày (string hoặc LocalDateTime tùy bạn)
    List<ImportInvoice> findByDateBetween(
            java.time.LocalDateTime start,
            java.time.LocalDateTime end
    );

    // Tìm theo tổng lớn hơn
    List<ImportInvoice> findByTotalGreaterThan(java.math.BigDecimal total);
}
