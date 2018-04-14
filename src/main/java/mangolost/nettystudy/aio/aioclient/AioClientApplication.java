package mangolost.nettystudy.aio.aioclient;

/**
 *
 */
public class AioClientApplication {

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 9977;
        new AioClient(host, port).start();
    }
}
