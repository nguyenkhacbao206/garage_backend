package com.example.demo.repository;

import com.example.demo.entity.ChatMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, String> {

    List<ChatMessageEntity> findByReceiver(String receiver);
}
