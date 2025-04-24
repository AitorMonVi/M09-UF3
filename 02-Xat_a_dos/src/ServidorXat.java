/* */

import java.net.ServerSocket;

public class ServidorXat {
    // constantes
    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";

    // propiedades
    private ServerSocket srvSocket;

    // constructor
    public ServidorXat() {
        this.srvSocket = null;
    }

    public void iniciarServidor() {}

    public void pararServidor() {}

    public String getNom() {
        return null;
    }

    // main
    public static void main(String[] args) {
        
    }
}