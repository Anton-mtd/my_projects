package com.skomorokhin.networkChat.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerApp {

    private static ServerApp SERVER;

    private static final int DEFAULT_PORT = 8189;
    private int port;
    private final Map<Channel, String> clients = new HashMap<Channel, String>();


    public ServerApp(int port) {
        this.port = port;
    }

    public ServerApp() {
        this.port = DEFAULT_PORT;
    }


    public static ServerApp getSERVER() {
        return SERVER;
    }


    public static void main(String[] args) {
        if (args.length != 0) {
            new ServerApp(Integer.parseInt(args[0])).start();
        } else
            SERVER = new ServerApp();
        SERVER.start();
    }

    public void start() {
        EventLoopGroup mainGroup = new NioEventLoopGroup(1);
        EventLoopGroup workClientThread = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(mainGroup, workClientThread)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ObjectEncoder());
                            socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            socketChannel.pipeline().addLast(new ServerAppHandler());
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            workClientThread.shutdownGracefully();
        }
    }

    public synchronized boolean isUsernameBusy(String userName) {
        for (String user : clients.values()) {
            if (user.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void subscribe(ChannelHandlerContext ctx, String name) {
        clients.put(ctx.channel(), name);
    }

    public synchronized void unsubscribe(String name) {
        clients.entrySet().removeIf(entry -> entry.getValue().equals(name));
    }

    public List<String> getListClientsNames() {
        return new ArrayList<String>(clients.values());
    }


    public Map<Channel, String> getClients() {
        return clients;
    }

    public void updateUserName(String previousUserName, String newUserName) {
        for (Map.Entry<Channel, String> entry : clients.entrySet()) {
            if (entry.getValue().equals(previousUserName)) {
                entry.setValue(newUserName);
            }
        }
    }
}
