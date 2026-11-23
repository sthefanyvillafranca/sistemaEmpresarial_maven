package servicio;

import modelo.Nomina;
import modelo.Persona;
import modelo.Proyecto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soporte.BdDePruebas;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Tests de ServicioSql contra la base de datos de PRUEBAS (empresa_test).
// Es la clase más importante de probar: por aquí pasan todos los
// INSERT, UPDATE, DELETE y SELECT del programa.
class ServicioSqlTest {

    // @BeforeAll: UNA vez antes de todos los tests (por eso es static).
    @BeforeAll
    static void comprobarBaseDePruebas() throws Exception {
        BdDePruebas.comprobarQueEsDePruebas();
    }

    // @BeforeEach: antes de CADA test, tablas vacías.
    @BeforeEach
    void vaciarTablas() throws Exception {
        BdDePruebas.vaciarTablas();
    }

    // ───────────────────────────── PERSONA ─────────────────────────────

    @Test
    @DisplayName("Persona: insertar y leer devuelve la persona con sus datos")
    void insertarYLeerPersona() throws Exception {
        // Arrange
        Persona ada = new Persona("Ada", "Lovelace", "Calle Algoritmo 1, Madrid", "Desarrolladora Senior");

        // Act
        ServicioSql.insertarPersona(ada);
        ArrayList<Persona> lista = ServicioSql.leerPersonas();

        // Assert
        assertEquals(1, lista.size());
        assertEquals("Ada", lista.get(0).getNombre());
        assertEquals("Lovelace", lista.get(0).getApellidos());
        assertEquals("Calle Algoritmo 1, Madrid", lista.get(0).getDireccion());
        assertEquals("Desarrolladora Senior", lista.get(0).getCargo());
    }

    @Test
    @DisplayName("Persona: modificar devuelve 1 y el cambio queda guardado")
    void modificarPersona() throws Exception {
        // Arrange: la leemos de la base para tenerla con el id que le dio MySQL
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        Persona ada = ServicioSql.leerPersonas().get(0);

        // Act
        ada.setCargo("Jefa de equipo");
        int filas = ServicioSql.modificarPersona(ada);

        // Assert: volvemos a leerla de la base
        assertEquals(1, filas);
        assertEquals("Jefa de equipo", ServicioSql.leerPersonas().get(0).getCargo());
    }

    @Test
    @DisplayName("Persona: eliminar devuelve 1 y la persona desaparece")
    void eliminarPersona() throws Exception {
        // Arrange
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        Persona ada = ServicioSql.leerPersonas().get(0);

        // Act
        int filas = ServicioSql.eliminarPersona(ada);

        // Assert
        assertEquals(1, filas);
        assertTrue(ServicioSql.leerPersonas().isEmpty());
    }

    // ───────────────────────────── PROYECTO ─────────────────────────────

    @Test
    @DisplayName("Proyecto: insertar, modificar y eliminar")
    void insertarModificarYEliminarProyecto() throws Exception {
        // Insertar
        ServicioSql.insertarProyecto(new Proyecto("Web corporativa", LocalDate.of(2026, 12, 15), "Nueva web", false));
        Proyecto web = ServicioSql.leerProyecto().get(0);
        assertEquals("Web corporativa", web.getNombre());
        assertEquals(LocalDate.of(2026, 12, 15), web.getDeadLine());

        // Modificar
        web.setTerminado(true);
        assertEquals(1, ServicioSql.modificarProyecto(web));
        assertTrue(ServicioSql.leerProyecto().get(0).isTerminado());

        // Eliminar
        assertEquals(1, ServicioSql.eliminarProyecto(web));
        assertTrue(ServicioSql.leerProyecto().isEmpty());
    }

    // ─────────────────────── NOMINA y la relación 1:N ───────────────────────

