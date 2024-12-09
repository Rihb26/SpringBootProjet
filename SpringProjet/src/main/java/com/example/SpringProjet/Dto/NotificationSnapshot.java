package com.example.SpringProjet.Dto;

import java.time.LocalDateTime;

public record NotificationSnapshot(
        Long id,
        Long userId,
        String message,
        boolean isRead,
        LocalDateTime createdAt
) {
}
