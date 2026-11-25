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

// Tests de ControladorPersona contra la base de PRUEBAS (empresa_test).
//
// El controlador no devuelve nada, así que se comprueba por sus dos efectos:
// lo que queda en la base de datos y lo que imprime por consola.
// Solo se prueban los métodos que NO leen del teclado.
class ControladorPersonaTest {

    @BeforeAll
    static void comprobarBaseDePruebas() throws Exception {
        BdDePruebas.comprobarQueEsDePruebas();
    }

    // Antes de CADA test: tablas vacías y la consola guardándose en la caja.
    @BeforeEach
    void prepararBaseYSalida() throws Exception {
        BdDePruebas.vaciarTablas();
        SalidaCapturada.empezar();
    }

    // Después de CADA test: System.out vuelve a la pantalla.
    @AfterEach
    void devolverSalida() {
        SalidaCapturada.terminar();
    }

    @Test
    @DisplayName("deletePersona con una persona que existe: la elimina y avisa de que salió bien")
    void deletePersonaQueExiste() throws Exception {
        // Arrange
        int idAda = guardarAdaYDevolverSuId();

        // Act
        ControladorPersona.deletePersona(idAda);

        // Assert: ya no está en la base y se imprimió el mensaje correcto
        assertTrue(ServicioSql.leerPersonas().isEmpty());
        assertTrue(SalidaCapturada.texto().contains("Operación realizada exitosamente"));
    }

    @Test
    @DisplayName("deletePersona con un id que no existe: avisa de que el dato no existe")
    void deletePersonaQueNoExiste() throws Exception {
        // Act: no hay ninguna persona con id -1
        ControladorPersona.deletePersona(-1);

        // Assert
        assertTrue(SalidaCapturada.texto().contains("El dato que busca no existe"));
    }

    @Test
    @DisplayName("deletePersona con una persona que tiene nóminas: no la elimina y avisa de los registros asociados")
    void deletePersonaConNominas() throws Exception {
        // Arrange
        int idAda = guardarAdaYDevolverSuId();
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "noviembre", idAda));

        // Act: MySQL lanza el error 1451 y el controlador lo atrapa
        ControladorPersona.deletePersona(idAda);

        // Assert: Ada sigue en la base y el mensaje es el del error 1451
        assertEquals(1, ServicioSql.leerPersonas().size());
        assertTrue(SalidaCapturada.texto().contains("Hay registros asociados"));
    }

    @Test
    @DisplayName("leerPersonas imprime el total y los datos de cada persona")
    void leerPersonasImprimeElListado() throws Exception {
        // Arrange
        guardarAdaYDevolverSuId();

        // Act
        ControladorPersona.leerPersonas();

        // Assert
        assertTrue(SalidaCapturada.texto().contains("Total de empleados: 1"));
        assertTrue(SalidaCapturada.texto().contains("Ada Lovelace"));
    }

    @Test
    @DisplayName("guardarModificacionPersona guarda el cambio y avisa de que salió bien")
    void guardarModificacionPersonaGuardaElCambio() throws Exception {
        // Arrange: la leemos de la base para tenerla con el id que le dio MySQL
        guardarAdaYDevolverSuId();
        Persona ada = ServicioSql.leerPersonas().get(0);
        ada.setCargo("Jefa de equipo");

        // Act
        ControladorPersona.guardarModificacionPersona(ada);

        // Assert
        assertEquals("Jefa de equipo", ServicioSql.leerPersonas().get(0).getCargo());
        assertTrue(SalidaCapturada.texto().contains("Operación realizada exitosamente"));
    }

    // Método de ayuda (no es un test: no lleva @Test)
    private int guardarAdaYDevolverSuId() throws Exception {
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        return ServicioSql.leerPersonas().get(0).getId();
    }
}
