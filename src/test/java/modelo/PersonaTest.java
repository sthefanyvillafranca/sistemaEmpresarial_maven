package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Tests de Persona. No usan base de datos: solo comprueban que el objeto
// guarda bien sus datos y que escribe bien el SQL y la línea de fichero.
class PersonaTest {

    private Persona ada;

    // @BeforeEach: se ejecuta antes de CADA test. Crea una Ada nueva,
    // así ningún test depende de los cambios que hizo otro.
    @BeforeEach
    void crearPersona() {
        ada = new Persona(1, "Ada", "Lovelace", "Calle Algoritmo 1, Madrid", "Desarrolladora Senior");
    }

    // @Test: marca el método como una prueba que JUnit ejecuta.
    // @DisplayName: el texto que se ve en el panel de IntelliJ.
    @Test
    @DisplayName("El constructor con id guarda todos los datos")
    void elConstructorConIdGuardaLosDatos() {
        // assertEquals(lo que espero, lo que ha salido)
        assertEquals(1, ada.getId());
        assertEquals("Ada", ada.getNombre());
        assertEquals("Lovelace", ada.getApellidos());
        assertEquals("Calle Algoritmo 1, Madrid", ada.getDireccion());
        assertEquals("Desarrolladora Senior", ada.getCargo());
    }

    @Test
    @DisplayName("El constructor sin id deja el id en 0 (lo pondrá MySQL)")
    void elConstructorSinIdDejaElIdEnCero() {
        // Arrange + Act
        Persona nueva = new Persona("Grace", "Hopper", "Calle Compilador 2", "Analista");

        // Assert: un int que nadie rellena vale 0
        assertEquals(0, nueva.getId());
        assertEquals("Grace", nueva.getNombre());
    }

    @Test
    @DisplayName("El constructor desde una línea de fichero separa los datos por |")
    void elConstructorDesdeCadenaSeparaLosDatos() {
        // Act
        Persona leida = new Persona("5|Grace|Hopper|Calle Compilador 2|Analista");

        // Assert
        assertEquals(5, leida.getId());
        assertEquals("Grace", leida.getNombre());
        assertEquals("Hopper", leida.getApellidos());
        assertEquals("Calle Compilador 2", leida.getDireccion());
        assertEquals("Analista", leida.getCargo());
    }

    @Test
    @DisplayName("Los setters cambian su dato y no tocan el id")
    void losSettersCambianSuDato() {
        // Act
        ada.setNombre("Augusta");
        ada.setApellidos("King");
        ada.setDireccion("Londres");
        ada.setCargo("Jefa de equipo");

        // Assert
        assertEquals("Augusta", ada.getNombre());
        assertEquals("King", ada.getApellidos());
        assertEquals("Londres", ada.getDireccion());
        assertEquals("Jefa de equipo", ada.getCargo());
        assertEquals(1, ada.getId());
    }

    @Test
    @DisplayName("toInsertSql escribe el INSERT con los datos de la persona")
    void toInsertSqlEscribeElInsert() {
        assertEquals("INSERT INTO persona (nombre, apellidos, direccion, cargo) " +
                        "VALUES ('Ada', 'Lovelace', 'Calle Algoritmo 1, Madrid', 'Desarrolladora Senior')",
                ada.toInsertSql());
    }

    @Test
    @DisplayName("toUpdateSql escribe el UPDATE con el id en el WHERE")
    void toUpdateSqlEscribeElUpdate() {
        assertEquals("UPDATE persona SET nombre='Ada', apellidos='Lovelace', " +
                        "direccion='Calle Algoritmo 1, Madrid', cargo='Desarrolladora Senior' WHERE id=1",
                ada.toUpdateSql());
    }

    @Test
    @DisplayName("toDeleteSql escribe el DELETE con el id")
    void toDeleteSqlEscribeElDelete() {
        assertEquals("DELETE FROM persona WHERE id=1", ada.toDeleteSql());
    }

    @Test
    @DisplayName("toFichero escribe la persona en una línea separada por |")
    void toFicheroEscribeLaLinea() {
        assertEquals("1|Ada|Lovelace|Calle Algoritmo 1, Madrid|Desarrolladora Senior", ada.toFichero());
    }
}
