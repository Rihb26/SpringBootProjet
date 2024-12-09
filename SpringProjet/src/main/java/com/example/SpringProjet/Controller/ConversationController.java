package com.example.SpringProjet.Controller;

import com.example.SpringProjet.Dto.ConversationSnapshot;
import com.example.SpringProjet.Repository.Conversation;
import com.example.SpringProjet.Repository.ConversationRepository;
import com.example.SpringProjet.Service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;



    @PostMapping
    public ResponseEntity<Conversation> createConversation(@RequestBody Conversation newConversation) {
        System.out.println("Conversation reçue : " + newConversation); // Log des données reçues
        Conversation conversation = conversationService.saveConversation(newConversation);
        return ResponseEntity.ok(conversation);
    }



    @GetMapping
    public List<ConversationSnapshot> getAllConversations() {
        return conversationService.getAllConversations();
    }

    @PutMapping("/{id}/last-message")
    public String updateLastMessage(@PathVariable Long id, @RequestBody String message) {
        conversationService.updateLastMessage(id, message);
        return "Message mis à jour avec succès.";
    }


}
