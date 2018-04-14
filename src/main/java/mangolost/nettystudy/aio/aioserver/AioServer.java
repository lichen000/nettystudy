package mangolost.nettystudy.aio.aioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class AioServer {

    private final int port;

    public static volatile int clientCount = 0; //客户端数量

    public CountDownLatch latch;
    public AsynchronousServerSocketChannel channel;

    public AioServer(int port) {
        this.port = port;
    }

    /**
     *
     */
    public void start() {
        try {
            channel = AsynchronousServerSocketChannel.open();
            channel.bind(new InetSocketAddress(port)); //绑定端口
            System.out.println("AIO Server is running on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        latch = new CountDownLatch(1);
        //用于接收客户端的连接
        channel.accept(this, new AioAcceptHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
