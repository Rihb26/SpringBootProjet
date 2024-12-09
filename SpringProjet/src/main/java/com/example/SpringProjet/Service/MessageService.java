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

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("La conversation n'existe pas."));


        Message message = new Message();
        message.setConversation(conversation);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setRead(false);


        return messageRepository.save(message);
    }

    public List<Message> getMessages(Long conversationId) {

        return messageRepository.findByConversationId(conversationId);
    }

    public void markMessagesAsRead(Long conversationId) {

        List<Message> unreadMessages = messageRepository.findByConversationIdAndIsReadFalse(conversationId);


        for (Message message : unreadMessages) {
            message.setRead(true);
        }


        messageRepository.saveAll(unreadMessages);
    }

    public List<Message> getUnreadMessages(Long conversationId) {

        return messageRepository.findByConversationIdAndIsReadFalse(conversationId);
    }

    public Message updateMessage(Long id, String newContent) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));


        message.setContent(newContent);


        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + id));


        messageRepository.deleteById(id);
    }
}
