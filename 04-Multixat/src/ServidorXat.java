/* */

import java.io.IOException;
import java.io.ObjectInputStream;
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

    public ServerSocket getServer() { return this.server; }

    public void servidorAEscoltar() {
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pararServidor() {
        try {
            if (server!=null && !server.isClosed()) server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalitzarXat() {
        enviarMissatgeGrup(MSG_SORTIR);
        table.clear();

        this.sortir = true;
    }

    public void afegirClient(GestorClient gestor) {
        table.put(gestor.getNom(), gestor);
        enviarMissatgeGrup("Entra: " + gestor.getNom());
    }

    public void eliminarClient(String name) {
        if (table.contains(name)) table.remove(name);
    }

    public void enviarMissatgeGrup(String message) {}

    public void enviarMissatgePersonal(String recipient, String sender, String message) {}

    public static void main(String[] args) {
        ServidorXat server = new ServidorXat();
        server.servidorAEscoltar();

        while (!server.sortir) {
            try {
                Socket client = server.getServer().accept();
    
                GestorClient gestor = new GestorClient(client, server);
                gestor.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        server.pararServidor();
    }
}