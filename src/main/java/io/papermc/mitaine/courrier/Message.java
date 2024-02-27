package io.papermc.mitaine.courrier;

import java.util.UUID;

public class Message {
    private UUID sender;
    private String message;

    public Message(UUID sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
