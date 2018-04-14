package mangolost.nettystudy.aio.aioserver;

/**
 *
 */
public class AioServerApplication {

    public static void main(String[] args) {
        int port = 9977;
        new AioServer(port).start();
    }

}
