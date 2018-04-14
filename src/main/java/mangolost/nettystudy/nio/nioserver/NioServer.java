package mangolost.nettystudy.nio.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 基于Java IO 的阻塞server
 */
public class NioServer {

    private final int port;

    public NioServer(int port) {
        this.port = port;
    }

    /**
     *
     * @throws IOException
     */
    public void start() throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address); //将服务器绑定到选定的端口

        System.out.println("Server is running on port:" + port);

        // 创建一个选择器，可用close()关闭，isOpen()表示是否处于打开状态，他不隶属于当前线程
        Selector selector = Selector.open();
        // 在选择器里面注册关注这个服务器套接字通道的accept事件
        // ServerSocketChannel只有OP_ACCEPT可用，OP_CONNECT,OP_READ,OP_WRITE用于SocketChannel
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            try {
                long timeout = 99999 * 1000;
                selector.select(timeout); //等待需要处理的新事件；阻塞将一直持续到下一个传入事件 ？？？
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys(); //获取所有接收事件的Selection-Key 实例
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove(); // 由于select操作只管对selectedKeys进行添加，所以key处理后我们需要从里面把key去掉
                try {

                    if (key.isAcceptable()) { //如果key对应的Channel包含客户端的链接请求
                        ServerSocketChannel server = (ServerSocketChannel) key.channel(); // 得到与客户端的套接字通道
                        SocketChannel client = server.accept();
                        client.configureBlocking(false); //接受客户端，并将它注册到选择器
                        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("Accepted connection from " + client);
                        key.interestOps(SelectionKey.OP_ACCEPT);
                    }
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();

                        try {
                            new NioServerHandler().handle(channel);
                            // 写完就把状态关注去掉，否则会一直触发写事件(改变自身关注事件)
//                            key.interestOps(SelectionKey.OP_READ); //???
                        } catch (IOException e) {
                            //如果捕获到该SelectionKey对应的Channel时出现了异常,即表明该Channel对于的Client出现了问题
                            //所以从Selector中取消该SelectionKey的注册
                            e.printStackTrace();
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        }
    }
}
