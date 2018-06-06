package ru.gosha;

import ru.gosha.Server.Server;
import ru.gosha.Server.TaskExecutor;
import ru.gosha.serverClient.PackageToClient;
import ru.gosha.serverClient.PackageToServer;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static int threadNumber = 10;

    public static void main(String[] args) throws Exception
    {
        Queue<PackageToServer> qIn = new LinkedBlockingQueue<PackageToServer>();
        Queue<PackageToClient> qOut = new ArrayDeque<PackageToClient>();

        TaskExecutor taskExecutor = new TaskExecutor(qIn, qOut);
        Thread[] threadExecutorArr = new Thread[threadNumber];
        for (int i=0; i<threadNumber;++i)
            threadExecutorArr[i] = new Thread(taskExecutor);

        for (int i=0; i<threadNumber;++i)
            threadExecutorArr[i].start();

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


