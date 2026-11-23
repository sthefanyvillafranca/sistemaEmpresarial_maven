package servicio;

import modelo.Persona;
import modelo.Proyecto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soporte.BdDePruebas;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Tests de ServicioMaestro.localizarIds contra la base de PRUEBAS.
// (Los métodos que escriben personas.txt, nominas.txt y proyectos.txt no se
// prueban aquí: sobrescribirían esos ficheros. La lectura y escritura de
// ficheros se prueba en FicheroTest, con una carpeta temporal.)
class ServicioMaestroTest {

    private int idAda;
    private int idWeb;

    @BeforeAll
    static void comprobarBaseDePruebas() throws Exception {
        BdDePruebas.comprobarQueEsDePruebas();
    }

    // Antes de CADA test: tablas vacías, una persona y un proyecto guardados.
    @BeforeEach
    void prepararPersonaYProyecto() throws Exception {
        BdDePruebas.vaciarTablas();
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        ServicioSql.insertarProyecto(new Proyecto("Web corporativa", LocalDate.of(2026, 12, 15), "Nueva web", false));
        idAda = ServicioSql.leerPersonas().get(0).getId();
        idWeb = ServicioSql.leerProyecto().get(0).getId();
    }

    @Test
    @DisplayName("localizarIds devuelve true si existen la persona Y el proyecto")
    void localizarIdsConLosDosExistentes() throws Exception {
        // assertTrue(condición): pasa si la condición es true.
        assertTrue(ServicioMaestro.localizarIds(idAda, idWeb));
    }

    @Test
    @DisplayName("localizarIds devuelve false si la persona no existe")
    void localizarIdsSinPersona() throws Exception {
        // assertFalse(condición): pasa si la condición es false.
        assertFalse(ServicioMaestro.localizarIds(-1, idWeb));
    }

    @Test
    @DisplayName("localizarIds devuelve false si el proyecto no existe")
    void localizarIdsSinProyecto() throws Exception {
        assertFalse(ServicioMaestro.localizarIds(idAda, -1));
    }
}
