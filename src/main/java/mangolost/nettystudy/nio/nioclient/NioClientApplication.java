package mangolost.nettystudy.nio.nioclient;

import java.io.IOException;

/**
 *
 */
public class NioClientApplication {

    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 9977;
        new NioClient(host, port).start();
    }
}
