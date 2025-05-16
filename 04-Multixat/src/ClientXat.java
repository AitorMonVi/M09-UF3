/* */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat extends Thread {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean sortir;

    public ClientXat() {}

    public void connecta() {}

    public void enviarMissatge() {}

    public void tancarClient() {}

    public void run() {}

    public void ajuda() {}

    public String getLinea(Scanner scanner, Missatge message, boolean obligatori) {
        return "";
    }

    public static void main(String[] args) {}
}