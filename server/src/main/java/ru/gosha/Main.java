package ru.gosha;

import ru.gosha.Server.ID_Pack;
import ru.gosha.Server.Sender;
import ru.gosha.Server.Server;
import ru.gosha.Server.TaskExecutor;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static int threadNumber = 10;

    public static void main(String[] args) throws Exception
    {
        Queue<ID_Pack> qIn = new LinkedBlockingQueue<ID_Pack>();
        Queue<ID_Pack> qOut = new ArrayDeque<ID_Pack>();

        Sender sender = new Sender(qOut);
        Thread senderTh = new Thread(sender);

        TaskExecutor taskExecutor = new TaskExecutor(qIn, qOut);
        Thread[] threadExecutorArr = new Thread[threadNumber];
        for (int i=0; i<threadNumber;++i)
            threadExecutorArr[i] = new Thread(taskExecutor);

        for (int i=0; i<threadNumber;++i)
            threadExecutorArr[i].start();

        senderTh.start();

        int port;
        if (args.length > 0)
        {
            port = Integer.parseInt(args[0]);
        }
        else
        {
            port = 60101;
        }
        new Server(port, qIn).start();
    }
}


