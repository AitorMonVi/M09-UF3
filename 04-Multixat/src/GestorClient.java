/* */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestorClient extends Thread {

    private String name;
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServidorXat server;
    private boolean sortir;

    public GestorClient(String name, Socket socket, ServidorXat server) {
        this.name = name;
        this.client = socket;
        this.server = server;
        
        try {
            this.output = new ObjectOutputStream(client.getOutputStream());
            this.input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sortir = false;
    }

    public String getNom() { return name; }

    public void run() {}

    public void enviarMissatge(String recipient, String message) {}

    public void processaMissatge(String message) {}
}