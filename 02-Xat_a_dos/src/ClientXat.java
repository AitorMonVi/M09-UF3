/* */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    // constantes
    public static Scanner scanner = new Scanner(System.in);

    // propiedades
    public Socket socket;
    public ObjectOutputStream sortida;
    public ObjectInputStream entrada;

    // constructor
    public ClientXat() {
        this.socket = null;
    }

    public void connecta() {
        try {
            socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);

            System.out.println("Client connectat a " + ServidorXat.HOST + ":" + ServidorXat.PORT);

            sortida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            System.out.println("Flux d'entrada y sortida creat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMissatge(String missatge) {
        System.out.println("Enviant missatge: " + missatge);

        try {
            sortida.writeObject(missatge);
            sortida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tancarClient() {
        System.out.println("Tancant client...");

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();

                System.out.println("Client tancat.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // main
    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        client.connecta();

        FilLectorCX thread = new FilLectorCX(client.entrada);
        thread.start();

        String message = "";
        while (!message.equals(ServidorXat.MSG_SORTIR)) {
            System.out.print("Missatge ('sortir' per tancar): ");
            message = scanner.nextLine();

            client.enviarMissatge(message);
        }

        try {
            thread.join();
        } catch (InterruptedException e) {}

        client.tancarClient();
    }
}