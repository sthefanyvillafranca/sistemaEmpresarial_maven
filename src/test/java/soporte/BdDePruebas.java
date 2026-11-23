package soporte;

import repositorio.BaseDeDatos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

// NO es un test: es una ayuda que usan los tests que tocan la base de datos,
// para no repetir lo mismo en cada clase de test.
public class BdDePruebas {

    /**
     * SEGURIDAD: comprueba que application.properties apunta a una base
     * que termina en "_test". Si no, lanza un error y los tests se paran
     * antes de borrar nada. Así nunca se vacía mi base "empresa" por error.
     */
    public static void comprobarQueEsDePruebas() throws IOException {
        Properties props = new Properties();
        InputStream fichero = null;

        try {
            fichero = BdDePruebas.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(fichero);
        } finally {
            if (fichero != null) {
                fichero.close();
            }
        }

        String url = props.getProperty("db.url");
        if (!url.endsWith("_test")) {
            throw new IllegalStateException("Los tests NO apuntan a la BD de pruebas: " + url);
        }
    }

    /**
     * Deja las cuatro tablas vacías para que cada test empiece desde cero.
     * El orden importa por las llaves foráneas: primero las tablas "hijas"
     * (persona_proyecto y nomina) y después las "padres" (persona y proyecto).
     */
    public static void vaciarTablas() throws SQLException, ClassNotFoundException, IOException {
        comprobarQueEsDePruebas();

        BaseDeDatos.ejecutarSQL("DELETE FROM persona_proyecto");
        BaseDeDatos.ejecutarSQL("DELETE FROM nomina");
        BaseDeDatos.ejecutarSQL("DELETE FROM persona");
        BaseDeDatos.ejecutarSQL("DELETE FROM proyecto");
    }
}
