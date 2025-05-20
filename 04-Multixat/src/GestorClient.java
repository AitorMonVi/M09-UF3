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

    public GestorClient(Socket socket, ServidorXat server) {
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

    public void run() {
        while (!sortir) {
            try {
                String message = (String) input.readObject();
                processaMissatge(message);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            if (client!=null && !client.isClosed()) client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMissatge(String recipient, String message) {}

    public void processaMissatge(String rawMessage) {
        String code = Missatge.getCodiMissatge(rawMessage);
        String[] codeParts = Missatge.getPartsMissatge(code);
        switch (code) {
            case Missatge.CODI_CONECTAR : {
                this.name = codeParts[1];
                server.afegirClient(this);
                break;
            }
            case Missatge.CODI_MSG_PERSONAL : {
                String recipient = codeParts[1];
                String message = codeParts[2];

                enviarMissatge(recipient, message);
                break;
            }
            case Missatge.CODI_MSG_GRUP : {
                break;
            }
            case Missatge.CODI_SORTIR_CLIENT : {
                server.eliminarClient(getNom());
                break;
            }
            case Missatge.CODI_SORTIR_TOTS : {
                this.sortir = true;
                server.finalitzarXat();
                break;
            }
            default : {}
        }
    }
}