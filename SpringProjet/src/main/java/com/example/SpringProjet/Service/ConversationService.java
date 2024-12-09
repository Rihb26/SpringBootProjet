package com.example.SpringProjet.Service;


import com.example.SpringProjet.Dto.ConversationSnapshot;
import com.example.SpringProjet.Repository.Conversation;
import com.example.SpringProjet.Repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    public ConversationSnapshot createConversation(String name) {
        Conversation conversation = new Conversation(name);
        conversationRepository.save(conversation);
        return conversation.toSnapshot();
    }

    public List<ConversationSnapshot> getAllConversations() {
        return conversationRepository.findAll()
                .stream()
                .map(Conversation::toSnapshot)
                .collect(Collectors.toList());
    }

    public void updateLastMessage(Long id, String message) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversation introuvable"));
        conversation.updateLastMessage(message);
        conversationRepository.save(conversation);
    }
    public Conversation saveConversation(Conversation conversation) {
        System.out.println("Conversation avant sauvegarde : " + conversation);
        return conversationRepository.save(conversation);
    }

}
