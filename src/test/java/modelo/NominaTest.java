package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Tests de Nomina. No usan base de datos.
class NominaTest {

    private Nomina nomina;

    // @BeforeEach: antes de CADA test, una nómina nueva:
    // id 7, 1500.50 euros, noviembre de 2026, de la persona con id 1.
    @BeforeEach
    void crearNomina() {
        nomina = new Nomina(7, new BigDecimal("1500.50"), 2026, "noviembre", 1);
    }

    @Test
    @DisplayName("El constructor con id guarda todos los datos")
    void elConstructorConIdGuardaLosDatos() {
        assertEquals(7, nomina.getId());
        assertEquals(new BigDecimal("1500.50"), nomina.getImporteFinal());
        assertEquals(2026, nomina.getYear());
        assertEquals("noviembre", nomina.getMes());
        assertEquals(1, nomina.getPersonaId());
    }

    @Test
    @DisplayName("El constructor sin id deja el id en 0 (lo pondrá MySQL)")
    void elConstructorSinIdDejaElIdEnCero() {
        Nomina nueva = new Nomina(new BigDecimal("2000.00"), 2026, "diciembre", 1);

        assertEquals(0, nueva.getId());
        assertEquals(1, nueva.getPersonaId());
    }

    @Test
    @DisplayName("El constructor desde una línea de fichero separa los datos por |")
    void elConstructorDesdeCadenaSeparaLosDatos() {
        // Act
        Nomina leida = new Nomina("3|1800.75|2027|enero|4");

        // Assert
        assertEquals(3, leida.getId());
        assertEquals(new BigDecimal("1800.75"), leida.getImporteFinal());
        assertEquals(2027, leida.getYear());
        assertEquals("enero", leida.getMes());
        assertEquals(4, leida.getPersonaId());
    }

    @Test
    @DisplayName("Los setters cambian su dato y no tocan la persona")
    void losSettersCambianSuDato() {
        // Act
        nomina.setImporteFinal(new BigDecimal("1800.75"));
        nomina.setYear(2027);
        nomina.setMes("enero");

        // Assert
        assertEquals(new BigDecimal("1800.75"), nomina.getImporteFinal());
        assertEquals(2027, nomina.getYear());
        assertEquals("enero", nomina.getMes());
        assertEquals(1, nomina.getPersonaId()); // no hay setter para la persona
    }

    @Test
    @DisplayName("toInsertSql escribe el INSERT con el id de la persona")
    void toInsertSqlEscribeElInsert() {
        assertEquals("INSERT INTO nomina (importeFinal, year, mes, id_persona) " +
                        "VALUES ( 1500.50, 2026, 'noviembre', 1)",
                nomina.toInsertSql());
    }

    @Test
    @DisplayName("toUpdateSql escribe el UPDATE con el id en el WHERE")
    void toUpdateSqlEscribeElUpdate() {
        assertEquals("UPDATE nomina SET importeFinal=1500.50, year=2026, mes='noviembre', id_persona=1 " +
                        "WHERE id=7",
                nomina.toUpdateSql());
    }

    @Test
    @DisplayName("toDeleteSql escribe el DELETE con el id")
    void toDeleteSqlEscribeElDelete() {
        assertEquals("DELETE FROM nomina WHERE id=7", nomina.toDeleteSql());
    }

    @Test
    @DisplayName("toFichero escribe la nómina en una línea separada por |")
    void toFicheroEscribeLaLinea() {
        assertEquals("7|1500.50|2026|noviembre|1", nomina.toFichero());
    }
}
