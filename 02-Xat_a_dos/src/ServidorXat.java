/* */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorXat {
    // constantes
    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";

    public static Scanner scanner = new Scanner(System.in);

    // propiedades
    public ServerSocket srvSocket;

    // constructor
    public ServidorXat() {
        this.srvSocket = null;
    }

    public void iniciarServidor() {
        try {
            srvSocket = new ServerSocket(PORT);

            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pararServidor() {
        try {
            if (srvSocket!=null && !srvSocket.isClosed()) {
                srvSocket.close();
                
                System.out.println("Servidor aturat.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNom(ObjectInputStream input, ObjectOutputStream output) {
        try {
            output.writeObject("Escriu el teu nom:");
            output.flush();

            String name = (String) input.readObject();
            return name;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // main
    public static void main(String[] args) {
        ServidorXat server = new ServidorXat();
        server.iniciarServidor();

        try {
            Socket client = server.srvSocket.accept();

            System.out.println("Client connectat: " + client.getInetAddress());
            
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            
            String name = server.getNom(input, output);
            System.out.println("Nom rebut: " + name);

            FilServidorXat thread = new FilServidorXat(name, input);
            thread.start();

            String message = "";
            while (!message.equals(MSG_SORTIR)) {
                System.out.print("Missatge ('sortir' per tancar): ");
                message = scanner.nextLine();

                output.writeObject(message);
                output.flush();
            }

            try {
                thread.join();
            } catch (InterruptedException e) {}

            scanner.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.pararServidor();
    }
}