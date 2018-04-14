package mangolost.nettystudy.nio.nioserver;

import java.io.IOException;

public class NioServerApplication {

    public static void main(String[] args) throws IOException {

        int port = 9977;
        new NioServer(port).start();
    }
}
