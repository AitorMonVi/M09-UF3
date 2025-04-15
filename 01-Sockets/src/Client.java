/* */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    // constantes
    public static final int PORT = 7777;
    public static final String HOST = "localhost";
    
    public static Scanner scanner = new Scanner(System.in);

    // propiedades
    private Socket socket;
    private PrintWriter out;

    // constructor
    public Client() {
        socket = null;
    }

    public void connecta() {
        try {
            InetAddress addr = InetAddress.getByName(HOST);
            socket = new Socket(addr, PORT);

            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connectat a servidor en " + HOST + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void envia(String message) {
        if (out == null) return;

        out.println(message);
        System.out.println("Enviant al servidor: " + message);
    }

    public void tanca() {
        try {
            if (out != null) {
                out.close();
            }

            if (socket!=null && !socket.isClosed()) {
                socket.close();
            }

            System.out.println("Client tancat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        client.connecta();
        client.envia("Prova d'enviament 1");
        client.envia("Prova d'enviament 2");
        client.envia("Adeu!");

        System.out.println("Prem Enter per tancar el client...");
        scanner.nextLine();

        client.tanca();
        scanner.close();  
    }
}