/* */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fitxer {
    // propiedades
    private String name;
    private byte[] contingut;
    
    public Fitxer(String name) {
        this.name = name;
    }

    public byte[] getContingut() {
        File file = new File(name);

        if (file.exists() && file.isFile()) {
            try {
                contingut = Files.readAllBytes(file.toPath());
                System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
                return contingut;
            } catch (IOException e) {
                return null;
            }
        } else return null;
    }
}