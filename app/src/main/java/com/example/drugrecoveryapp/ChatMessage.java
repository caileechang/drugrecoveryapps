package com.example.drugrecoveryapp;

public class ChatMessage {

    private String senderUid;
    private String message;
    private long timestamp;
    public ChatMessage() {
        // Default constructor required for Firebase
    }

    public ChatMessage(String senderUid, String message) {
        this.senderUid = senderUid;
        this.message = message;
    }

    public String getSenderUid() {
        return senderUid;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }
}
