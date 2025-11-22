package com.example.demo.repository;

import com.example.demo.entity.Supplier;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {
        //  Lấy mã lớn nhất
    Supplier findFirstByOrderBySupplierCodeDesc();
    //kiểm tra sdt và email của nhà cung cấp
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    
    // kiểm tra xen tên dịch vụ đã tồn tại chưa
    boolean existsByName(String name);
    boolean existsBySupplierCode(String supplierCode);

    //tìm kiếm riêng lẻ từng trường
    List<Supplier> findByNameContainingIgnoreCase(String name);

    List<Supplier> findBySupplierCodeContainingIgnoreCase(String supplierCode);
    
    // Tìm kiếm nhiều trường với regex
    @Query("{ $or: [ " +
           "{ 'supplierCode': { $regex: ?0, $options: 'i' } }, " +
           "{ 'name': { $regex: ?0, $options: 'i' } }, " +
           "{ 'phone': { $regex: ?0, $options: 'i' } }, " +
           "{ 'email': { $regex: ?0, $options: 'i' } } " +
           "] }")
    List<Supplier> searchByKeyword(String keyword);
}
