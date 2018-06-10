package ru.gosha.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Sender implements Runnable, Closeable {

    Queue<ID_Pack> qOut;

    public Sender(Queue<ID_Pack> qOut) {
        this.qOut = qOut;
    }

    List<Thread> currentThread = new LinkedList<>();

    @Override
    public void run()
    {
        synchronized (currentThread) {currentThread.add(Thread.currentThread()); }
        while(!Thread.currentThread().isInterrupted())
        {
            ID_Pack pack = null;
            synchronized(qOut)
            {
                pack = qOut.poll();
            }
            if (pack!=null)
            {
                System.out.println("Send.");
                ByteBuf out = Unpooled.wrappedBuffer(pack.data.toByteArray());
                pack.ctx.writeAndFlush(out);
            }
            else
                try{Thread.sleep(10);}catch (InterruptedException e){e.printStackTrace();}
        }
    }

    @Override
    public void close()
    {
        synchronized (currentThread) {
            for(Thread i : currentThread)
                i.interrupt();
        }
    }
}
