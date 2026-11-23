package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Tests de Proyecto. No usan base de datos.
class ProyectoTest {

    private Proyecto web;

    // @BeforeEach: antes de CADA test, un proyecto nuevo:
    // id 1, entrega el 15 de diciembre de 2026, sin terminar.
    @BeforeEach
    void crearProyecto() {
        web = new Proyecto(1, "Web corporativa", LocalDate.of(2026, 12, 15), "Nueva web de la empresa", false);
    }

    @Test
    @DisplayName("El constructor con id guarda todos los datos")
    void elConstructorConIdGuardaLosDatos() {
        assertEquals(1, web.getId());
        assertEquals("Web corporativa", web.getNombre());
        assertEquals(LocalDate.of(2026, 12, 15), web.getDeadLine());
        assertEquals("Nueva web de la empresa", web.getDescripcion());
        // assertFalse(condición): pasa si la condición es false.
        assertFalse(web.isTerminado());
    }

    @Test
    @DisplayName("El constructor desde una línea de fichero separa los datos por |")
    void elConstructorDesdeCadenaSeparaLosDatos() {
        // Act
        Proyecto leido = new Proyecto("9|App movil|2027-03-01|Version para telefonos|true");

        // Assert
        assertEquals(9, leido.getId());
        assertEquals("App movil", leido.getNombre());
        assertEquals(LocalDate.of(2027, 3, 1), leido.getDeadLine());
        assertEquals("Version para telefonos", leido.getDescripcion());
        // assertTrue(condición): pasa si la condición es true.
        assertTrue(leido.isTerminado());
    }

    @Test
    @DisplayName("Los setters cambian su dato")
    void losSettersCambianSuDato() {
        // Act
        web.setNombre("Web nueva");
        web.setDeadLine(LocalDate.of(2027, 1, 31));
        web.setDescripcion("Cambio de planes");
        web.setTerminado(true);

        // Assert
        assertEquals("Web nueva", web.getNombre());
        assertEquals(LocalDate.of(2027, 1, 31), web.getDeadLine());
        assertEquals("Cambio de planes", web.getDescripcion());
        assertTrue(web.isTerminado());
    }

    @Test
    @DisplayName("toInsertSql escribe el INSERT con la fecha y el estado")
    void toInsertSqlEscribeElInsert() {
        assertEquals("INSERT INTO proyecto (nombre, deadLine, descripcion, terminado) " +
                        "VALUES ('Web corporativa','2026-12-15','Nueva web de la empresa',false)",
                web.toInsertSql());
    }

    @Test
    @DisplayName("toUpdateSql escribe el UPDATE con el id en el WHERE")
    void toUpdateSqlEscribeElUpdate() {
        assertEquals("UPDATE proyecto SET Nombre='Web corporativa', deadLine='2026-12-15', " +
                        "descripcion='Nueva web de la empresa', terminado=false WHERE ID=1",
                web.toUpdateSql());
    }

    @Test
    @DisplayName("toDeleteSql escribe el DELETE con el id")
    void toDeleteSqlEscribeElDelete() {
        assertEquals("DELETE FROM proyecto WHERE ID=1", web.toDeleteSql());
    }

    @Test
    @DisplayName("toFichero escribe el proyecto en una línea separada por |")
    void toFicheroEscribeLaLinea() {
        assertEquals("1|Web corporativa|2026-12-15|Nueva web de la empresa|false", web.toFichero());
    }
}
