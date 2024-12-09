package com.example.SpringProjet.Repository;

import com.example.SpringProjet.Dto.MessageSnapshot;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public Message(Conversation conversation, Long senderId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu ne peut pas être vide.");
        }
        if (conversation == null) {
            throw new IllegalArgumentException("La conversation est obligatoire.");
        }
        this.conversation = conversation;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Message() {

    }


    public MessageSnapshot toSnapshot() {
        return new MessageSnapshot(
                id,
                conversation.getId(),
                senderId,
                content,
                createdAt
        );
    }

    public void updateContent(String newContent) {
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu mis à jour ne peut pas être vide.");
        }
        this.content = newContent;
    }
}
