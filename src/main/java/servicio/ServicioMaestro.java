package servicio;

import modelo.Nomina;
import modelo.Persona;
import modelo.Proyecto;
import repositorio.Fichero;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServicioMaestro {

    /**
     * Sirve para comprobar que los ids pertenecen a una persona y a un proyecto existentes a la vez (&&)
     *
     * @param idPersona
     * @param idProyecto
     * @return boolean
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static boolean localizarIds(int idPersona, int idProyecto) throws SQLException, ClassNotFoundException {
        boolean respuesta = false;
        Persona persona = ServicioPersona.buscarPersona(idPersona);
        Proyecto proyecto = ServicioProyecto.buscarProyecto(idProyecto);

        if (proyecto != null && persona != null) {
            respuesta = true;
        }
        return respuesta;
    }

    ///////////////////////////////////////// SERVICIO - COMUNICA CON FICHERO !!! ////////////////

    /**
     * Razonamiento inicial...
     * en mi servicio de mi anterior proyecto. tenia una clase listalibros
     * que pasaba por una variable / parametro item y lo transformaba con un tostring,
     * en este proyecto yo no tengo una clase llamada asi, directamente tengo
     * la lista, pero sale de la base de datos que yo ahi mismo tengo como lista
     * de objetos. entonces... un metodo en el servicio que se conecte
     * con el repositorio / baseDeDatos , que agarre esos objetos y los
     * vuelva Strings... Escribir fichero, que copie solo la estructura,
     * recibe un array de strings.
     */


    public static void escribirArchivoProyecto() throws SQLException, ClassNotFoundException, IOException {
        Fichero fichero = new Fichero("proyectos.txt");
        ArrayList<Proyecto> listaProyecto;
        listaProyecto = ServicioSql.leerProyecto();

        ArrayList<String> lista = new ArrayList<>();

        for (Proyecto proyecto : listaProyecto) {
            lista.add(proyecto.toFichero());
        }

        fichero.escribirFicheroExportar(lista);
    }

    public static void escribirArchivoNomina() throws SQLException, ClassNotFoundException, IOException {
        Fichero fichero = new Fichero("nominas.txt");

        ArrayList<Nomina> listaNomina;
        listaNomina = ServicioSql.leerNomina();

        ArrayList<String> lista = new ArrayList<>();

        for (Nomina nomina : listaNomina) {
            lista.add(nomina.toFichero());
        }
        fichero.escribirFicheroExportar(lista);
    }

    /**
     * Un metodo que toma la lista de Objetos hecha en BaseDeDatos con las filas de las tablas
     * Este sirve para escribir en un texto pero para eso hay que transformarlo
     * a String. // Recorre una lista de Personas, y cada una la va volviendo un String
     * Que se guarda en el ArrayList<String> del metodo
     */
    public static void escribirArchivoPersona() throws SQLException, ClassNotFoundException, IOException {
        Fichero fichero = new Fichero("personas.txt");
        ArrayList<String> lista = new ArrayList<>();
        ArrayList<Persona> listaPersona;


        listaPersona = ServicioSql.leerPersonas(); // guarda en la variable la lista de Personas.


        for (Persona persona : listaPersona) {
            lista.add(persona.toFichero()); // va agregando a cada persona de la lista real de personas
            // a una lista de strings usando un metodo toFichero con un formato comun
        }
        fichero.escribirFicheroExportar(lista); //llama al metodo de fichero
        // y le pasa esa lista de String recien creada
    }


    public static ArrayList<Persona> leerArchivoPersona() throws SQLException, ClassNotFoundException, IOException {
        ArrayList<String> lista;
        Fichero fichero = new Fichero("personas.txt");
        ArrayList<Persona> listaPersona = new ArrayList<>();

        lista = fichero.leerFichero();

        for (String elemento : lista) {
            listaPersona.add(new Persona(elemento));
        }

        return listaPersona;
    }

    public static void leerArchivoPersonaInsert() throws SQLException, IOException, ClassNotFoundException {
        ArrayList<Persona> lista;
        lista = ServicioMaestro.leerArchivoPersona();
        Persona personaBuscada;

        for (Persona persona : lista) {
            personaBuscada = ServicioPersona.buscarPersonaNombreApellido(persona);
            if (personaBuscada == null) {
                ServicioSql.insertarPersona(persona);
            }
        }

    }

    public static ArrayList<Proyecto> leerArchivoProyecto() throws SQLException, ClassNotFoundException, IOException {
        ArrayList<String> lista;
        Fichero fichero = new Fichero("proyectos.txt");
        ArrayList<Proyecto> listaProyecto = new ArrayList<>();

        lista = fichero.leerFichero();

        for (String elemento : lista) {
            listaProyecto.add(new Proyecto(elemento));

        }
        return listaProyecto;
    }

    public static void leerArchivoProyectoInsert() throws SQLException, IOException, ClassNotFoundException {
        ArrayList<Proyecto> lista;
        lista = ServicioMaestro.leerArchivoProyecto();
        Proyecto proyectoBuscado;

        for (Proyecto proyecto : lista) {
            proyectoBuscado = ServicioProyecto.buscarProyectoPorNombre(proyecto);
            if (proyectoBuscado == null) {
                ServicioSql.insertarProyecto(proyecto);
            }
        }

    }

    public static ArrayList<Nomina> leerArchivoNomina() throws SQLException, ClassNotFoundException, IOException {
        ArrayList<String> lista;
        Fichero fichero = new Fichero("nominas.txt");
        ArrayList<Nomina> listaNomina = new ArrayList<>();

        lista = fichero.leerFichero();

        for (String elemento : lista) {
            listaNomina.add(new Nomina(elemento));

        }
        return listaNomina;
    }

    public static void leerArchivoNominaInsert() throws SQLException, IOException, ClassNotFoundException {
        ArrayList<Nomina> lista;
        lista = ServicioMaestro.leerArchivoNomina();
        Nomina nominaBuscada;

        for (Nomina nomina : lista) {
            nominaBuscada = ServicioNomina.buscarNominaPorPersonaYearMes(nomina);
            if (nominaBuscada == null) {
                ServicioSql.insertarNomina(nomina);
            }
        }

    }

//llave final
}



