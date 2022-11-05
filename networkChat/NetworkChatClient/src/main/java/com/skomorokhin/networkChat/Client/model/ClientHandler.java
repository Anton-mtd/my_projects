package com.skomorokhin.networkChat.Client.model;

import com.skomorokhin.networkChat.Client.ClientChat;
import com.skomorokhin.networkChat.Client.model.dialogs.Dialogs;
import com.skomorokhin.networkChat.Data.Command;
import com.skomorokhin.networkChat.Data.CommandType;
import com.skomorokhin.networkChat.Data.commands.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    volatile Command outComingCommand;
    volatile Command inComingCommand;
    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        authProcess(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        inComingCommand = (Command) msg;
        readingCommand(ctx);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }


    private void authProcess(ChannelHandlerContext ctx) throws ExecutionException, InterruptedException {
        Callable<Command> authTask = () -> {
            while (true) {
                String login = ClientChat.INSTANCE.getAuthController().getLogin();
                String password = ClientChat.INSTANCE.getAuthController().getPassword();
                if (login != null && !login.isBlank() && password != null && !password.isBlank()) {
                    return Command.authCommand(login, password);
                }
            }
        };
        FutureTask<Command> futureAuthTask = new FutureTask<>(authTask);
        new Thread(futureAuthTask).start();
        outComingCommand = futureAuthTask.get();
        ctx.writeAndFlush(outComingCommand);

        ClientChat.INSTANCE.getAuthController().setLogin(null);
        ClientChat.INSTANCE.getAuthController().setPassword(null);
        outComingCommand = null;
    }


    public void sendingCommand() {
        ctx.writeAndFlush(outComingCommand);
        outComingCommand = null;
    }


    private void readingCommand(ChannelHandlerContext ctx) throws Exception {
        if (inComingCommand.getType().equals(CommandType.ERROR)) {
            ErrorCommandData data = (ErrorCommandData) inComingCommand.getData();
            Platform.runLater(() -> {
                Dialogs.AuthError.INVALID_CREDENTIALS.show(data.getErrorMessage());
            });
            channelActive(ctx);
        }

        else if (inComingCommand.getType() == CommandType.AUTH_OK) {
            AuthOkCommandData data = (AuthOkCommandData) inComingCommand.getData();
            String username = data.getUsername();
            Platform.runLater(() -> ClientChat.INSTANCE.switchToMainChatWindow(username));
            ctx.writeAndFlush(Command.updateUserListCommand(null));
        }

        else if (inComingCommand.getType() == CommandType.UPDATE_USER_LIST) {
            UpdateUserListCommandData data = (UpdateUserListCommandData) inComingCommand.getData();
            ClientChat.INSTANCE.getClientController().updateUserList(data.getUsers());
        }

        else if (inComingCommand.getType() == CommandType.PUBLIC_MESSAGE) {
            PublicMessageCommandData data = (PublicMessageCommandData) inComingCommand.getData();
            if (ClientChat.INSTANCE.getUsername().equals(data.getSender())) {
                ClientChat.INSTANCE.getClientController().displayMessageInChatWindow("Я", data.getMessage(),false);
            } else {
                ClientChat.INSTANCE.getClientController().displayMessageInChatWindow(data.getSender(), data.getMessage(),false);
            }
        }

        else if (inComingCommand.getType() == CommandType.PRIVATE_MESSAGE) {
            StringBuilder prefix = new StringBuilder();
            PrivateMessageCommandData data = (PrivateMessageCommandData) inComingCommand.getData();
            if (ClientChat.INSTANCE.getUsername().equals(data.getSender())) {
                prefix.append("to ");
                prefix.append(data.getRecipient());
                ClientChat.INSTANCE.getClientController().displayMessageInChatWindow(prefix.toString(), data.getMessage(),true);
            } else if (ClientChat.INSTANCE.getUsername().equals(data.getRecipient())) {
                prefix.append("from ");
                prefix.append(data.getSender());
                ClientChat.INSTANCE.getClientController().displayMessageInChatWindow(prefix.toString(), data.getMessage(),true);
            }
        }
    }

    public void close() {

    }

    public void setOutComingCommand(Command outComingCommand) {
        this.outComingCommand = outComingCommand;
    }
}
