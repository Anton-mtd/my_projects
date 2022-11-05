package com.skomorokhin.networkChat.Client.model;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Network {

    public static final int SERVER_PORT = 8189;
    public static final String SERVER_HOST = "localhost";

    private static Network INSTANCE;

    private ClientHandler clientHandler;
    private boolean connected;

    public Network() {
        connect();
    }

    private void connect() {
        new Thread(() -> {
            clientHandler = new ClientHandler();
            EventLoopGroup workClientThread = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workClientThread)
                        .channel(NioSocketChannel.class)
                        .remoteAddress(SERVER_HOST, SERVER_PORT)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new ObjectEncoder());
                                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                                socketChannel.pipeline().addLast(clientHandler);
                            }
                        });
                ChannelFuture f = b.connect().sync();
                connected = true;
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("Не удалось установить соединение");
            } finally {
                workClientThread.shutdownGracefully();
            }
        }).start();
    }

    public static Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();
        }
        return INSTANCE;
    }


    public boolean isConnected() {
        return connected;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}
