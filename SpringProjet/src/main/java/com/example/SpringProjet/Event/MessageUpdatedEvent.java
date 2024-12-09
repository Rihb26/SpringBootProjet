package com.example.SpringProjet.Event;

import org.springframework.context.ApplicationEvent;

public class MessageUpdatedEvent extends ApplicationEvent {

    private final Long messageId;
    private final String oldContent;
    private final String newContent;

    public MessageUpdatedEvent(Object source, Long messageId, String oldContent, String newContent) {
        super(source);
        this.messageId = messageId;
        this.oldContent = oldContent;
        this.newContent = newContent;
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getOldContent() {
        return oldContent;
    }

    public String getNewContent() {
        return newContent;
    }
}
