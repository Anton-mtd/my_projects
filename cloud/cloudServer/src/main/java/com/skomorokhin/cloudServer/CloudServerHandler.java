package com.skomorokhin.cloudServer;


import com.skomorokhin.cloudClient.TransferFile;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.File;
import java.io.RandomAccessFile;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {

    private int byteRead;
    private volatile int start = 0;
    private static String fileDir;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TransferFile) {
            TransferFile tF = (TransferFile) msg;
            fileDir = tF.getDest();
            byte[] bytes = tF.getBytes();
            byteRead = tF.getEndPos();
            String md5 = tF.getFile_md5();//имя файла
            String path = fileDir + File.separator + md5;
            File file = new File(path);

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            randomAccessFile.close();
            ctx.writeAndFlush("repeat");
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
