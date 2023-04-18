package com.skomorokhin.cloudServer;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

public class CloudServer {

    private final int PORT = 8189;
    private int port;

    public CloudServer(int port) {
        this.port = port;
    }

    public CloudServer() {
        this.port = PORT;
    }

    public static void main(String[] args) {
        if (args.length != 0){
            new CloudServer(Integer.parseInt(args[0])).start();
        } else {
            new CloudServer().start();
        }
    }

    private void start() {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workClientThread = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(mainGroup, workClientThread)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // Максимальная длина
                            ch.pipeline().addLast(new CloudServerHandler());
                        }
                    });
            ChannelFuture future = b.bind().sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            workClientThread.shutdownGracefully();
        }
    }
}
