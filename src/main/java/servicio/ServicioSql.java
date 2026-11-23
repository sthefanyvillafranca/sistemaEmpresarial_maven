package servicio;

import modelo.Nomina;
import modelo.Persona;
import modelo.Proyecto;
import repositorio.BaseDeDatos;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServicioSql {

    /// /////////////////////////// PERSONA ////////////////////////////////

    public static ArrayList<Persona> leerPersonas() throws SQLException, ClassNotFoundException {
        // entra un objeto
        //sale una sql
        ArrayList<Persona> lista;

        lista = BaseDeDatos.ejecutarConsultaPersona();

        return lista;
    }

    public static int eliminarPersona(Persona elemento) throws SQLException, ClassNotFoundException {
        int respuestaBd;
        respuestaBd = BaseDeDatos.ejecutarSQL(elemento.toDeleteSql());

        return respuestaBd;
    }

    public static int modificarPersona(Persona elemento) throws SQLException, ClassNotFoundException {
        int resultado;
        resultado = BaseDeDatos.ejecutarSQL(elemento.toUpdateSql());
        return resultado;
    }

    public static void insertarPersona(Persona elemento) throws SQLException, ClassNotFoundException {
        BaseDeDatos.ejecutarSQL(elemento.toInsertSql());
    }


    /// /////////////////////////////////NOMINA //////////////////////////////////////

    public static ArrayList<Nomina> leerNomina() throws SQLException, ClassNotFoundException {
        ArrayList<Nomina> lista;
        lista = BaseDeDatos.ejecutarConsultaNomina();
        return lista;
    }

    public static int eliminarNomina(Nomina elemento) throws SQLException, ClassNotFoundException {
        int respuestaBd;
        respuestaBd = BaseDeDatos.ejecutarSQL(elemento.toDeleteSql());

        return respuestaBd;
    }

    public static int modificarNomina(Nomina elemento) throws SQLException, ClassNotFoundException {
        int resultado;
        resultado = BaseDeDatos.ejecutarSQL(elemento.toUpdateSql());
        return resultado;
    }

    public static void insertarNomina(Nomina elemento) throws SQLException, ClassNotFoundException {
        BaseDeDatos.ejecutarSQL(elemento.toInsertSql());
    }

    /// /////////////////////////////// PROYECTO ////////////////////////////////////

    public static ArrayList<Proyecto> leerProyecto() throws SQLException, ClassNotFoundException {
        ArrayList<Proyecto> lista;
        lista = BaseDeDatos.ejecutarConsultaProyecto();
        return lista;
    }

    public static int eliminarProyecto(Proyecto elemento) throws SQLException, ClassNotFoundException {
        int respuestaBd;
        respuestaBd = BaseDeDatos.ejecutarSQL(elemento.toDeleteSql());

        return respuestaBd;
    }

    public static int modificarProyecto(Proyecto elemento) throws SQLException, ClassNotFoundException {
        int resultado;
        resultado = BaseDeDatos.ejecutarSQL(elemento.toUpdateSql());
        return resultado;
    }

    public static void insertarProyecto(Proyecto elemento) throws SQLException, ClassNotFoundException {
        BaseDeDatos.ejecutarSQL(elemento.toInsertSql());
    }

    /// //////////////////////////// PERSONA_PROYECTO M:N /////////////////////////

    public static int insertarPersonaProyecto(int idPersona, int idProyecto) throws SQLException, ClassNotFoundException {
        int respuestaBd;

        respuestaBd = BaseDeDatos.ejecutarInsertPersonProyect(idPersona, idProyecto);

        return respuestaBd;
    }


    public static int deletePersonaProyecto(int idPersona, int idProyecto) throws SQLException, ClassNotFoundException {
        int respuestaBd;

        respuestaBd = BaseDeDatos.ejecutarDeletePersonProyect(idPersona, idProyecto);

        return respuestaBd;
    }

    public static ArrayList<String> leerPersonaProyectos() throws SQLException, ClassNotFoundException {
        ArrayList<String> lista;
        lista = BaseDeDatos.ejecutarPersonaProyecto();

        return lista;

    }

    /////////////////////////////////////////////// CONSULTA GROUP BY /////////////////////////////////////
    //SELECT id_persona, COUNT(*) FROM nomina GROUP BY id_persona;

    /**
     * Razonamiento previo:
     * Como nómina y persona tienen una relación 1:n ...
     * Es decir... una persona tiene varias nóminas, pero una nòmina
     * pertenece a una sola persona a la vez...
     * entonces, podria haber en los registros algo como el id
     * de la misma persona varias veces, y claro, el id de la propia
     * nómina siendo diferente es decir, otra nómina
     */
    public static ArrayList<String> consultaGroupNominasPersona() throws SQLException, ClassNotFoundException {
        ArrayList<String> lista;
        lista = BaseDeDatos.ejecutarConsultaGroup();

        return lista;
    }


}

//INSERT INTO `persona` (`Nombre`, `Apellidos`, `Direccion`, `cargo`)
//VALUES ('Ada', 'Lovelace', 'Calle Algoritmo 1, Madrid', 'Desarrolladora Senior');

/**
 *
 *
 *
 */