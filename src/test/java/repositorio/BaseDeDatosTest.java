package repositorio;

import modelo.Persona;
import modelo.Proyecto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soporte.BdDePruebas;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Tests de BaseDeDatos contra la base de datos de PRUEBAS (empresa_test).
//
// Aquí no hay try/catch: el control de excepciones va en el código real
// (en los controladores). "throws Exception" solo dice: si BaseDeDatos lanza
// un error inesperado, que suba hasta JUnit, y JUnit pinta el test en rojo.
class BaseDeDatosTest {

    // @BeforeAll: se ejecuta UNA vez, antes de todos los tests de la clase.
    // Es static porque se ejecuta antes de que exista ningún objeto BaseDeDatosTest.
    @BeforeAll
    static void comprobarBaseDePruebas() throws Exception {
        BdDePruebas.comprobarQueEsDePruebas();
    }

    // @BeforeEach: se ejecuta antes de CADA test. Deja las tablas vacías
    // para que ningún test dependa de lo que dejó otro.
    @BeforeEach
    void vaciarTablas() throws Exception {
        BdDePruebas.vaciarTablas();
    }

    @Test
    @DisplayName("conectar devuelve una conexión abierta")
    void conectarDevuelveConexionAbierta() throws Exception {
        // Act
        Connection conexion = BaseDeDatos.conectar();

        // Assert: assertFalse(condición) pasa si la condición es false.
        assertFalse(conexion.isClosed());

        conexion.close();
    }

    @Test
    @DisplayName("ejecutarSQL con un INSERT devuelve 1 (una fila afectada)")
    void ejecutarSqlInsertDevuelveUno() throws Exception {
        // Act
        int filas = BaseDeDatos.ejecutarSQL("INSERT INTO persona (nombre, apellidos, direccion, cargo) " +
                "VALUES ('Ada', 'Lovelace', 'Madrid', 'Desarrolladora')");

        // Assert: assertEquals(lo que espero, lo que ha salido)
        assertEquals(1, filas);
    }

    @Test
    @DisplayName("ejecutarSQL con un UPDATE que no encuentra a nadie devuelve 0")
    void ejecutarSqlUpdateSinCoincidenciaDevuelveCero() throws Exception {
        // Act: no hay ninguna persona con id -1
        int filas = BaseDeDatos.ejecutarSQL("UPDATE persona SET cargo = 'Nadie' WHERE id = -1");

        // Assert
        assertEquals(0, filas);
    }

    @Test
    @DisplayName("ejecutarSQL con un SQL mal escrito lanza SQLException")
    void ejecutarSqlMalEscritoLanzaExcepcion() {
        // assertThrows(tipo de error, código): pasa si ese código lanza ese error.
        // "() -> ..." es el trozo de código que JUnit ejecuta y vigila.
        assertThrows(SQLException.class,
                () -> BaseDeDatos.ejecutarSQL("INSERT INTO tabla_que_no_existe VALUES (1)"));
    }

    @Test
    @DisplayName("A través de InterfaceDatos: agregar, modificar y eliminar devuelven las filas afectadas")
    void agregarModificarYEliminarPorLaInterfaz() throws Exception {
        // Arrange: la variable es del tipo de la INTERFAZ, no de la clase.
        // Solo sabe que "datos" tiene agregar, modificar y eliminar.
        InterfaceDatos datos = new BaseDeDatos();

        // Act + Assert
        assertEquals(1, datos.agregar("INSERT INTO persona (nombre, apellidos, direccion, cargo) " +
                "VALUES ('Ada', 'Lovelace', 'Madrid', 'Desarrolladora')"));
        assertEquals(1, datos.modificar("UPDATE persona SET cargo = 'Jefa de equipo' WHERE nombre = 'Ada'"));
        assertEquals(1, datos.eliminar("DELETE FROM persona WHERE nombre = 'Ada'"));
    }

    @Test
    @DisplayName("Con la tabla vacía, ejecutarConsultaPersona devuelve una lista vacía")
    void consultaPersonaConTablaVacia() throws Exception {
        // Act
        ArrayList<Persona> lista = BaseDeDatos.ejecutarConsultaPersona();

        // Assert: assertTrue(condición) pasa si la condición es true.
        assertTrue(lista.isEmpty());
    }

    @Test
    @DisplayName("ejecutarConsultaPersona convierte cada fila en un objeto Persona")
    void consultaPersonaDevuelveObjetos() throws Exception {
        // Arrange
        BaseDeDatos.ejecutarSQL("INSERT INTO persona (nombre, apellidos, direccion, cargo) " +
                "VALUES ('Ada', 'Lovelace', 'Madrid', 'Desarrolladora')");

        // Act
        ArrayList<Persona> lista = BaseDeDatos.ejecutarConsultaPersona();

        // Assert
        assertEquals(1, lista.size());
        assertEquals("Ada", lista.get(0).getNombre());
        assertEquals("Lovelace", lista.get(0).getApellidos());
        assertEquals("Madrid", lista.get(0).getDireccion());
        assertEquals("Desarrolladora", lista.get(0).getCargo());
    }

    @Test
    @DisplayName("ejecutarConsultaProyecto recupera la fecha y el estado")
    void consultaProyectoDevuelveFechaYEstado() throws Exception {
        // Arrange
        BaseDeDatos.ejecutarSQL("INSERT INTO proyecto (nombre, deadLine, descripcion, terminado) " +
                "VALUES ('Web corporativa', '2026-12-15', 'Nueva web', false)");

        // Act
        ArrayList<Proyecto> lista = BaseDeDatos.ejecutarConsultaProyecto();

        // Assert
        assertEquals(1, lista.size());
        assertEquals("Web corporativa", lista.get(0).getNombre());
        assertEquals(LocalDate.of(2026, 12, 15), lista.get(0).getDeadLine());
        assertFalse(lista.get(0).isTerminado());
    }

    @Test
    @DisplayName("ejecutarConsultaGroup cuenta las nóminas de cada persona")
    void consultaGroupCuentaNominas() throws Exception {
        // Arrange: una persona con dos nóminas
        BaseDeDatos.ejecutarSQL("INSERT INTO persona (nombre, apellidos, direccion, cargo) " +
                "VALUES ('Ada', 'Lovelace', 'Madrid', 'Desarrolladora')");
        int idAda = BaseDeDatos.ejecutarConsultaPersona().get(0).getId();
        BaseDeDatos.ejecutarSQL("INSERT INTO nomina (importeFinal, year, mes, id_persona) " +
                "VALUES (1500.50, 2026, 'noviembre', " + idAda + ")");
        BaseDeDatos.ejecutarSQL("INSERT INTO nomina (importeFinal, year, mes, id_persona) " +
                "VALUES (1500.50, 2026, 'diciembre', " + idAda + ")");

        // Act
        ArrayList<String> lista = BaseDeDatos.ejecutarConsultaGroup();

        // Assert: una línea por persona, con el formato que le di en BaseDeDatos
        assertEquals(1, lista.size());
        assertEquals("Persona: " + idAda + " - 2 nominas", lista.get(0));
    }
}
