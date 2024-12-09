package com.example.SpringProjet.Controller;

import com.example.SpringProjet.Service.MessageService;
import com.example.SpringProjet.Dto.MessageSnapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Endpoint pour envoyer un message
    @PostMapping("/send")
    public ResponseEntity<MessageSnapshot> sendMessage(
            @RequestParam Long conversationId,
            @RequestParam Long senderId,
            @RequestParam String content) {
        try {
            MessageSnapshot messageSnapshot = messageService.sendMessage(conversationId, senderId, content);
            return ResponseEntity.ok(messageSnapshot);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint pour récupérer les messages d'une conversation
    @GetMapping("/{conversationId}")
    public ResponseEntity<List<MessageSnapshot>> getMessages(@PathVariable Long conversationId) {
        List<MessageSnapshot> messages = messageService.getMessages(conversationId);
        return ResponseEntity.ok(messages);
    }
}

