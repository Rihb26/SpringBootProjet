package com.example.SpringProjet.Service;




import com.example.SpringProjet.Repository.Conversation;
import com.example.SpringProjet.Repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationService {
    private List<Conversation> mockConversations;
    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }


    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }
    public boolean sendMessageToConversation(Long conversationId, String message) {
        // Cette méthode sera simulée dans les tests
        return false; // Placeholder
    }



}
