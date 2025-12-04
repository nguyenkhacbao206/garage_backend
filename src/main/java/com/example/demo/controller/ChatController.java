package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void sendMessage(ChatMessageDTO dto) {
        // Lưu Mongo
        chatService.saveMessage(dto);

        // Gửi WebSocket
        if(dto.getReceiver() == null || dto.getReceiver().isEmpty()) {
            // chat public
            messagingTemplate.convertAndSend("/topic/messages", dto);
        } else {
            // chat riêng
            messagingTemplate.convertAndSend("/topic/private." + dto.getReceiver(), dto);
        }
    }

    @GetMapping("/history")
    public List<ChatMessageEntity> getAllMessages() {
        return chatService.getAllMessages();
    }

    @GetMapping("/history/{receiver}")
    public List<ChatMessageEntity> getMessagesByReceiver(@PathVariable String receiver) {
        return chatService.getMessagesByReceiver(receiver);
    }
}
