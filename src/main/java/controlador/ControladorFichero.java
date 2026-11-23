package controlador;

import modelo.Nomina;
import modelo.Persona;
import modelo.Proyecto;
import servicio.ServicioMaestro;
import servicio.ServicioSql;
import vista.Consola;
import vista.Lector;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorFichero {


    /// /////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////
    /// ESCRITURA DE ARCHIVOS
    /// (FICHERO -> SERVICIO (TOMA LA LISTA DE BD) -> CONTROLADOR)
    ///

    public static void menuEscrituraYlectura() { // todo: progragar excepciones de fichero hasta aqui
        int opcion = 99;

        while (opcion != 0) {
            Consola.mostrarMenuFicheros();
            ;

            try {
                opcion = Lector.pedirNumero();

                switch (opcion) {
                    case 1:
                        escribirArchivoPersona();
                        break;
                    case 2:
                        escribirArchivoProyecto();
                        break;
                    case 3:
                        escribirArchivoNomina();
                        break;
                    case 4:
                        leerArchivoPersona();
                        break;
                    case 5:
                        importarArchivoPersona();
                        break;
                    case 6:
                        leerArchivoProyecto();
                        break;
                    case 7:
                        importarArchivoProyecto();
                        break;
                    case 8:
                        leerArchivoNomina();
                        break;
                    case 9:
                        importarArchivoNomina();
                        break;
                    case 0:
                        Consola.mostrarTextEspecifico("Salida de Menu de Importar y Exportar");
                        break;
                    default:
                        Consola.mostrarOpcionInvalida();
                }
            } catch (IOException io) {
                Consola.mostrarTextEspecifico(io.getMessage());
            } catch (SQLException e) {
                Consola.mostrarTextEspecifico(e.getMessage());
            } catch (ClassNotFoundException e) {
                Consola.mostrarTextEspecifico(e.getMessage());
            } catch (Exception e) {
                Consola.mostrarTextEspecifico(e.getMessage());
            }
        }
    }


    public static void escribirArchivoNomina() throws IOException, SQLException, ClassNotFoundException {

        ServicioMaestro.escribirArchivoNomina();
        Consola.mostrarTextEspecifico("Exportacion correcta");

    }

    public static void escribirArchivoProyecto() throws IOException, SQLException, ClassNotFoundException {

        ServicioMaestro.escribirArchivoProyecto();
        Consola.mostrarTextEspecifico("Exportación correcta");

    }


    public static void escribirArchivoPersona() throws IOException, SQLException, ClassNotFoundException {

        ServicioMaestro.escribirArchivoPersona();
        Consola.mostrarTextEspecifico("Exportación correcta");


    }

    // ///////////////////////////////////////// PERSONA - LEER

    public static void leerArchivoPersona() throws IOException, SQLException, ClassNotFoundException {

        ArrayList<Persona> lista = ServicioMaestro.leerArchivoPersona();
        Consola.mostrarPersonas(lista);

    }

    public static void importarArchivoPersona() throws IOException, SQLException, ClassNotFoundException {
        ServicioMaestro.leerArchivoPersonaInsert();
        Consola.mostrarTextEspecifico("Importación a la BBDD terminada");
    }

    /// ////////////////////////////// Nomima

    public static void leerArchivoNomina() throws IOException, SQLException, ClassNotFoundException {

        ArrayList<Nomina> lista = ServicioMaestro.leerArchivoNomina();
        Consola.mostrarNominas(lista);

    }

    public static void importarArchivoNomina() throws IOException, SQLException, ClassNotFoundException {
        ServicioMaestro.leerArchivoNominaInsert();
        Consola.mostrarTextEspecifico("Importación a la BBDD terminada");
    }


    /// /////////////////////// Proyecto

    public static void leerArchivoProyecto() throws IOException, SQLException, ClassNotFoundException {

        ArrayList<Proyecto> lista = ServicioMaestro.leerArchivoProyecto();
        Consola.mostrarProyectos(lista);

    }

    public static void importarArchivoProyecto() throws IOException, SQLException, ClassNotFoundException {
        ServicioMaestro.leerArchivoProyectoInsert();
        Consola.mostrarTextEspecifico("Importación a la BBDD terminada");
    }


}

