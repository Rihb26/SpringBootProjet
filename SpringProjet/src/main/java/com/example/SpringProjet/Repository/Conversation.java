package com.example.SpringProjet.Repository;

import com.example.SpringProjet.Dto.ConversationSnapshot;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String lastMessage;


    private String name;


    private LocalDateTime lastMessageTime;

    @Version
    private Integer version;

    protected Conversation() {
        // Constructeur par défaut requis par JPA
    }

    public Conversation(String name) {
        this.name = name;
        this.lastMessage = "";
        this.lastMessageTime = LocalDateTime.now();
    }

    public void updateLastMessage(String message) {
        this.lastMessage = message;
        this.lastMessageTime = LocalDateTime.now();
    }

    public ConversationSnapshot toSnapshot() {
        return new ConversationSnapshot(id, name, lastMessage, lastMessageTime);
    }


    public Long getId() {
        return this.id;
    }

    public void setId(  Long conversationId) {
        this.id = conversationId;
    }
}
