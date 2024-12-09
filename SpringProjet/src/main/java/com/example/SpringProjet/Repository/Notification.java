package com.example.SpringProjet.Repository;

import com.example.SpringProjet.Dto.NotificationSnapshot;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // L'utilisateur qui reçoit la notification

    @Column(nullable = false)
    private String message; // Le message de notification

    @Column(nullable = false)
    private boolean isRead = false; // Indique si la notification est lue

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Date de création de la notification

    // Constructeurs (obligatoire pour Hibernate)
    protected Notification() {
    }

    public Notification(Long userId, String message) {
        this.userId = userId;
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public NotificationSnapshot toSnapshot() {
        return new NotificationSnapshot(id, userId, message, isRead, createdAt);
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
