package com.example.SpringProjet.Service;



import com.example.SpringProjet.Dto.MessageSnapshot;
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

    public MessageSnapshot sendMessage(Long conversationId, Long senderId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation inexistante."));

        Message message = new Message(conversation, senderId, content);
        Message savedMessage = messageRepository.save(message);

        return savedMessage.toSnapshot();
    }

    public List<MessageSnapshot> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(Message::toSnapshot)
                .toList();
    }
}
