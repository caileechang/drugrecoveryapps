package com.example.drugrecoveryapp.entity;

public class ChatMessage {

    private String userId;
    private String message;

    public ChatMessage() {
        // Required empty constructor for Firebase
    }

    public ChatMessage(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
