package com.skomorokhin.networkChat.Server;

import com.skomorokhin.networkChat.Data.Command;
import com.skomorokhin.networkChat.Data.CommandType;
import com.skomorokhin.networkChat.Data.commands.AuthCommandData;
import com.skomorokhin.networkChat.Data.commands.PrivateMessageCommandData;
import com.skomorokhin.networkChat.Data.commands.UnsubscribeUserCommandData;
import com.skomorokhin.networkChat.Data.commands.UpdateUserName;
import com.skomorokhin.networkChat.Server.auth.IAuthService;
import com.skomorokhin.networkChat.Server.auth.PersistentDbAuthService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

public class ServerAppHandler extends ChannelInboundHandlerAdapter {

    private IAuthService authService;



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился к серверу");
        authService = new PersistentDbAuthService();
        authService.start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Command command = (Command) msg;
        if (command.getType().equals(CommandType.AUTH)) {
            authenticating(command, ctx);
        }

        else if (command.getType().equals(CommandType.PUBLIC_MESSAGE)) {
            for (Channel client : ServerApp.getSERVER().getClients().keySet()) {
                client.writeAndFlush(command);
            }
        }

        else if (command.getType().equals(CommandType.PRIVATE_MESSAGE)) {
            PrivateMessageCommandData data = (PrivateMessageCommandData) command.getData();
            for (Map.Entry client : ServerApp.getSERVER().getClients().entrySet()) {
                if (client.getValue().equals(data.getRecipient())) {
                    Channel channel = (Channel) client.getKey();
                    channel.writeAndFlush(command);
                } else if (client.getValue().equals(data.getSender())) {
                    Channel channel = (Channel) client.getKey();
                    channel.writeAndFlush(command);
                }
            }
        }

        else if (command.getType().equals(CommandType.UPDATE_USER_LIST)) {
            for (Channel client : ServerApp.getSERVER().getClients().keySet()) {
                client.writeAndFlush(Command.updateUserListCommand(ServerApp.getSERVER().getListClientsNames()));
            }
        }

        else if (command.getType().equals(CommandType.UNSUBSCRIBE_USER)) {
            UnsubscribeUserCommandData data = (UnsubscribeUserCommandData) command.getData();
            ServerApp.getSERVER().unsubscribe(data.getUserName());
        }

        else if (command.getType().equals(CommandType.UPDATE_USERNAME)) {
            UpdateUserName data = (UpdateUserName) command.getData();
            ServerApp.getSERVER().updateUserName(data.getPreviousUserName(), data.getNewUserName());
            authService.updateUsername(data.getPreviousUserName(), data.getNewUserName());
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel() + " " + cause.getMessage());
    }


    private void authenticating(Command command, ChannelHandlerContext ctx) {
        AuthCommandData data = (AuthCommandData) command.getData();

        String login = data.getLogin();
        String password = data.getPassword();
        String userName = authService.getUserNameByLoginAndPassword(login, password);

        if (userName == null) {
            ctx.writeAndFlush(Command.errorCommand("Некорректные логин и пароль"));
        } else if (ServerApp.getSERVER().isUsernameBusy(userName)) {
            ctx.writeAndFlush(Command.errorCommand("Такой пользователь уже существует!"));
        } else  {
            ServerApp.getSERVER().subscribe(ctx, userName);
            ctx.writeAndFlush(Command.authOkCommand(userName));
        }
    }

    public IAuthService getAuthService() {
        return authService;
    }
}
