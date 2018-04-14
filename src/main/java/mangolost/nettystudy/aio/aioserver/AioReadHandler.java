package mangolost.nettystudy.aio.aioserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 *
 */
public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    public AioReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    /**
     * 读取到消息后的处理
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        //flip操作
        attachment.flip();
        //根据
        byte[] message = new byte[attachment.remaining()];
        attachment.get(message);
        try {
            String msg = new String(message, "UTF-8");
            System.out.println("服务器收到消息: " + msg);

            //handle msg
            String response = (String) new AioMsgHandler().doHandle(msg);

            //向客户端发送消息
            doWrite(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     * @param result
     */
    private void doWrite(String result) {
        byte[] bytes = new byte[0];
        try {
            bytes = result.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        //异步写数据 参数与前面的read一样
        channel.write(writeBuffer, writeBuffer, new AioWriteHandler(channel));
    }

    /**
     *
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
