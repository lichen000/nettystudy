package mangolost.nettystudy.bio.bioserver;

/**
 *
 */
public class BioServerApplication {

    public static void main(String[] args) {
        int port = 9977;
        new BioServer(port).start();
    }
}
