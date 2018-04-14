package mangolost.nettystudy.bio.bioserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于Java IO 的阻塞server
 */
public class BioServer {

    private final int port;

    public BioServer(int port) {
        this.port = port;
    }

    /**
     *
     */
    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port); //把服务器绑定到端口
            System.out.println("Server is running on 127.0.0.1 : " + port);
            while (true) { //无限循环
                final Socket clientSocket = serverSocket.accept(); //接受来自客户端的socket,如果没有客户端连接，将阻塞
                System.out.println( "Accepted connection from " + clientSocket);
                new Thread(new BioServerHandler(clientSocket)) //创建新线程处理
                        .start(); //启动线程
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    serverSocket = null;
                }
            }
        }
    }
}