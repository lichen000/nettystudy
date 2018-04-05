package mangolost.nettystudy.client;

public class NettyClientApplication {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 9977;
        new EchoClient(host, port).start();
    }
}
