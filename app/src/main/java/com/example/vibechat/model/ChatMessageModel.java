package com.example.vibechat.model;

import com.google.firebase.Timestamp;

public class ChatMessageModel {
    private String messageId; // Field to store the document ID
    private String message;
    private String senderId;
    private Timestamp timestamp;
    private boolean isSelected; // New field to track selection state

    public ChatMessageModel() {
    }

    public ChatMessageModel(String message, String senderId, Timestamp timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public ChatMessageModel(String messageId, String message, String senderId, Timestamp timestamp) {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public interface OnMessageLongClickListener {
        void onMessageLongClick(ChatMessageModel message, int position);
    }
}
