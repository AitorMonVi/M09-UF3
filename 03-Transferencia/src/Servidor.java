/* */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    // constantes
    public static final int PORT = 9999;
    public static final String HOST = "localhost";

    public static boolean read = true;

    // propiedades
    private ServerSocket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public Servidor() {}

    public Socket conectar() {
        System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);

        try {
            server = new ServerSocket(PORT);

            System.out.println("Esperant connexio...");
            Socket socket = server.accept();

            System.out.println("Connexio acceptada: " + socket.getInetAddress());
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void enviarFitxers(Socket socket) {
        try {
            System.out.println("Esperant el nom del fitxer del client...");
            String nameFile = (String) input.readObject();
            if (nameFile == null) {
                System.out.println("Error llegint el fitxer del client: " + nameFile);
                System.out.println("Nom del fitxer buit o null. Sortint...");

                read = false;
                return;
            }

            System.out.println("Nom fitxer rebut: " + nameFile);
            Fitxer fitxer = new Fitxer(nameFile);
            byte[] contingut = fitxer.getContingut();

            output.writeObject(contingut);
            output.flush();
            System.out.println("Fitxer enviat al client: " + nameFile);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void tancarConexio(Socket socket) {
        try {
            if (server!=null && !server.isClosed()) server.close();

            System.out.println("Tancant connexio amb el client: " + socket.getInetAddress());
            if (socket!=null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();

        Socket client = servidor.conectar();
        try {
            servidor.output = new ObjectOutputStream(client.getOutputStream());
            servidor.input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (read) {
            servidor.enviarFitxers(client);
        }
        servidor.tancarConexio(client);
    }
}