package controlador;

import servicio.ServicioMaestro;
import servicio.ServicioSql;
import vista.Consola;
import vista.InterfaceVista;
import vista.Lector;

import java.sql.SQLException;
import java.util.ArrayList;


public class ControladorMaestro implements InterfaceControlador {

    // La vista, guardada en una variable del tipo de la INTERFAZ:
    // este controlador solo necesita saber que la vista tiene menu().
    private static InterfaceVista vista = new Consola();

    // menu() es lo que exige InterfaceControlador. Solo llama a mi menuPrincipal().
    @Override
    public void menu() {
        menuPrincipal();
    }

    /**
     * Tiene un switch donde
     * REUNE LAS LLAMADAS A LOS MENUS DE CONTROLADORES!!!
     */
    public static void menuPrincipal() {
        int opcion = 99;

        while (opcion != 0) {
            vista.menu(); // antes: Consola.mostrarMenuPrincipal();

            opcion = Lector.pedirNumero();

            switch (opcion) {
                case 1:
                    ControladorPersona.gestionarPersona();
                    break;
                case 2:
                    ControladorProyecto.gestionarProyecto();
                    break;
                case 3:
                    ControladorNomina.gestionarNomina();
                    break;
                case 4:
                    insertarPersonaProyecto();
                    break;
                case 5:
                    leerPersonaProyectos();
                    break;
                case 6:
                    deletePersonaProyecto();
                    break;
                case 7:
                    leerGroupByNominasPersona();
                    break;
                case 8:
                    ControladorFichero.menuEscrituraYlectura();
                    break;
                case 0:
                    Consola.mostrarTextEspecifico("Adiós");
                    break;
                default:
                    Consola.mostrarOpcionInvalida();
            }
        }

    }

    public static void insertarPersonaProyecto() {
        int idPersona;
        int idProyecto;
        boolean existenIds;
        int respuestaBd;
        Consola.mostrarTextEspecifico("Ingresa el id de una Persona ya registrada");
        idPersona = Lector.pedirNumSimple();

        Consola.mostrarTextEspecifico("Ingresa el id de un Proyecto ya registrado");
        idProyecto = Lector.pedirNumSimple();

        try {
            existenIds = ServicioMaestro.localizarIds(idPersona, idProyecto);
            if (existenIds) {
                respuestaBd = ServicioSql.insertarPersonaProyecto(idPersona, idProyecto);

                if (respuestaBd > 0) {
                    Consola.mostrarOperacionCorrecta();

                } else { // solo si no ocurriera ningún cambio
                    Consola.mostrarOperacionIncorrecta();
                }

            } else { //esto es si existenIds devuelve un false
                Consola.mostrarTextEspecifico("Persona o proyecto no encontrado");
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // es el códgio de error que surge cuando se intentan hacer duplicaciones
                Consola.mostrarTextEspecifico("Alguno de los datos ya están asociados");
            } else {
                Consola.mostrarTextEspecifico(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            Consola.mostrarTextEspecifico(e.getMessage());
        }
    }

    /**
     * Delete Persona_Proyecto (M:N)
     */
    public static void deletePersonaProyecto() {
        int idPersona;
        int idProyecto;
        boolean existenIds;
        int respuestaBd;
        Consola.mostrarTextEspecifico("Ingresa el id de una Persona ya registrada");
        idPersona = Lector.pedirNumSimple();

        Consola.mostrarTextEspecifico("Ingresa el id de un Proyecto ya registrado");
        idProyecto = Lector.pedirNumSimple();

        try {
            existenIds = ServicioMaestro.localizarIds(idPersona, idProyecto);

            if (existenIds) {
                respuestaBd = ServicioSql.deletePersonaProyecto(idPersona, idProyecto);

                if (respuestaBd > 0) {
                    Consola.mostrarOperacionCorrecta();

                } else { // si es cero, no hay cambios.
                    Consola.mostrarTextEspecifico("No hay un vinculo entre la persona y el proyecto proporcionados");
                }

            } else { //esto es si existenIds devuelve un false
                Consola.mostrarTextEspecifico("Persona o proyecto no encontrado");
            }

        } catch (SQLException e) {
            Consola.mostrarTextEspecifico(e.getMessage());
        } catch (ClassNotFoundException e) {
            Consola.mostrarTextEspecifico(e.getMessage());
        }
        /**
         * No les cree un menu aparte como con los otros porque
         * lo veía excesivo considerando que solo creé dos metodos para
         * esta relacion M:N, asi que lo dejé en el menú principal
         */
    }

    /**
     * Llama a la consola para podre imprimir gracias a ServicioSql que se comunica con bd
     * para poder mostrar por consola la lista de la tabla Persona_proyecto
     */
    public static void leerPersonaProyectos() {
        ArrayList<String> lista;
        try {
            lista = ServicioSql.leerPersonaProyectos();
            Consola.mostrarPersonasProyectos(lista);

        } catch (SQLException e) {
            Consola.mostrarTextEspecifico(e.getMessage());
        } catch (ClassNotFoundException e) {
            Consola.mostrarTextEspecifico(e.getMessage());
        }

    }

    //-------------------------- CONSULTA GROUP BY

    /**
     * Conecta con servicioSql para llama a servicio que llama metodos
     * de la base de datos que ejecutan consulta, a su vez,
     * el contrador muestra all esto al llamar a alguno de sus metodos
     * usando la consola que imprime
     */
    public static void leerGroupByNominasPersona() {
        ArrayList<String> lista;

        try {
            lista = ServicioSql.consultaGroupNominasPersona();
            Consola.mostrarNominasPorPersona(lista);

        } catch (SQLException e) {
            Consola.mostrarTextEspecifico(e.getMessage());

        } catch (ClassNotFoundException e) {
            Consola.mostrarTextEspecifico(e.getMessage());
        }
        // gestiono las excepciones aquí directamente
        // y no en un menu gestion como lo hacía en los otros controladores por lo general
        // queria hacerlo desde el menu peroo no me dio tiempo y me enredé un poco enetre tantas excepciones
    }

}

/**
 * Como llegue a esta clase?
 * En principio empecé por ControladorMaestro //######
 * Ahi empecé con la clase Persona
 * Tenia un menu principal donde llamaba a todos
 * Luego me di cuenta al avanzar con personas en este controlador
 * Que era demasiado largo y todo se mezclaba
 * asi que le cambié el nombre a ControladorPersona (Donde este metodo estaba antes)
 * Creé los otros controladores para las otras clases
 * Y metí el menu principal en esté controlador donde llama a cada controlador
 * y a la gestion de la clase
 * <p>
 * No sé si es lo más correcto ni si es la mejor practica
 * pero lo entiendo mejor por ahora estando separado
 * y no mezclando los tipos
 * <p>
 * <p>
 * Luego pense en cambiar todo lo relacionado a sql a controlador sql y el resto dejarlo aqui en controlador maestro
 *
 */

// hay un controlador que se comunica con la base de datos, es el que
// Realice t-odo lo necesario de Base de datos en fichero, para pasarlo ç
// al main, asi poder hacer inserts updates y otros
// eso es lo que yo entiendo

