package com.example.SpringProjet.Listener;

import com.example.SpringProjet.Event.MessageDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MessageDeletedListener {

    @EventListener
    public void handleMessageDeletedEvent(MessageDeletedEvent event) {
        // Affiche dans la console que le message a été supprimé
        System.out.println("Notification : Le message " + event.getMessageId() + " a été supprimé " );
    }
}
