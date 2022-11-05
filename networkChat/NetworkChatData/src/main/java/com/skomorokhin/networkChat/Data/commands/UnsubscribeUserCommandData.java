package com.skomorokhin.networkChat.Data.commands;

import java.io.Serializable;

public class UnsubscribeUserCommandData implements Serializable {

    private final String userName;

    public UnsubscribeUserCommandData(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
