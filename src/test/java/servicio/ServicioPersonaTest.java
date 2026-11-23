package servicio;

import modelo.Persona;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soporte.BdDePruebas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// Tests de ServicioPersona (las búsquedas) contra la base de PRUEBAS.
class ServicioPersonaTest {

    @BeforeAll
    static void comprobarBaseDePruebas() throws Exception {
        BdDePruebas.comprobarQueEsDePruebas();
    }

    // Antes de CADA test: tablas vacías y dos personas ya guardadas.
    @BeforeEach
    void prepararDosPersonas() throws Exception {
        BdDePruebas.vaciarTablas();
        ServicioSql.insertarPersona(new Persona("Ada", "Lovelace", "Madrid", "Desarrolladora"));
        ServicioSql.insertarPersona(new Persona("Grace", "Hopper", "Sevilla", "Analista"));
    }

    @Test
    @DisplayName("buscarPersona devuelve la persona que tiene ese id")
    void buscarPersonaPorId() throws Exception {
        // Arrange: averiguamos el id que MySQL le dio a Grace (la segunda)
        int idGrace = ServicioSql.leerPersonas().get(1).getId();

        // Act
        Persona encontrada = ServicioPersona.buscarPersona(idGrace);

        // Assert
        assertEquals("Grace", encontrada.getNombre());
        assertEquals(idGrace, encontrada.getId());
    }

    @Test
    @DisplayName("buscarPersona devuelve null si el id no existe")
    void buscarPersonaInexistenteDevuelveNull() throws Exception {
        // assertNull(valor): pasa si el valor es null.
        assertNull(ServicioPersona.buscarPersona(-1));
    }

    @Test
    @DisplayName("buscarPersonaNombreApellido ignora mayúsculas y espacios sobrantes")
    void buscarPorNombreYApellidoIgnoraMayusculas() throws Exception {
        // Arrange: mismo nombre, pero escrito distinto
        Persona buscada = new Persona("  ADA ", " lovelace  ", "", "");

        // Act
        Persona encontrada = ServicioPersona.buscarPersonaNombreApellido(buscada);

        // Assert
        assertEquals("Ada", encontrada.getNombre());
        assertEquals("Lovelace", encontrada.getApellidos());
    }

    @Test
    @DisplayName("buscarPersonaNombreApellido devuelve null si no hay nadie con ese nombre")
    void buscarPorNombreYApellidoInexistente() throws Exception {
        Persona buscada = new Persona("Alan", "Turing", "", "");

        assertNull(ServicioPersona.buscarPersonaNombreApellido(buscada));
    }
}
