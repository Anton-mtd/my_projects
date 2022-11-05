package com.skomorokhin.networkChat.Data.commands;

import java.io.Serializable;

public class UpdateUserName implements Serializable {
    private final String newUserName;
    private final String previousUserName;

    public UpdateUserName(String PreviousUserName, String NewUserName) {
        this.newUserName = NewUserName;
        this.previousUserName = PreviousUserName;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public String getPreviousUserName() {
        return previousUserName;
    }
}
