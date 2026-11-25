package controlador;

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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Tests de ControladorProyecto contra la base de PRUEBAS (empresa_test).
// Se comprueba lo que queda en la base de datos y lo que se imprime por consola.
// Solo se prueban los métodos que NO leen del teclado.
class ControladorProyectoTest {

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
    @DisplayName("deleteProyecto con un proyecto que existe: lo elimina y avisa de que salió bien")
    void deleteProyectoQueExiste() throws Exception {
        // Arrange
        int idWeb = guardarWebYDevolverSuId();

        // Act
        ControladorProyecto.deleteProyecto(idWeb);

        // Assert
        assertTrue(ServicioSql.leerProyecto().isEmpty());
        assertTrue(SalidaCapturada.texto().contains("Operación realizada exitosamente"));
    }

    @Test
    @DisplayName("deleteProyecto con un id que no existe: avisa de que el dato no existe")
    void deleteProyectoQueNoExiste() throws Exception {
        // Act: no hay ningún proyecto con id -1
        ControladorProyecto.deleteProyecto(-1);

        // Assert
        assertTrue(SalidaCapturada.texto().contains("El dato que busca no existe"));
    }

    @Test
    @DisplayName("deleteProyecto con una persona vinculada: no lo elimina y avisa de los registros asociados")
    void deleteProyectoConPersonaVinculada() throws Exception {
        // Arrange: Ada está vinculada al proyecto
        int idWeb = guardarWebYDevolverSuId();
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        int idAda = ServicioSql.leerPersonas().get(0).getId();
        ServicioSql.insertarPersonaProyecto(idAda, idWeb);

        // Act: MySQL lanza el error 1451 y el controlador lo atrapa
        ControladorProyecto.deleteProyecto(idWeb);

        // Assert: el proyecto sigue en la base y el mensaje es el del error 1451
        assertEquals(1, ServicioSql.leerProyecto().size());
        assertTrue(SalidaCapturada.texto().contains("Hay registros asociados"));
    }

    @Test
    @DisplayName("leerProyectos imprime el total y los datos de cada proyecto")
    void leerProyectosImprimeElListado() throws Exception {
        // Arrange
        guardarWebYDevolverSuId();

        // Act
        ControladorProyecto.leerProyectos();

        // Assert
        assertTrue(SalidaCapturada.texto().contains("Total de proyectos: 1"));
        assertTrue(SalidaCapturada.texto().contains("Web corporativa"));
    }

    @Test
    @DisplayName("guardarModificacionProyecto guarda el cambio y avisa de que salió bien")
    void guardarModificacionProyectoGuardaElCambio() throws Exception {
        // Arrange: lo leemos de la base para tenerlo con el id que le dio MySQL
        guardarWebYDevolverSuId();
        Proyecto web = ServicioSql.leerProyecto().get(0);
        web.setTerminado(true);

        // Act
        ControladorProyecto.guardarModificacionProyecto(web);

        // Assert
        assertTrue(ServicioSql.leerProyecto().get(0).isTerminado());
        assertTrue(SalidaCapturada.texto().contains("Operación realizada exitosamente"));
    }

    // Método de ayuda (no es un test: no lleva @Test)
    private int guardarWebYDevolverSuId() throws Exception {
        ServicioSql.insertarProyecto(new Proyecto("Web corporativa", LocalDate.of(2026, 12, 15), "Nueva web", false));
        return ServicioSql.leerProyecto().get(0).getId();
    }
}
