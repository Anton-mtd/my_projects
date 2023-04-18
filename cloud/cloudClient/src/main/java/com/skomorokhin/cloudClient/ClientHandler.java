package com.skomorokhin.cloudClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    private int byteRead;
    private volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    private TransferFile transferFile = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        try {

            Callable task = () -> {
                while (true) {
                    if (Network.getInstance().getTransferFile() != null)
                        return Network.getInstance().getTransferFile();
                }
            };
            Network.getInstance().setTransferFile(null);
            FutureTask<TransferFile> futureTask = new FutureTask<>(task);
            new Thread(futureTask).start();
            transferFile = futureTask.get();

            File file = transferFile.getFile();
            randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(transferFile.getStarPos());
            lastLength = (int) randomAccessFile.length();
            byte[] bytes = new byte[lastLength];
            byteRead = randomAccessFile.read(bytes);
            transferFile.setEndPos(byteRead);
            transferFile.setBytes(bytes);
            ctx.writeAndFlush(transferFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channelActive(ctx);
    }
}
