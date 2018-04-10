package mangolost.nettystudy.bioserver;

import java.io.*;
import java.net.Socket;

public class BioServerHandler implements Runnable {

    private Socket socket;

    public BioServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        OutputStream out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = socket.getOutputStream();
            String msg = in.readLine(); //如果没有消息，就阻塞
            System.out.println("服务器收到消息：" + msg);
            // 把msg反转后输出
            String result = new StringBuilder(msg).reverse().toString();
            out.write((result + System.getProperty("line.separator")).getBytes("UTF-8")); //给客户端返回消息
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
            if (socket != null) {
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
