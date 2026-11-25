package controlador;

import modelo.Nomina;
import modelo.Persona;
import modelo.Proyecto;
import servicio.ServicioSql;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soporte.BdDePruebas;
import soporte.SalidaCapturada;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Tests de ControladorMaestro contra la base de PRUEBAS (empresa_test).
// Se comprueba lo que se imprime por consola.
// Solo se prueban los métodos que NO leen del teclado: el menú principal
// y los que piden ids por teclado se quedan sin test.
class ControladorMaestroTest {

    @BeforeAll
    static void comprobarBaseDePruebas() throws Exception {
        BdDePruebas.comprobarQueEsDePruebas();
    }

    @BeforeEach
    void prepararBaseYSalida() throws Exception {
        BdDePruebas.vaciarTablas();
        SalidaCapturada.empezar();
    }

    @AfterEach
    void devolverSalida() {
        SalidaCapturada.terminar();
    }

    @Test
    @DisplayName("leerPersonaProyectos imprime el vínculo entre la persona y el proyecto")
    void leerPersonaProyectosImprimeElVinculo() throws Exception {
        // Arrange: Ada vinculada al proyecto web
        int idAda = guardarAdaYDevolverSuId();
        ServicioSql.insertarProyecto(new Proyecto("Web corporativa", LocalDate.of(2026, 12, 15), "Nueva web", false));
        int idWeb = ServicioSql.leerProyecto().get(0).getId();
        ServicioSql.insertarPersonaProyecto(idAda, idWeb);

        // Act
        ControladorMaestro.leerPersonaProyectos();

        // Assert
        assertTrue(SalidaCapturada.texto().contains("Persona: " + idAda + " - Proyecto: " + idWeb));
    }

    @Test
    @DisplayName("leerGroupByNominasPersona imprime cuántas nóminas tiene cada persona")
    void leerGroupByNominasPersonaImprimeElRecuento() throws Exception {
        // Arrange: Ada con dos nóminas
        int idAda = guardarAdaYDevolverSuId();
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "noviembre", idAda));
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "diciembre", idAda));

        // Act
        ControladorMaestro.leerGroupByNominasPersona();

        // Assert
        assertTrue(SalidaCapturada.texto().contains("Persona: " + idAda + " - 2 nominas"));
    }

    // Método de ayuda (no es un test: no lleva @Test)
    private int guardarAdaYDevolverSuId() throws Exception {
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        return ServicioSql.leerPersonas().get(0).getId();
    }
}
