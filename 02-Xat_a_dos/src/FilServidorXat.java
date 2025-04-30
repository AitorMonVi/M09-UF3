/* */

import java.io.IOException;
import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {
    // propiedades
    private ObjectInputStream stream;

    // constructor
    public FilServidorXat(String nom, ObjectInputStream stream) {
        super(nom);
        this.stream = stream;

        System.out.println("Fil de xat creat.");
    }

    public void run() {
        System.out.println("Fil de " + getName() + " iniciat.");

        String message = "";
        try {
            while (!message.equals(ServidorXat.MSG_SORTIR)) {
                message = (String) stream.readObject();

                System.out.println("Rebut: " + message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Fil de xat finalitzat.");
    }
}