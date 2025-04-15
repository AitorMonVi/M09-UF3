/* */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    // constantes
    public static final int PORT = 7777;
    public static final String HOST = "localhost";

    // propiedades
    private ServerSocket srvSocket;
    private Socket clientSocket;

    // constructor
    public Servidor() {
        srvSocket = null;
        clientSocket = null;
    }

    public void connecta() {
        try {
            srvSocket = new ServerSocket(PORT);
            System.out.println("Servidor en marxa a " + HOST + ":" + PORT);
            
            System.out.println("Esperant connexions a " + HOST + ":" + PORT);
            clientSocket = srvSocket.accept();

            System.out.println("Client connectat: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void repDades() {
        if (clientSocket == null) return;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println("Rebut: " + linea);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tanca() {
        try {
            if (srvSocket!=null && !srvSocket.isClosed()) {
                srvSocket.close();
            }

            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }

            System.out.println("Servidor tancat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();

        servidor.connecta();
        servidor.repDades();
        servidor.tanca();   
    }
}