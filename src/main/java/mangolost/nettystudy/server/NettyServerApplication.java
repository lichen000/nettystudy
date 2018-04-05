package mangolost.nettystudy.server;

public class NettyServerApplication {

    public static void main(String[] args) throws Exception {
        int port = 9977;
        new EchoServer(port).start();
    }
}
