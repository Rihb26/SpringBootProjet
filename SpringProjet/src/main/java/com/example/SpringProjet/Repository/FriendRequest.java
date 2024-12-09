package com.example.SpringProjet.Repository;

import com.example.SpringProjet.Dto.FriendRequestSnapshot;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friend_requests")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, ACCEPTED, REJECTED

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected FriendRequest() {
        // Constructeur requis par JPA
    }

    public FriendRequest(Long senderId, Long receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public void accept() {
        if (!"PENDING".equals(this.status)) {
            throw new IllegalArgumentException("La demande ne peut être acceptée que si elle est en attente.");
        }
        this.status = "ACCEPTED";
    }

    public void reject() {
        if (!"PENDING".equals(this.status)) {
            throw new IllegalArgumentException("La demande ne peut être rejetée que si elle est en attente.");
        }
        this.status = "REJECTED";
    }

    public FriendRequestSnapshot toSnapshot() {
        return new FriendRequestSnapshot(id, senderId, receiverId, status, createdAt);
    }
}
