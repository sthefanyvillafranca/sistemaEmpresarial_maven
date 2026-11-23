package repositorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Tests de Fichero. No usan base de datos ni tocan mis personas.txt de verdad.
class FicheroTest {

    // @TempDir: JUnit crea una carpeta temporal vacía para cada test
    // y la borra al terminar. Ahí escribimos los ficheros de prueba.
    @TempDir
    Path carpeta;

    private Fichero fichero;

    // @BeforeEach: antes de CADA test, un Fichero que apunta a
    // "prueba.txt" dentro de la carpeta temporal.
    @BeforeEach
    void crearFichero() {
        fichero = new Fichero(carpeta.resolve("prueba.txt").toString());
    }

    @Test
    @DisplayName("Lo que se escribe es lo que se lee, línea a línea")
    void escribirYLeerDevuelveLasMismasLineas() throws Exception {
        // Arrange
        ArrayList<String> lista = new ArrayList<>();
        lista.add("1|Ada|Lovelace|Madrid|Desarrolladora");
        lista.add("2|Grace|Hopper|Sevilla|Analista");

        // Act
        fichero.escribirFicheroExportar(lista);
        ArrayList<String> leido = fichero.leerFichero();

        // Assert
        assertEquals(2, leido.size());
        assertEquals("1|Ada|Lovelace|Madrid|Desarrolladora", leido.get(0));
        assertEquals("2|Grace|Hopper|Sevilla|Analista", leido.get(1));
    }

    @Test
    @DisplayName("Escribir otra vez sobrescribe: no añade al final")
    void escribirDosVecesSobrescribe() throws Exception {
        // Arrange
        ArrayList<String> primera = new ArrayList<>();
        primera.add("linea vieja 1");
        primera.add("linea vieja 2");
        ArrayList<String> segunda = new ArrayList<>();
        segunda.add("linea nueva");

        // Act
        fichero.escribirFicheroExportar(primera);
        fichero.escribirFicheroExportar(segunda);

        // Assert: solo queda lo último (FileWriter con append = false)
        ArrayList<String> leido = fichero.leerFichero();
        assertEquals(1, leido.size());
        assertEquals("linea nueva", leido.get(0));
    }

    @Test
    @DisplayName("Leer un fichero que no existe lanza FileNotFoundException")
    void leerFicheroInexistenteLanzaExcepcion() {
        // assertThrows(tipo de error, código): pasa si ese código lanza ese error.
        // El "() -> ..." es la forma de darle a JUnit un trozo de código
        // para que lo ejecute él y vigile si lanza el error.
        assertThrows(FileNotFoundException.class, () -> fichero.leerFichero());
    }
}
