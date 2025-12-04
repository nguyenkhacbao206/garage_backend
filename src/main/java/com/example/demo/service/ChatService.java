package com.example.demo.service;

import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository repository;

    public ChatService(ChatMessageRepository repository) {
        this.repository = repository;
    }

    public ChatMessageEntity saveMessage(ChatMessageDTO dto) {
        ChatMessageEntity entity = new ChatMessageEntity(
                dto.getSender(),
                dto.getMessage(),
                dto.getReceiver(),
                LocalDateTime.now()
        );
        return repository.save(entity);
    }

    public List<ChatMessageEntity> getAllMessages() {
        return repository.findAll();
    }

    public List<ChatMessageEntity> getMessagesByReceiver(String receiver) {
        return repository.findByReceiver(receiver);
    }
}
