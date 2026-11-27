package repositorio;

import modelo.Nomina;
import modelo.Persona;
import modelo.Proyecto;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import java.util.ArrayList;
import java.util.Properties;
import java.math.BigDecimal;
// pregunté chatgpt esto "como representas un decimal(10,2)
// en java y me sugerio bigDecicmal e importar esto

/**
 * Ejecuta operaciones SQL
 */
public class BaseDeDatos implements InterfaceDatos {


    public static Connection conectar() throws SQLException, ClassNotFoundException {
        Connection conexion = null;
        Properties props = new Properties(); // contenedor clave -> valor
        InputStream fichero = null;

        // CAMBIO respecto a mi proyecto original: la dirección, el usuario y la
        // contraseña ya NO están escritos aquí. Se leen de application.properties.
        // Así la aplicación usa src/main/resources/application.properties
        // y los tests usan src/test/resources/application.properties (base de pruebas),
        // sin tocar este código.
        try {
            fichero = BaseDeDatos.class.getClassLoader().getResourceAsStream("application.properties");
            if (fichero == null) {
                throw new SQLException("No encuentro application.properties en resources");
            }
            props.load(fichero); // lee cada línea y la parte por el "="
        } catch (IOException e) {
            throw new SQLException("No se pudo leer application.properties", e);
        } finally {
            try {
                if (fichero != null) {
                    fichero.close();
                }
            } catch (IOException e) {
                e.printStackTrace(); // fuga de información
            }
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.pass"));

        return conexion;
    }


    public static int ejecutarSQL(String miSql) throws SQLException, ClassNotFoundException {
        int respuesta = 0;
        Connection conexion = null;
        Statement sentenciaSql = null;

        try {

            conexion = conectar();

            sentenciaSql = conexion.createStatement();

            respuesta = sentenciaSql.executeUpdate(miSql); // esto sirve para: INSERT, UPDATE, DELETE!!!
            //respuesta regresa un int

        } finally {
            try {
                if (sentenciaSql != null) {
                    sentenciaSql.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); //fuga de información
            }
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return respuesta; //devuelve cuantas filas se han vistoo afectadas
    }

    // Los tres métodos que exige InterfaceDatos. Los tres hacen lo mismo:
    // llamar a mi ejecutarSQL, que sirve para INSERT, UPDATE y DELETE.
    // No son static porque los métodos de una interfaz se llaman sobre un objeto.
    @Override
    public int agregar(String sql) throws SQLException, ClassNotFoundException {
        return ejecutarSQL(sql);
    }

    @Override
    public int modificar(String sql) throws SQLException, ClassNotFoundException {
        return ejecutarSQL(sql);
    }

    @Override
    public int eliminar(String sql) throws SQLException, ClassNotFoundException {
        return ejecutarSQL(sql);
    }

    public static ArrayList<Persona> ejecutarConsultaPersona() throws SQLException, ClassNotFoundException {
        // respetar el meter la conexion dentro de la misma clase
        ArrayList<Persona> lista = new ArrayList();
        Persona persona;
        String consulta = "SELECT ID,NOMBRE,APELLIDOS,DIRECCION,CARGO FROM PERSONA";

        Connection conexion = null;
        Statement sentenciaSql = null;
        ResultSet respuesta = null;

        try {
            conexion = conectar();
            sentenciaSql = conexion.createStatement();
            respuesta = sentenciaSql.executeQuery(consulta);


            while (respuesta.next()) {

                persona = new Persona((respuesta.getInt("ID")),
                        (respuesta.getString("Nombre")),
                        (respuesta.getString("Apellidos")),
                        (respuesta.getString("Direccion")),
                        (respuesta.getString("cargo")));
                lista.add(persona);
            }
        } finally {

            try {
                if (respuesta != null) {
                    respuesta.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // fuga de información
            }

            try {
                if (sentenciaSql != null) {
                    sentenciaSql.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // fuga de información
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;

    }

    public static ArrayList<Nomina> ejecutarConsultaNomina() throws SQLException, ClassNotFoundException {
        ArrayList<Nomina> lista = new ArrayList();
        Nomina nomina;
        String consulta = "SELECT ID,importeFinal,year,mes,id_persona FROM nomina";

        Connection conexion = null;
        Statement sentenciaSql = null;
        ResultSet respuesta = null;
        try {
            conexion = conectar();
            sentenciaSql = conexion.createStatement();
            respuesta = sentenciaSql.executeQuery(consulta);


            while (respuesta.next()) {

                nomina = new Nomina((respuesta.getInt("ID")),
                        (respuesta.getBigDecimal("importeFinal")),
                        (respuesta.getInt("year")),
                        (respuesta.getString("mes")),
                        (respuesta.getInt("id_persona")));
                lista.add(nomina);
            }

        } finally {
            try {
                if (respuesta != null) {
                    respuesta.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // fuga de información y se salta la vista
            }

            try {
                if (sentenciaSql != null) {
                    sentenciaSql.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    public static ArrayList<Proyecto> ejecutarConsultaProyecto() throws SQLException, ClassNotFoundException {
        ArrayList<Proyecto> lista = new ArrayList();
        Proyecto proyecto;
        String consulta = "SELECT ID,nombre,Deadline,Descripcion,terminado FROM proyecto";

        Connection conexion = null;
        Statement sentenciaSql = null;
        ResultSet respuesta = null;

        try {
            conexion = conectar();
            sentenciaSql = conexion.createStatement();
            respuesta = sentenciaSql.executeQuery(consulta);


            while (respuesta.next()) {

                proyecto = new Proyecto((respuesta.getInt("ID")),
                        (respuesta.getString("Nombre")),
                        (respuesta.getDate("Deadline")).toLocalDate(),
                        (respuesta.getString("Descripcion")),
                        (respuesta.getBoolean("Terminado")));
                lista.add(proyecto);
            }
        } finally {
            try {
                if (respuesta != null) {
                    respuesta.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (sentenciaSql != null) {
                    sentenciaSql.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // fuga de información
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    public static int ejecutarInsertPersonProyect(int idPersona, int idProyecto) throws SQLException, ClassNotFoundException {
        int respuestaBd;
        String cadena = "Insert into persona_proyecto (id_persona, id_proyecto) values (" + idPersona + "," + idProyecto + ")";
        respuestaBd = ejecutarSQL(cadena);

        return respuestaBd;
    }

    //

    public static int ejecutarDeletePersonProyect(int idPersona, int idProyecto) throws SQLException, ClassNotFoundException {
        int respuestaBd;
        String cadena = "DELETE FROM persona_proyecto WHERE id_persona = " + idPersona + " AND id_proyecto = " + idProyecto;
        respuestaBd = ejecutarSQL(cadena);

        return respuestaBd;
    }

    /**
     * Ejecuta una consulta que agrupa a las personas y cuenta cuantás nóminas tienen
     *
     * @return array de strings
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    //SELECT id_persona, COUNT(*) AS total FROM nomina GROUP BY id_persona
    public static ArrayList<String> ejecutarConsultaGroup() throws SQLException, ClassNotFoundException {
        ArrayList<String> lista = new ArrayList();
        String consulta = "SELECT id_persona, COUNT(*) AS total_nominas FROM nomina GROUP BY id_persona";
        String idDePersona = "Persona: ";
        String nominas = " nominas";
        String cadena;

        Connection conexion = null; // variable de referencia para conexion
        Statement sentenciaSql = null; // es el  que lleva la sentencia SQL a traves de la conexion
        ResultSet respuesta = null; // respuesta linea por linea (cursor), trae filas

        try {
            conexion = conectar(); // usa la variable conexion llamando a mi
            // metodo conectar que tiene la conexion real con la BBDD
            sentenciaSql = conexion.createStatement(); // usanndo esa conexion ...
            respuesta = sentenciaSql.executeQuery(consulta); // se guarda en un resultset?


            while (respuesta.next()) { // gracias al next avanza el cursor fila for fila
                // y regresa "false" cuando no hay más / asi termina el bucle.
                cadena = idDePersona + (respuesta.getString("id_persona")) + " - " +
                        (respuesta.getInt("total_nominas")) + nominas;

                lista.add(cadena);

            }
        } finally {

            try {
                if (respuesta != null) {
                    respuesta.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); //fuga de información y se salta a la vista
            }

            try {
                if (sentenciaSql != null) {
                    sentenciaSql.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;

    }

    public static ArrayList<String> ejecutarPersonaProyecto() throws SQLException, ClassNotFoundException {
        String consulta = "SELECT id_persona, id_proyecto FROM persona_proyecto";
        String persona = "Persona: ";
        String proyecto = " - Proyecto: ";
        String cadena;
        ArrayList<String> lista = new ArrayList();

        Connection conexion = null;
        Statement sentenciaSql = null;
        ResultSet respuesta = null;
        try {
            conexion = conectar();
            sentenciaSql = conexion.createStatement();
            respuesta = sentenciaSql.executeQuery(consulta);


            while (respuesta.next()) {
                cadena = persona + respuesta.getInt("id_persona") +
                        proyecto + respuesta.getInt("id_proyecto");
                lista.add(cadena);


            }

        } finally {
            try {
                if (respuesta != null) {
                    respuesta.close();
                }  //sjadk
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (sentenciaSql != null) {
                    sentenciaSql.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); //
            }

            try {
                if (conexion != null) { // committ de prueba
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }


} // llave final


/// ----------------------------------------------------------------------------------
////Fuga de información
// Es lo que me dice SonarQube. Le pregunté a la IA, y me dijo:
// "Fuga de información (information disclosure).
// En tu captura de Sonar, el aviso llevaba las etiquetas cwe y debug:
// CWE es un catálogo público de debilidades de software,
// y esta figura ahí como "código de depuración activo".
/**
 * Debilidad: algo en el código que podría aprovecharse. El printStackTrace() es esto.
 * Vulnerabilidad: una debilidad que alguien puede explotar de verdad para hacer daño.
 *
 * Esta debilidad por sí sola no deja entrar a nadie ni cambiar nada.
 * Lo que hace es darle pistas a quien quiera atacar.
 * Por eso Sonar la pone en seguridad, pero con gravedad baja.
 */
/// -----------------------------------------------------------------------------------




// "SELECT ID,NOMBRE,Apellidos,Direccion,cargo FROM persona";
// INSERT INTO persona(Nombre, Apellidos,Direccion, cargo) VALUES ('Ada', 'Lovelace', 'Calle Algoritmo 1, Madrid', 'Desarrolladora Senior')