    @Test
    @DisplayName("Nomina: insertar, modificar y eliminar una nómina de una persona")
    void insertarModificarYEliminarNomina() throws Exception {
        // Arrange: una nómina necesita una persona
        int idAda = guardarAdaYDevolverSuId();

        // Insertar
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "noviembre", idAda));
        Nomina nomina = ServicioSql.leerNomina().get(0);
        assertEquals("noviembre", nomina.getMes());
        assertEquals(idAda, nomina.getPersonaId());
        // Con BigDecimal se compara con compareTo: devuelve 0 si valen lo mismo.
        assertEquals(0, new BigDecimal("1500.50").compareTo(nomina.getImporteFinal()));

        // Modificar
        nomina.setMes("diciembre");
        assertEquals(1, ServicioSql.modificarNomina(nomina));
        assertEquals("diciembre", ServicioSql.leerNomina().get(0).getMes());

        // Eliminar
        assertEquals(1, ServicioSql.eliminarNomina(nomina));
        assertTrue(ServicioSql.leerNomina().isEmpty());
    }

    @Test
    @DisplayName("1:N - una persona puede tener varias nóminas")
    void unaPersonaPuedeTenerVariasNominas() throws Exception {
        // Arrange
        int idAda = guardarAdaYDevolverSuId();
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "noviembre", idAda));
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "diciembre", idAda));

        // Act
        ArrayList<String> lista = ServicioSql.consultaGroupNominasPersona();

        // Assert
        assertEquals(1, lista.size());
        assertEquals("Persona: " + idAda + " - 2 nominas", lista.get(0));
    }

    @Test
    @DisplayName("1:N - una nómina no puede existir sin una persona")
    void nominaSinPersonaLanzaExcepcion() {
        // Arrange: no hay ninguna persona con id -1
        Nomina huerfana = new Nomina(new BigDecimal("1000.00"), 2026, "enero", -1);

        // Act + Assert: assertThrows devuelve el error para poder mirarlo.
        // "() -> ..." es el trozo de código que JUnit ejecuta y vigila.
        SQLException error = assertThrows(SQLException.class, () -> ServicioSql.insertarNomina(huerfana));

        // 1452: código de MySQL para "la llave foránea no encuentra a su padre"
        assertEquals(1452, error.getErrorCode());
    }

    @Test
    @DisplayName("1:N - no se puede eliminar a una persona que tiene nóminas")
    void eliminarPersonaConNominasLanzaExcepcion() throws Exception {
        // Arrange
        int idAda = guardarAdaYDevolverSuId();
        ServicioSql.insertarNomina(new Nomina(new BigDecimal("1500.50"), 2026, "noviembre", idAda));
        Persona ada = ServicioSql.leerPersonas().get(0);

        // Act + Assert
        SQLException error = assertThrows(SQLException.class, () -> ServicioSql.eliminarPersona(ada));

        // 1451: el mismo código que compruebo en ControladorPersona.deletePersona()
        // para mostrar "Hay registros asociados".
        assertEquals(1451, error.getErrorCode());
    }

    // ─────────────────── PERSONA_PROYECTO: la relación M:N ───────────────────

    @Test
    @DisplayName("M:N - vincular una persona a un proyecto y quitar el vínculo")
    void insertarYEliminarPersonaProyecto() throws Exception {
        // Arrange
        int idAda = guardarAdaYDevolverSuId();
        int idWeb = guardarWebYDevolverSuId();

        // Act + Assert: vincular
        assertEquals(1, ServicioSql.insertarPersonaProyecto(idAda, idWeb));
        ArrayList<String> vinculos = ServicioSql.leerPersonaProyectos();
        assertEquals(1, vinculos.size());
        assertEquals("Persona: " + idAda + " - Proyecto: " + idWeb, vinculos.get(0));

        // Act + Assert: quitar el vínculo
        assertEquals(1, ServicioSql.deletePersonaProyecto(idAda, idWeb));
        assertTrue(ServicioSql.leerPersonaProyectos().isEmpty());
    }

    @Test
    @DisplayName("M:N - quitar un vínculo que no existe devuelve 0")
    void eliminarVinculoInexistenteDevuelveCero() throws Exception {
        // Arrange: existen la persona y el proyecto, pero no están vinculados
        int idAda = guardarAdaYDevolverSuId();
        int idWeb = guardarWebYDevolverSuId();

        // Act
        int filas = ServicioSql.deletePersonaProyecto(idAda, idWeb);

        // Assert
        assertEquals(0, filas);
    }

    @Test
    @DisplayName("M:N - vincular dos veces a la misma persona y proyecto lanza el error 1062")
    void vincularDosVecesLanzaExcepcion() throws Exception {
        // Arrange
        int idAda = guardarAdaYDevolverSuId();
        int idWeb = guardarWebYDevolverSuId();
        ServicioSql.insertarPersonaProyecto(idAda, idWeb);

        // Act + Assert
        SQLException error = assertThrows(SQLException.class,
                () -> ServicioSql.insertarPersonaProyecto(idAda, idWeb));

        // 1062: duplicado. Es el código que compruebo en
        // ControladorMaestro.insertarPersonaProyecto().
        assertEquals(1062, error.getErrorCode());
    }

    @Test
    @DisplayName("M:N - no se puede eliminar a una persona vinculada a un proyecto")
    void eliminarPersonaVinculadaLanzaExcepcion() throws Exception {
        // Arrange
        int idAda = guardarAdaYDevolverSuId();
        int idWeb = guardarWebYDevolverSuId();
        ServicioSql.insertarPersonaProyecto(idAda, idWeb);
        Persona ada = ServicioSql.leerPersonas().get(0);

        // Act + Assert: primero habría que eliminar el vínculo
        SQLException error = assertThrows(SQLException.class, () -> ServicioSql.eliminarPersona(ada));
        assertEquals(1451, error.getErrorCode());
    }

    // ───────────── Métodos de ayuda (no son tests: no llevan @Test) ─────────────

    private int guardarAdaYDevolverSuId() throws Exception {
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        return ServicioSql.leerPersonas().get(0).getId();
    }

    private int guardarWebYDevolverSuId() throws Exception {
        ServicioSql.insertarProyecto(new Proyecto("Web corporativa", LocalDate.of(2026, 12, 15), "Nueva web", false));
        return ServicioSql.leerProyecto().get(0).getId();
    }
}
