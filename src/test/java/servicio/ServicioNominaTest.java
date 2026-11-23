package servicio;

import modelo.Nomina;
import modelo.Persona;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soporte.BdDePruebas;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// Tests de ServicioNomina (las búsquedas) contra la base de PRUEBAS.
class ServicioNominaTest {

    // El id que MySQL le da a Ada en cada test. La nómina se cuelga de este id.
    private int idAda;

    @BeforeAll
    static void comprobarBaseDePruebas() throws Exception {
        BdDePruebas.comprobarQueEsDePruebas();
    }

    // Antes de CADA test: tablas vacías, Ada guardada y su nómina de noviembre de 2026.
    @BeforeEach
    void prepararPersonaConNomina() throws Exception {
        BdDePruebas.vaciarTablas();
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        idAda = ServicioSql.leerPersonas().get(0).getId();
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "noviembre", idAda));
    }

    @Test
    @DisplayName("buscarNomina devuelve la nómina que tiene ese id")
    void buscarNominaPorId() throws Exception {
        // Arrange
        int idNomina = ServicioSql.leerNomina().get(0).getId();

        // Act
        Nomina encontrada = ServicioNomina.buscarNomina(idNomina);

        // Assert
        assertEquals("noviembre", encontrada.getMes());
        assertEquals(idAda, encontrada.getPersonaId());
    }

    @Test
    @DisplayName("buscarNomina devuelve null si el id no existe")
    void buscarNominaInexistenteDevuelveNull() throws Exception {
        assertNull(ServicioNomina.buscarNomina(-1));
    }

    @Test
    @DisplayName("buscarNominaPorPersonaYearMes encuentra la nómina aunque el mes venga en mayúsculas")
    void buscarPorPersonaYearMes() throws Exception {
        // Arrange: misma persona, mismo año, mismo mes escrito distinto
        Nomina buscada = new Nomina(new BigDecimal("0"), 2026, " NOVIEMBRE ", idAda);

        // Act
        Nomina encontrada = ServicioNomina.buscarNominaPorPersonaYearMes(buscada);

        // Assert
        assertEquals("noviembre", encontrada.getMes());
        assertEquals(2026, encontrada.getYear());
    }

    @Test
    @DisplayName("buscarNominaPorPersonaYearMes devuelve null si esa persona no tiene nómina ese mes")
    void buscarPorPersonaYearMesInexistente() throws Exception {
        Nomina buscada = new Nomina(new BigDecimal("0"), 2026, "diciembre", idAda);

        assertNull(ServicioNomina.buscarNominaPorPersonaYearMes(buscada));
    }
}
