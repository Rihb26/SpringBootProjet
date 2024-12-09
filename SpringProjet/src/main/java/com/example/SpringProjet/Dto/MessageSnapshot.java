package com.example.SpringProjet.Dto;

import java.time.LocalDateTime;

public class MessageSnapshot {

    private final Long id;
    private final Long conversationId;
    private final Long senderId;
    private final String content;
    private final LocalDateTime createdAt;

    public MessageSnapshot(Long id, Long conversationId, Long senderId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters immuables pour exposer les données (pas setters)
    public Long getId() {
        return id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

