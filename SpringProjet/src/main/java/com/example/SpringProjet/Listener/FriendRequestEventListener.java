package com.example.SpringProjet.Listener;

import com.example.SpringProjet.Event.FriendRequestAcceptedEvent;
import com.example.SpringProjet.Event.FriendRequestReceivedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestEventListener {

    @EventListener
    public void handleFriendRequestReceived(FriendRequestReceivedEvent event) {
        System.out.println("Notification : L'utilisateur " + event.getReceiverId() +
                " a reçu une demande d’ami de l’utilisateur " + event.getSenderId());
    }

    @EventListener
    public void handleFriendRequestAccepted(FriendRequestAcceptedEvent event) {
        System.out.println("Notification : L'utilisateur " + event.getSenderId() +
                " a accepté la demande d’ami de l’utilisateur " + event.getReceiverId());
    }
}
