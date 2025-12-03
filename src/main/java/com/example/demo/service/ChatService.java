package com.example.demo.service;

import com.example.demo.dto.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Gửi tin tới tất cả (chat room)
    public void sendPublicMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }

    // Gửi tin riêng
    public void sendPrivateMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/queue/" + message.getReceiver(), message);
    }
}
