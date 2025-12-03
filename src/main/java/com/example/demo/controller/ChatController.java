package com.example.demo.controller;

import com.example.demo.dto.ChatMessage;
import com.example.demo.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Client gửi /app/chat
    @MessageMapping("/chat")
    public void handlePublicChat(ChatMessage message) {
        chatService.sendPublicMessage(message);
    }

    // Client gửi /app/private
    @MessageMapping("/private")
    public void handlePrivateChat(ChatMessage message) {
        chatService.sendPrivateMessage(message);
    }
}
