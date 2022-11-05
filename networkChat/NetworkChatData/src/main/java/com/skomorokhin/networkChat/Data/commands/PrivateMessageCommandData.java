package com.skomorokhin.networkChat.Data.commands;

import java.io.Serializable;

public class PrivateMessageCommandData implements Serializable {

    private final String sender;
    private final String recipient;
    private final String message;

    public PrivateMessageCommandData(String sender, String recipient, String message) {
        this.recipient = recipient;
        this.sender = sender;
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

}
