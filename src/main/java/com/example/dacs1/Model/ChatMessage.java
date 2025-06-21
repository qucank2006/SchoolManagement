package com.example.dacs1.Model;

import java.sql.Timestamp;

public class ChatMessage {
    private int id;
    private String senderName;
    private String recipientName;
    private String messageContent;
    private Timestamp sentAt;
    private boolean delivered;

    public ChatMessage(int id, String senderName, String recipientName, String messageContent, Timestamp sentAt, boolean delivered) {
        this.id = id;
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.messageContent = messageContent;
        this.sentAt = sentAt;
        this.delivered = delivered;
    }

    // Constructor không có id (dành cho việc tạo mới)
    public ChatMessage(String senderName, String recipientName, String messageContent) {
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.messageContent = messageContent;
    }

    // Getters
    public int getId() { return id; }
    public String getSenderName() { return senderName; }
    public String getRecipientName() { return recipientName; }
    public String getMessageContent() { return messageContent; }
    public Timestamp getSentAt() { return sentAt; }
    public boolean isDelivered() { return delivered; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }
    public void setSentAt(Timestamp sentAt) { this.sentAt = sentAt; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }
}
