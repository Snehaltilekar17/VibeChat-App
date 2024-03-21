package com.example.vibechat.model;

public class Message {
    private String senderId;
    private String senderName;
    private String text;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String senderId, String senderName, String text) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getText() {
        return text;
    }
}
