package com.example.SpringProjet.Service;

import com.example.SpringProjet.Event.MessageDeletedEvent;
import com.example.SpringProjet.Event.MessageUpdatedEvent;
import com.example.SpringProjet.Repository.Conversation;
import com.example.SpringProjet.Repository.ConversationRepository;
import com.example.SpringProjet.Repository.Message;
import com.example.SpringProjet.Repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public MessageService(MessageRepository messageRepository, ConversationRepository conversationRepository, ApplicationEventPublisher eventPublisher) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.eventPublisher = eventPublisher;
    }

    public Message sendMessage(Long conversationId, Long senderId, String content) {
        logger.info("Envoi d'un message : conversationId={}, senderId={}, content={}", conversationId, senderId, content);

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> {
                    logger.error("La conversation avec ID {} n'existe pas.", conversationId);
                    return new IllegalArgumentException("La conversation n'existe pas.");
                });

        Message message = new Message();
        message.setConversation(conversation);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setRead(false);

        Message savedMessage = messageRepository.save(message);
        logger.info("Message envoyé avec succès : messageId={}", savedMessage.getId());
        return savedMessage;
    }

    public List<Message> getMessages(Long conversationId) {
        logger.info("Récupération des messages pour la conversationId={}", conversationId);

        List<Message> messages = messageRepository.findByConversationId(conversationId);
        logger.info("Nombre de messages récupérés : {}", messages.size());
        return messages;
    }

    public void markMessagesAsRead(Long conversationId) {
        logger.info("Marquage des messages comme lus pour la conversationId={}", conversationId);

        List<Message> unreadMessages = messageRepository.findByConversationIdAndIsReadFalse(conversationId);
        if (unreadMessages.isEmpty()) {
            logger.info("Aucun message non lu trouvé pour la conversationId={}", conversationId);
            return;
        }

        for (Message message : unreadMessages) {
            message.setRead(true);
        }

        messageRepository.saveAll(unreadMessages);
        logger.info("{} messages marqués comme lus pour la conversationId={}", unreadMessages.size(), conversationId);
    }

    public List<Message> getUnreadMessages(Long conversationId) {
        logger.info("Récupération des messages non lus pour la conversationId={}", conversationId);

        List<Message> unreadMessages = messageRepository.findByConversationIdAndIsReadFalse(conversationId);
        logger.info("Nombre de messages non lus récupérés : {}", unreadMessages.size());
        return unreadMessages;
    }

    public Message updateMessage(Long id, String newContent) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        String oldContent = message.getContent();
        message.setContent(newContent);

        message.setContent(newContent);

        Message updatedMessage = messageRepository.save(message);
        eventPublisher.publishEvent(new MessageUpdatedEvent(this, message.getId(), oldContent, newContent));
        return updatedMessage;
    }

    public void deleteMessage(Long id) {


        Message message = messageRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Le message avec ID {} n'a pas été trouvé.", id);
                    return new RuntimeException("Message not found with ID: " + id);
                });

        // Supprimer le message
        messageRepository.deleteById(id);

        // Publier l'événement pour signaler que le message a été supprimé
        eventPublisher.publishEvent(new MessageDeletedEvent(this, id));


    }

}
