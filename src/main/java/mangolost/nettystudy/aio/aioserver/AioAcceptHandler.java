package mangolost.nettystudy.aio.aioserver;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 *
 */
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

    /**
     *
     * @param channel
     * @param aioServer
     */
    @Override
    public void completed(AsynchronousSocketChannel channel, AioServer aioServer) {
        //继续接受其他客户端的请求
        AioServer.clientCount++;
        System.out.println("连接的客户端数：" + AioServer.clientCount);
        aioServer.channel.accept(aioServer, this);
        //创建新的Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //异步读  第三个参数为接收消息回调的业务Handler
        channel.read(buffer, buffer, new AioReadHandler(channel));
    }

    /**
     *
     * @param exc
     * @param aioServer
     */
    @Override
    public void failed(Throwable exc, AioServer aioServer) {
        exc.printStackTrace();
        aioServer.latch.countDown();
    }
}
