/**
 * Разработка Handler для сервера. Дочерний файл: https://github.com/Himioshi/numeric_integrator/blob/master/integration-server/src/main/java/pack/IntegrationServerHandler.java
 * Автор: [SG]Muwa Сидоренко Михаил Павлович 2018 г.
 */

package ru.gosha.Server;
import ru.gosha.serverClient.PackageToServer;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Queue;

public class Handler extends ChannelInboundHandlerAdapter {
    private Queue<PackageToServer> qInput;

    Handler(Queue<PackageToServer> queueInput) {
        this.qInput = queueInput;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            ByteBuf message =(ByteBuf) msg;
            try {
                synchronized (qInput) {
                    qInput.add(PackageToServer.fromByteArray(message.array()));
                }
            } catch (ClassNotFoundException error) {return;}

            // Do something with msg
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
