package com.example.SpringProjet.Event;

import org.springframework.context.ApplicationEvent;

public class MessageDeletedEvent extends ApplicationEvent {


    private final Long messageId; // Celui qui a supprimé le message

    public MessageDeletedEvent(Object source ,Long messageId) {
        super(source);

        this.messageId = messageId;
    }



    public Long getMessageId() {
        return messageId;
    }


}
