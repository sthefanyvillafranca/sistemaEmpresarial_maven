package controlador;

import modelo.Nomina;
import modelo.Persona;
import servicio.ServicioSql;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soporte.BdDePruebas;
import soporte.SalidaCapturada;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Tests de ControladorNomina contra la base de PRUEBAS (empresa_test).
// Se comprueba lo que queda en la base de datos y lo que se imprime por consola.
// Solo se prueban los métodos que NO leen del teclado.
class ControladorNominaTest {

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
    @DisplayName("deleteNomina con una nómina que existe: la elimina y avisa de que salió bien")
    void deleteNominaQueExiste() throws Exception {
        // Arrange
        int idNomina = guardarNominaDeAdaYDevolverSuId();

        // Act
        ControladorNomina.deleteNomina(idNomina);

        // Assert
        assertTrue(ServicioSql.leerNomina().isEmpty());
        assertTrue(SalidaCapturada.texto().contains("Operación realizada exitosamente"));
    }

    @Test
    @DisplayName("deleteNomina con un id que no existe: avisa de que el dato no existe")
    void deleteNominaQueNoExiste() throws Exception {
        // Act: no hay ninguna nómina con id -1
        ControladorNomina.deleteNomina(-1);

        // Assert
        assertTrue(SalidaCapturada.texto().contains("El dato que busca no existe"));
    }

    @Test
    @DisplayName("leerNominas imprime el total y los datos de cada nómina")
    void leerNominasImprimeElListado() throws Exception {
        // Arrange
        guardarNominaDeAdaYDevolverSuId();

        // Act
        ControladorNomina.leerNominas();

        // Assert
        assertTrue(SalidaCapturada.texto().contains("Total de nóminas: 1"));
        assertTrue(SalidaCapturada.texto().contains("noviembre"));
    }

    @Test
    @DisplayName("guardarModificacionNomina guarda el cambio y avisa de que salió bien")
    void guardarModificacionNominaGuardaElCambio() throws Exception {
        // Arrange: la leemos de la base para tenerla con el id que le dio MySQL
        guardarNominaDeAdaYDevolverSuId();
        Nomina nomina = ServicioSql.leerNomina().get(0);
        nomina.setMes("diciembre");

        // Act
        ControladorNomina.guardarModificacionNomina(nomina);

        // Assert
        assertEquals("diciembre", ServicioSql.leerNomina().get(0).getMes());
        assertTrue(SalidaCapturada.texto().contains("Operación realizada exitosamente"));
    }

    // Método de ayuda (no es un test: no lleva @Test).
    // Una nómina necesita una persona, así que primero guarda a Ada.
    private int guardarNominaDeAdaYDevolverSuId() throws Exception {
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        int idAda = ServicioSql.leerPersonas().get(0).getId();
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "noviembre", idAda));
        return ServicioSql.leerNomina().get(0).getId();
    }
}
