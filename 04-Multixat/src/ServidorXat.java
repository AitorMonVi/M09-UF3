/* */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {

    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";

    private ServerSocket server;
    private Hashtable<String, GestorClient> table;
    private boolean sortir;

    public ServidorXat() {
        this.table = new Hashtable<>();
        this.sortir = false;
    }

    public void servidorAEscoltar() {
        try {
            server = new ServerSocket(PORT);

            while (!sortir) {
                Socket client = server.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pararServidor() {}

    public void finalitzarXat() {}

    public void afegirClient(GestorClient client) {}

    public void eliminarClient(String name) {}

    public void enviarMissatgeGrup(String message) {}

    public void enviarMissatgePersonal(String recipient, String sender, String message) {}

    public static void main(String[] args) {}
}