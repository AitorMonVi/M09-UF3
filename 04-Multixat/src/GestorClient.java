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

    @Override
    public void run() {
        try {
            while (!sortir) {
                String message = (String) input.readObject();
                processaMissatge(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                server.eliminarClient(name);
                if (client!=null && !client.isClosed()) client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviarMissatge(String recipient, String message) {
        try {
            if (recipient.equals("Grup")) {
                output.writeObject(Missatge.getMissatgeGrup(message));
                output.flush();
            } else {
                output.writeObject(Missatge.getMissatgePersonal(recipient, message));
                output.flush();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processaMissatge(String rawMessage) {
        String code = Missatge.getCodiMissatge(rawMessage);
        String[] codeParts = Missatge.getPartsMissatge(rawMessage);
        switch (code) {
            case Missatge.CODI_CONECTAR : {
                this.name = codeParts[1];
                server.afegirClient(this);
                break;
            }
            case Missatge.CODI_MSG_PERSONAL : {
                String recipient = codeParts[1];
                String message = codeParts[2];
                server.enviarMissatgePersonal(recipient, name, message);
                break;
            }
            case Missatge.CODI_MSG_GRUP : {
                String message = codeParts[1];
                server.enviarMissatgeGrup(message);
                break;
            }
            case Missatge.CODI_SORTIR_CLIENT : {
                this.sortir = true;
                server.eliminarClient(getNom());
                break;
            }
            case Missatge.CODI_SORTIR_TOTS : {
                this.sortir = true;
                server.finalitzarXat();
                break;
            }
            default : {
                System.err.println("Codi desconegut: " + code);
            }
        }
    }
}