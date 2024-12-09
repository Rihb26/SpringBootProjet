package com.example.SpringProjet.Listener;

import com.example.SpringProjet.Event.MessageUpdatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MessageUpdatedListener implements ApplicationListener<MessageUpdatedEvent> {

    @Override
    public void onApplicationEvent(MessageUpdatedEvent event) {
        System.out.println("Message avec ID " + event.getMessageId() + " a été mis à jour.");
        System.out.println("Ancien contenu : " + event.getOldContent());
        System.out.println("Nouveau contenu : " + event.getNewContent());
    }
}
