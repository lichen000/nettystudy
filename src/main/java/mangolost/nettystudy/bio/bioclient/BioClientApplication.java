package mangolost.nettystudy.bio.bioclient;

public class BioClientApplication {

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9977;
        new BioClient(host, port).start();
    }
}
