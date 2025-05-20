/* */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat extends Thread {

    public static Scanner scanner;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean sortir;

    public ClientXat() {}

    public void setSortir(boolean sortir) { this.sortir = sortir; }

    public void connecta() {
        try {
            socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            sortir = false;

            System.out.println("Client connectat a " + ServidorXat.HOST + ":" + ServidorXat.PORT);
            System.out.println("Flux d'entrada i sortida creat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMissatge(String message) {
        try {
            System.out.println("Enviant missatge: " + message);
            output.writeObject(message);
            output.flush(); 
        } catch (IOException e) {
            System.err.println("Error enviant missatge. Sortint...");
            sortir = true;
        }
    }

    public void tancarClient() {
        try {
            output.close();
            input.close();
            socket.close();
            System.out.println("Flux d'entrada tancat.");
            System.out.println("Flux de sortida tancat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("DEBUG: Iniciant rebuda de missatges...");
            while (!sortir) {
                String rawMessage = (String) input.readObject();
                if (rawMessage != null && !rawMessage.trim().isEmpty()) {
                    String code = Missatge.getCodiMissatge(rawMessage);
                    String[] codeParts = Missatge.getPartsMissatge(rawMessage);
                    switch (code) {
                        case Missatge.CODI_MSG_GRUP : {
                            String message = codeParts[1];
                            System.out.println("Missatge de grup: " + message);
                            break;
                        }
                        case Missatge.CODI_MSG_PERSONAL : {
                            String recipient = codeParts[1];
                            String message = codeParts[2];
                            System.out.println("Missatge personal de " + recipient + ": " + message);
                            break;
                        }
                        case Missatge.CODI_SORTIR_CLIENT : {
                            System.out.println("T'han desconnectat del servidor.");
                            sortir = true;
                            break;
                        }
                        case Missatge.CODI_SORTIR_TOTS : {
                            System.out.println("Servidor tancat per a tots.");
                            sortir = true;
                            break;
                        }
                        default : {
                            System.out.println("Missatge desconegut: " + rawMessage);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (!sortir) {
                System.err.println("Error rebent missatges: " + e.getMessage());
                e.printStackTrace();
                this.sortir = true;
            }  
        }
    }

    public void ajuda() {
        System.out.printf("""
            ---------------------
            Comandes disponibles:
            1.- Conectar al servidor (primer pas obligatori)
            2.- Enviar missatge personal
            3.- Enviar missatge al grup
            4.- (o línia en blanc) -> Sortir del client
            5.- Finalitzar tothom
            ---------------------
                """);
    }

    public String getLinea(Scanner scanner, String message, boolean obligatori) {
        if (message==null || message.isBlank() || message.isEmpty()) return "";
        System.out.printf("%s ", message);
        
        String line = scanner.nextLine().trim();
        while (obligatori && line.isEmpty()) {
            System.out.print(message + " ");
            line = scanner.nextLine().trim();
        }
        return line;
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        scanner = new Scanner(System.in);

        try {
            client.connecta();
            client.start();
            client.ajuda();

            boolean sortir = false;
            while (!sortir) {
                System.out.print("> ");
                String option = scanner.nextLine();

                switch (option) {
                    case "":
                        sortir = true;
                        break;
                    case "1": {
                        String name = client.getLinea(scanner, "Introdueix el nom:", true);
                        String message = Missatge.getMissatgeConectar(name);
                        client.enviarMissatge(message);
                        break;
                    }
                    case "2": {
                        String recipient = client.getLinea(scanner, "Destinatari:", true);
                        String message = client.getLinea(scanner, "Missatge a enviar:", true);
                        client.enviarMissatge(Missatge.getMissatgePersonal(recipient, message));
                        break;
                    }
                    case "3": {
                        String message = client.getLinea(scanner, "Missatge al grup:", true);
                        client.enviarMissatge(Missatge.getMissatgeGrup(message));
                        break;
                    }
                    case "4": {
                        client.enviarMissatge(Missatge.getMissatgeSortirClient("Adéu"));
                        sortir = true;
                        client.setSortir(sortir);
                        break;
                    }
                    case "5": {
                        client.enviarMissatge(Missatge.getMissatgeSortirTots("Adéu"));
                        sortir = true;
                        client.setSortir(sortir);
                        break;
                    }
                    default:
                        System.out.println("Opcio no valida: " + option);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al connectar: " + e.getMessage());
        }

        client.tancarClient();
        scanner.close();
    }
}