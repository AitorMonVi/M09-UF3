/* */

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Client {
    // constantes
    public static String DIR_ARRIBADA = "./03-Transferencia/src/tmp";
    public static Scanner scanner = new Scanner(System.in);

    public static boolean read = true;
    
    // propiedades
    public Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public Client() {}

    public void conectar() {
        System.out.println("Connectant a -> " + Servidor.HOST + ":" + Servidor.PORT);
        try {
            socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);
            System.out.println("Connexio acceptada: " + socket.getInetAddress());

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rebreFitxers() {
        System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
        String nameFile = scanner.nextLine();

        try {
            if (nameFile.equals("sortir")) {
                System.out.println("Sortint...");
                output.writeObject(null);
                output.flush();

                read = false;
                return;
            }
            output.writeObject(nameFile);
            output.flush();

            byte[] contingut = (byte[]) input.readObject();
            if (contingut == null) {
                System.out.println("Contingut es null.");
                return;
            }

            System.out.print("Nom del fitxer a guardar: ");
            String name = scanner.nextLine();
            Path path = new File(DIR_ARRIBADA, name).toPath();
            Files.write(path, contingut);

            System.out.println("Fitxer rebut y guardat com: " + path);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void tancarConexio() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Connexio tancada.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        client.conectar();
        while (read) {
            client.rebreFitxers();
        }
        client.tancarConexio();
    }
}