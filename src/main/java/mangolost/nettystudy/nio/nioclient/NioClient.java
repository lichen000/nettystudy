package mangolost.nettystudy.nio.nioclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 *
 */
public class NioClient {

    private final String host;
    private final int port;

    public NioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     *
     */
    public void start() throws IOException {

        boolean stop = false;

        //创建选择器
        Selector selector = Selector.open();
        //打开监听通道
        SocketChannel socketChannel = SocketChannel.open();
        //如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式
        socketChannel.configureBlocking(false);//开启非阻塞模式
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            sendMsg(socketChannel, "HelloWorld");
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

        //循环遍历selector
        while (!stop) {
            try {
                //无论是否有读写事件发生，selector每隔1s被唤醒一次
                selector.select(1000);
                //阻塞,只有当至少一个注册的事件发生的时候才会继续.
//              selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    SocketChannel sc = (SocketChannel) key.channel();

                    // OP_CONNECT 两种情况，链接成功或失败这个方法都会返回true
                    if (key.isConnectable()){
                        // 由于非阻塞模式，connect只管发起连接请求，finishConnect()方法会阻塞到链接结束并返回是否成功
                        // 另外还有一个isConnectionPending()返回的是是否处于正在连接状态(还在三次握手中)
                        if (socketChannel.finishConnect()) {

                            sc.register(selector,SelectionKey.OP_READ);
                            sendMsg(socketChannel, "HelloWorld2");
                        } else {
                            //链接失败，进程推出
                            System.exit(1);
                        }
                    }
                    if (key.isReadable()) {
                        //读取服务端的响应
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int readBytes = sc.read(buffer);
                        String response = "";
                        if (readBytes > 0){
                            buffer.flip();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);
                            response += new String(bytes, Charset.forName("UTF-8"));
                            stop = true;
                        } else if (readBytes < 0) {
                            //对端链路关闭
                            key.channel();
                            sc.close();
                        }
                        System.out.println("客户端收到消息：" + response);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        selector.close();
    }

    /**
     *
     * @param sc
     * @param msg
     * @throws IOException
     */
    public void sendMsg(SocketChannel sc, String msg) throws IOException{
        byte[] req = msg.getBytes(Charset.forName("UTF-8"));
        ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
        byteBuffer.put(req);
        byteBuffer.flip();
        sc.write(byteBuffer);
        if(!byteBuffer.hasRemaining()){
            System.out.println("Send 2 nioclient successed");
        }
    }
}