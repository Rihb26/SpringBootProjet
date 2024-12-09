package com.example.SpringProjet.Controller;



import com.example.SpringProjet.Repository.Message;
import com.example.SpringProjet.Service.MessageService;
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

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestParam Long conversationId,
            @RequestParam Long senderId,
            @RequestParam String content) {
        try {
            Message message = messageService.sendMessage(conversationId, senderId, content);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long conversationId) {
        List<Message> messages = messageService.getMessages(conversationId);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(
            @PathVariable Long id,
            @RequestBody String newContent) {
        Message updatedMessage = messageService.updateMessage(id, newContent);
        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok("Message deleted successfully");
    }
}
