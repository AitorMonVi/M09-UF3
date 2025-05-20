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

    public ServerSocket getServer() { return this.server; }

    public void servidorAEscoltar() {
        try {
            server = new ServerSocket(PORT);
            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pararServidor() {
        try {
            if (server!=null && !server.isClosed()) server.close();
            System.out.println("Servidor aturat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalitzarXat() {
        enviarMissatgeGrup(MSG_SORTIR);
        System.out.println("DEBUG: multicast sortir");
        table.clear();
        this.sortir = true;
        pararServidor();
        System.out.println("Xat finalitzat.");
    }

    public void afegirClient(GestorClient gestor) {
        table.put(gestor.getNom(), gestor);
        System.out.println(gestor.getNom() + " connectat.");
        System.out.println("DEBUG: multicast Entra: " + gestor.getNom());
    }

    public void eliminarClient(String name) {
        if (table.containsKey(name)) {
            table.remove(name);
            System.out.println("Client eliminat: " + name);
        }
    }

    public void enviarMissatgeGrup(String message) {
        for (GestorClient gestor : table.values()) {
            gestor.enviarMissatge("Grup", message);
            System.out.println("Missatge grupal: " + message);
        }
    }

    public void enviarMissatgePersonal(String recipient, String sender, String message) {
        GestorClient gestor = table.get(recipient);
        if (gestor != null) {
            gestor.enviarMissatge(sender, message);
            System.out.println("Missatge personal per (" + recipient + ") de (" + sender + "): " + message);
        } else System.out.println("Destinatari no trobat: " + recipient);
    }

    public static void main(String[] args) {
        ServidorXat server = new ServidorXat();
        server.servidorAEscoltar();

        while (!server.sortir) {
            try {
                Socket client = server.getServer().accept();
                System.out.println("Client connectat: " + ServidorXat.HOST + ":" + ServidorXat.PORT);
                GestorClient gestor = new GestorClient(client, server);
                gestor.start();
            } catch (IOException e) {
                if (!server.sortir) e.printStackTrace();
            }
        }

        server.pararServidor();
    }
}