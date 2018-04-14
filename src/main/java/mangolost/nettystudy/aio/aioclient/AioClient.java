package mangolost.nettystudy.aio.aioclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class AioClient implements CompletionHandler<Void, AioClient> {

    private final String host;
    private final int port;

    private CountDownLatch latch;
    private AsynchronousSocketChannel clientChannel;

    public AioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     *
     */
    public void start() {
        try {
            clientChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        latch = new CountDownLatch(1);
        clientChannel.connect(new InetSocketAddress(host, port), this, this);
        try {
            latch.await();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        byte[] req = msg.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        //异步写
        clientChannel.write(writeBuffer, writeBuffer, new AioWriteHandler(clientChannel, latch));
    }

    /**
     *
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Void result, AioClient attachment) {
        System.out.println("客户端成功连接到服务器...");
        sendMsg("Hello World");
    }

    /**
     *
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, AioClient attachment) {
        System.err.println("连接服务器失败...");
        exc.printStackTrace();
        try {
            clientChannel.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
