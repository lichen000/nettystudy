package mangolost.nettystudy.bio.bioclient;

import java.io.*;
import java.net.Socket;

public class BioClient {

    private final String host;
    private final int port;

    public BioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        String msg = "Hello World" + System.getProperty("line.separator");
        Socket socket = null;
        BufferedReader in = null;
        OutputStream out = null;
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = socket.getOutputStream();
            out.write(msg.getBytes("UTF-8")); //发送消息
            out.flush();

            //进入轮询，接受服务端消息
            String res = in.readLine(); //如果没有消息，就阻塞
            System.out.println("从服务器收到的结果为：" + res);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //必要的清理工作
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    in = null;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    out = null;
                }
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    socket = null;
                }
            }
        }
    }
}