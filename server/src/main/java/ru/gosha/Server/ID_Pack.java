package ru.gosha.Server;

import io.netty.channel.ChannelHandlerContext;
import ru.gosha.interpreter.Package;

public class ID_Pack {

    public final ChannelHandlerContext ctx;

    public final Package data;

    public ID_Pack(ChannelHandlerContext ctx, Package data) {
        this.ctx = ctx;
        this.data = data;
    }

}
