/* */

import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {
    // propiedades
    private ObjectInputStream stream;

    // constructor
    public FilServidorXat(String nom, ObjectInputStream stream) {
        super(nom);
        this.stream = stream;    
    }

    public void run() {}
}