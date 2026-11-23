package servicio;

import modelo.Proyecto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soporte.BdDePruebas;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// Tests de ServicioProyecto (las búsquedas) contra la base de PRUEBAS.
class ServicioProyectoTest {

    @BeforeAll
    static void comprobarBaseDePruebas() throws Exception {
        BdDePruebas.comprobarQueEsDePruebas();
    }

    // Antes de CADA test: tablas vacías y un proyecto ya guardado.
    @BeforeEach
    void prepararUnProyecto() throws Exception {
        BdDePruebas.vaciarTablas();
        ServicioSql.insertarProyecto(new Proyecto("Web corporativa", LocalDate.of(2026, 12, 15), "Nueva web", false));
    }

    @Test
    @DisplayName("buscarProyecto devuelve el proyecto que tiene ese id")
    void buscarProyectoPorId() throws Exception {
        // Arrange
        int idWeb = ServicioSql.leerProyecto().get(0).getId();

        // Act
        Proyecto encontrado = ServicioProyecto.buscarProyecto(idWeb);

        // Assert
        assertEquals("Web corporativa", encontrado.getNombre());
        assertEquals(LocalDate.of(2026, 12, 15), encontrado.getDeadLine());
    }

    @Test
    @DisplayName("buscarProyecto devuelve null si el id no existe")
    void buscarProyectoInexistenteDevuelveNull() throws Exception {
        assertNull(ServicioProyecto.buscarProyecto(-1));
    }

    @Test
    @DisplayName("buscarProyectoPorNombre ignora mayúsculas y espacios sobrantes")
    void buscarPorNombreIgnoraMayusculas() throws Exception {
        // Arrange
        Proyecto buscado = new Proyecto("  WEB CORPORATIVA ", LocalDate.of(2000, 1, 1), "", false);

        // Act
        Proyecto encontrado = ServicioProyecto.buscarProyectoPorNombre(buscado);

        // Assert
        assertEquals("Web corporativa", encontrado.getNombre());
    }

    @Test
    @DisplayName("buscarProyectoPorNombre devuelve null si no hay ninguno con ese nombre")
    void buscarPorNombreInexistente() throws Exception {
        Proyecto buscado = new Proyecto("App movil", LocalDate.of(2000, 1, 1), "", false);

        assertNull(ServicioProyecto.buscarProyectoPorNombre(buscado));
    }
}
