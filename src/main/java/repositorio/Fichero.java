package repositorio;

import java.io.*;
import java.util.ArrayList;

public class Fichero {
    private String ruta;

    public Fichero(String ruta) {
        this.ruta = ruta;

    }

    public void escribirFicheroExportar(ArrayList<String> lista) throws FileNotFoundException, IOException {

        FileWriter fichero = null;

        try {
            fichero = new FileWriter(ruta, false);
            // pensar en si quiero que se agreguen o se sobreescriba (el append con true)

            for (String linea : lista) { // la lista que pasas por parametro
                fichero.write(linea + "\n"); //escribe linea por linea con saltos de linea
            }

        } finally {
            if (fichero != null) {
                fichero.close();

            }
        }
    }


    public ArrayList<String> leerFichero() throws IOException, FileNotFoundException {
        FileReader fichero = null;
        BufferedReader lector = null;

        ArrayList<String> lista = new ArrayList<>();

        String linea;

        try {
            fichero = new FileReader(ruta);
            lector = new BufferedReader(fichero);

            while ((linea = lector.readLine()) != null) {
                lista.add(linea);
            }
        } finally {
            if (lector != null) {
                lector.close();
            }
        }

        return lista;
    }


//llave final
}