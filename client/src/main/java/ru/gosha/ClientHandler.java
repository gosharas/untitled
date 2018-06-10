package ru.gosha;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.gosha.interpreter.PackageToClient;
import ru.gosha.interpreter.PackageToServer;


import java.util.Queue;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private Queue<PackageToClient> qInput;

    ClientHandler(Queue<PackageToClient> qInput) {this.qInput = qInput;}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            ByteBuf message =(ByteBuf) msg;
            try {
                synchronized (qInput) {
                    qInput.add(PackageToClient.fromByteArray(message.array()));
                }
            } catch (ClassNotFoundException error) {return;}

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
