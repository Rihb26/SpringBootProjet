package com.example.SpringProjet.Dto;

import java.time.LocalDateTime;

public record ConversationSnapshot(
        Long id,
        String name,
        String lastMessage,
        LocalDateTime lastMessageTime
) {}
