package com.example.SpringProjet.Controller;



import com.example.SpringProjet.Repository.Message;
import com.example.SpringProjet.Repository.User;
import com.example.SpringProjet.Repository.UserRepository;
import com.example.SpringProjet.Service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestParam Long conversationId,
            @RequestParam String content) {
        // Récupère l'utilisateur connecté via le contexte de sécurité
        String senderUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Utilise l'instance injectée de UserRepository
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Appelle le service avec l'ID du sender
        Message message = messageService.sendMessage(conversationId, sender.getId(), content);
        return ResponseEntity.ok(message);
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
