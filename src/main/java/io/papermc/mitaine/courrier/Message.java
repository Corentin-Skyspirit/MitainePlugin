package io.papermc.mitaine.courrier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Message {
    private String sender;
    private String date;
    private String message;

    public Message(String sender, String date, String message) {
        this.sender = sender;
        this.date = date;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
