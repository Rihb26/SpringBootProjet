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
        message.setRead(false); // Nouveau message est marqué comme "non lu" par défaut

        // Sauvegardez le message
        return messageRepository.save(message);
    }

    public List<Message> getMessages(Long conversationId) {
        // Récupère tous les messages d'une conversation donnée
        return messageRepository.findByConversationId(conversationId);
    }

    public void markMessagesAsRead(Long conversationId) {
        // Récupère tous les messages non lus d'une conversation donnée
        List<Message> unreadMessages = messageRepository.findByConversationIdAndIsReadFalse(conversationId);

        // Marquer les messages comme lus
        for (Message message : unreadMessages) {
            message.setRead(true);
        }

        // Sauvegarder les modifications
        messageRepository.saveAll(unreadMessages);
    }

    public List<Message> getUnreadMessages(Long conversationId) {
        // Récupère tous les messages non lus d'une conversation donnée
        return messageRepository.findByConversationIdAndIsReadFalse(conversationId);
    }
}
