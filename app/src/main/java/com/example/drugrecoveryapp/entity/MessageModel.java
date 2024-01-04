package com.example.drugrecoveryapp.entity;

public class MessageModel {
    private String messageId;
    private String senderId;
    private String message;
    private long timestamp; // New field for timestamp
    private String sender;
    // Constructor without timestamp (for sending messages)
    public MessageModel(String messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public MessageModel() {
    }

    public MessageModel(String messageId, String senderId) {
        this.messageId = messageId;
        this.senderId = senderId;
    }

    // Constructor with timestamp (for receiving messages)
    public MessageModel(String messageId, String senderId, String message, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getter methods
    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }
}
