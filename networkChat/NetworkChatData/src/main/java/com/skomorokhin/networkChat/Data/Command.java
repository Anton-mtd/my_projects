package com.skomorokhin.networkChat.Data;

import com.skomorokhin.networkChat.Data.commands.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

    private Object data;
    private CommandType type;

    public Object getData() {
        return data;
    }

    public CommandType getType() {
        return type;
    }

    public static Command authCommand(String login, String password) {
        Command command = new Command();
        command.data = new AuthCommandData(login, password);
        command.type = CommandType.AUTH;
        return command;
    }

    public static Command errorCommand(String errorMessage) {
        Command command = new Command();
        command.type = CommandType.ERROR;
        command.data = new ErrorCommandData(errorMessage);
        return command;
    }

    public static Command authOkCommand(String username) {
        Command command = new Command();
        command.data = new AuthOkCommandData(username);
        command.type = CommandType.AUTH_OK;
        return command;
    }

    public static Command updateUserListCommand(List<String> users) {
        Command command = new Command();
        command.type = CommandType.UPDATE_USER_LIST;
        command.data = new UpdateUserListCommandData(users);
        return command;
    }

    public static Command privateMessageCommand(String sender, String recipient, String message) {
        Command command = new Command();
        command.type = CommandType.PRIVATE_MESSAGE;
        command.data = new PrivateMessageCommandData(sender, recipient, message);
        return command;
    }

    public static Command publicMessageCommand(String sender, String message) {
        Command command = new Command();
        command.type = CommandType.PUBLIC_MESSAGE;
        command.data = new PublicMessageCommandData(sender, message);
        return command;
    }

    public static Command unsubscribeUserCommand(String userName) {
        Command command = new Command();
        command.type = CommandType.UNSUBSCRIBE_USER;
        command.data = new UnsubscribeUserCommandData(userName);
        return command;
    }

    public static Command updateUsername(String previousUserName, String newUserName) {
        Command command = new Command();
        command.type = CommandType.UPDATE_USERNAME;
        command.data = new UpdateUserName(previousUserName, newUserName);
        return command;
    }
}
