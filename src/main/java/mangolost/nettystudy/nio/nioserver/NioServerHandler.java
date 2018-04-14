package mangolost.nettystudy.nio.nioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NioServerHandler {

    /**
     *
     * @param socketChannel
     * @throws IOException
     */
    public void handle(SocketChannel socketChannel) throws IOException {

        String msg = "";
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int readBytes = socketChannel.read(byteBuffer);
        if (readBytes > 0) {
            byteBuffer.flip(); //为write()准备
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            msg += new String(bytes, Charset.forName("UTF-8"));
            System.out.println("服务器已接受:" +  msg);

            byte[] req = ("你好: " + msg).getBytes("UTF-8");
            ByteBuffer byteBuffer2 = ByteBuffer.allocate(req.length);
            byteBuffer2.put(req);
            byteBuffer2.flip();
            socketChannel.write(byteBuffer2);
            if (!byteBuffer.hasRemaining()) {
//                System.out.println("Send 2 Service succeed");
            }
        }
    }
}
