package mangolost.nettystudy.aio.aioclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public AioReadHandler(AsynchronousSocketChannel clientChannel, CountDownLatch latch) {
        this.clientChannel = clientChannel;
        this.latch = latch;
    }

    /**
     *
     * @param result
     * @param buffer
     */
    @Override
    public void completed(Integer result,ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        try {
            String res = new String(bytes,"UTF-8");
            System.out.println("客户端收到结果: " + res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc,ByteBuffer attachment) {
        System.err.println("数据读取失败...");
        try {
            clientChannel.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
