package ru.gosha;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import ru.gosha.interpreter.PackageToClient;
import ru.gosha.interpreter.PackageToServer;
import ru.gosha.interpreter.Seeker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JFileChooser;


public class Client {
    private final String host;
    private final int port;

    private Queue<PackageToClient> qIn;

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        Queue<PackageToServer> qOut = new LinkedBlockingQueue<PackageToServer>();
        Queue<PackageToClient> qIn = new ArrayDeque<PackageToClient>();

        JFileChooser chooser = new JFileChooser();
        //chooser.setCurrentDirectory(new File("C:\\Users\\nomid\\Desktop\\Transfer\\out_file"));
        int send = chooser.showDialog(null, "Send");
        System.out.println(chooser.getSelectedFile().getPath());
        //File file = new File(chooser.getSelectedFile().getPath());
        byte[] array = Files.readAllBytes(Paths.get(chooser.getSelectedFile().getPath()));
        InputSeeker seeker = new InputSeeker();
        Seeker seeker1 = seeker.setSeeker();
        //System.out.println(seeker.setSeeker());
        System.out.println(seeker1.seekerType);
        //System.out.println(array);

        PackageToServer server = new PackageToServer(new byte[][]{array} , seeker1);


        new Client("localhost", 60101).run();
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException, IOException {
        EventLoopGroup wgroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(wgroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>()
                            {
                @Override
                public void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline().addLast(new ClientHandler(qIn));
                }
            });


            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();

        } finally {
            wgroup.shutdownGracefully();
        }
    }

}
