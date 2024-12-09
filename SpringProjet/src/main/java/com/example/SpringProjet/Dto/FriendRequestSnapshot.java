package com.example.SpringProjet.Dto;

import java.time.LocalDateTime;

public record FriendRequestSnapshot(
        Long id,
        Long senderId,
        Long receiverId,
        String status,
        LocalDateTime createdAt
) {}
