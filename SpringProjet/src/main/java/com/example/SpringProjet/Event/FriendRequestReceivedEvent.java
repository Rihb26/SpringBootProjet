package com.example.SpringProjet.Event;

import org.springframework.context.ApplicationEvent;

public class FriendRequestReceivedEvent extends ApplicationEvent {

    private final Long receiverId;
    private final Long senderId;

    public FriendRequestReceivedEvent(Object source, Long receiverId, Long senderId) {
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
