package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
public class ChatMessageEntity {

    @Id
    private String id;

    private String sender;
    private String message;
    private String receiver;  // có thể null
    private LocalDateTime timestamp;

    public ChatMessageEntity() {}

    public ChatMessageEntity(String sender, String message, String receiver, LocalDateTime timestamp) {
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
