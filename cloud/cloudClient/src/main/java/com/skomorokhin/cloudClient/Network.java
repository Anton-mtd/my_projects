package com.skomorokhin.cloudClient;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

public class Network {

    private final String HOST = "localhost";
    private final int PORT = 8189;
    private static volatile Network INSTANCE;
    private ClientHandler clientHandler;
    private TransferFile transferFile;


    private Network() {
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
                        .option(ChannelOption.TCP_NODELAY, true)
                        .remoteAddress(new InetSocketAddress(HOST, PORT))
                        .handler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel channel) throws Exception {
                                channel.pipeline().addLast(new ObjectEncoder());
                                channel.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                                channel.pipeline().addLast(clientHandler);
                            }
                        });
                ChannelFuture future = b.connect().sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workClientThread.shutdownGracefully();
            }

        }).start();


    }

    public TransferFile getTransferFile() {
        return transferFile;
    }

    public void setTransferFile(TransferFile transferFile) {
        this.transferFile = transferFile;
    }

    public static Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();
        }
        return INSTANCE;
    }

}
