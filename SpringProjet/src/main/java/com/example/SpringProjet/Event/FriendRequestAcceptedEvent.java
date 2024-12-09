package com.example.SpringProjet.Event;

import org.springframework.context.ApplicationEvent;

public class FriendRequestAcceptedEvent extends ApplicationEvent {

    private final Long receiverId;
    private final Long senderId;

    public FriendRequestAcceptedEvent(Object source, Long receiverId, Long senderId) {
        super(source);
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }
}
