package com.example.SpringProjet.Service;



import com.example.SpringProjet.Repository.Conversation;
import com.example.SpringProjet.Repository.ConversationRepository;
import com.example.SpringProjet.Repository.Message;
import com.example.SpringProjet.Repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessageService(MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    public Message sendMessage(Long conversationId, Long senderId, String content) {
        // Vérifiez que la conversation existe
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("La conversation n'existe pas."));

        // Créez un nouveau message
        Message message = new Message();
        message.setConversation(conversation);
        message.setSenderId(senderId);
        message.setContent(content);

        // Sauvegardez le message
        return messageRepository.save(message);
    }

    public List<Message> getMessages(Long conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }
}
