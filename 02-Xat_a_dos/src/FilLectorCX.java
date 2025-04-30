/* */

import java.io.IOException;
import java.io.ObjectInputStream;

public class FilLectorCX extends Thread {
    // propiedades
    private ObjectInputStream stream;

    // constructor
    public FilLectorCX(ObjectInputStream stream) {
        this.stream = stream;
    }

    public void run() {
        System.out.println("Fil de lectura iniciat.");

        String message = "";
        try {
            while (!message.equals(ServidorXat.MSG_SORTIR)) {
                message = (String) stream.readObject();

                System.out.println("Rebut: " + message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("El servidor ha tancat la connexió.");
    }
}