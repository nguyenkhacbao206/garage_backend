package com.example.demo.repository;

import com.example.demo.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findAllByOrderByCreatedAtDesc();

    List<Notification> findByReadFalseOrderByCreatedAtDesc();

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(String receiverId);

    List<Notification> findByStatusOrderByCreatedAtDesc(String status);

    List<Notification> findByTypeOrderByCreatedAtDesc(String type); // BOOKING / CONFIRM / CANCEL
}
