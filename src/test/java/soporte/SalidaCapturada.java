package soporte;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

// NO es un test: es una ayuda para los tests de los controladores.
//
// Un controlador no devuelve nada: su resultado es lo que imprime por consola.
// Esta clase cambia System.out por una "caja" donde se guarda lo impreso,
// para que el test pueda leerlo después.
public class SalidaCapturada {

    private static PrintStream salidaOriginal;
    private static ByteArrayOutputStream caja;

    // Desde aquí, todo lo que se imprima va a la caja y no a la pantalla.
    public static void empezar() {
        salidaOriginal = System.out;
        caja = new ByteArrayOutputStream();
        System.setOut(new PrintStream(caja));
    }

    // Todo lo impreso desde empezar(), como un solo texto.
    public static String texto() {
        return caja.toString();
    }

    // Devuelve System.out a la pantalla.
    public static void terminar() {
        System.setOut(salidaOriginal);
    }
}
