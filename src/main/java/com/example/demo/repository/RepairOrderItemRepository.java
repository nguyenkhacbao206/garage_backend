package com.example.demo.repository;

import com.example.demo.entity.RepairOrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairOrderItemRepository extends MongoRepository<RepairOrderItem, String> {

    // Lấy danh sách item theo RepairOrderId
    List<RepairOrderItem> findByRepairOrderId(String repairOrderId);

    // Xóa tất cả item khi xóa RepairOrder
    void deleteByRepairOrderId(String repairOrderId);
}
