package controlador;

import modelo.Proyecto;
import servicio.ServicioProyecto;
import servicio.ServicioSql;
import vista.Consola;
import vista.Lector;

import java.sql.SQLException;
import java.time.LocalDate;

public class ControladorProyecto {

    public static void gestionarProyecto() {
        int opcion = 99;

        while (opcion != 0) {
            Consola.mostrarMenuProyectos();

            try {
                opcion = Lector.pedirNumero();

                switch (opcion) {
                    case 1:
                        leerProyectos();
                        break;
                    case 2:
                        registrarProyecto();
                        break;
                    case 3:
                        modificarProyecto();
                        break;
                    case 4:
                        deleteProyecto(entregarIdProyecto());
                        break;
                    case 0:
                        Consola.mostrarTextEspecifico("Salida de Menu Proyecto");
                        break;
                    default:
                        Consola.mostrarOpcionInvalida();
                }
            } catch (SQLException e) {
                Consola.mostrarTextEspecifico(e.getMessage());
            } catch (ClassNotFoundException e) {
                Consola.mostrarTextEspecifico(e.getMessage());
            } catch (Exception e) {
                Consola.mostrarTextEspecifico(e.getMessage());
            }
        }
    }

    public static void registrarProyecto() throws SQLException, ClassNotFoundException {
        Proyecto proyecto;
        String nombre;
        LocalDate deadLine; //A date without a time-zone in the ISO-8601 calendar system, such as 2007-12-03.
        String descripcion;
        boolean terminado;
        int opcionTerminado; // para el boolean

        Consola.pedirDato("Nombre");
        nombre = Lector.pedirDato();
        Consola.pedirDato("Deadline del proyecto");
        deadLine = Lector.pedirDate();
        Consola.pedirDato("Descripcion del proyecto");
        descripcion = Lector.pedirDato();

        Consola.mostrarEstadoProyecto();
        opcionTerminado = Lector.pedirNumSimple(); //el numero que sirve para el boolean
        terminado = (opcionTerminado == 1); // si opcion es igual a 1 true, si no false

        proyecto = new Proyecto(nombre, deadLine, descripcion, terminado);
        ServicioSql.insertarProyecto(proyecto);
        Consola.mostrarOperacionCorrecta();

    }

    public static void modificarProyecto() throws SQLException, ClassNotFoundException {
        int idProyecto = Lector.pedirNumeroParaObjeto();
        Proyecto proyecto = ServicioProyecto.buscarProyecto(idProyecto);
        LocalDate deadline;
        String descripcion;
        String nombre;
        boolean terminado;
        int opcionTerminado;

        if (proyecto != null) {
            int opcion = 99;

            while (opcion != 0) {
                Consola.mostrarAtributosProyecto();
                opcion = Lector.pedirNumero();

                if (opcion >= 1 && opcion <= 4) {
                    Consola.pedirDato("Escribe el nuevo dato a cambiar: ");

                    switch (opcion) {
                        case 1:
                            Consola.pedirDato("Ingresa el nombre del proyecto: ");
                            nombre = Lector.pedirDato();
                            proyecto.setNombre(nombre);
                            break;
                        case 2:
                            Consola.pedirDato("Deadline del proyecto");
                            deadline = Lector.pedirDate();
                            proyecto.setDeadLine(deadline);
                            break;
                        case 3:
                            Consola.pedirDato("Descripcion del proyecto");
                            descripcion = Lector.pedirDato();
                            proyecto.setDescripcion(descripcion);
                            break;
                        case 4:
                            Consola.mostrarEstadoProyecto();
                            opcionTerminado = Lector.pedirNumSimple();
                            terminado = (opcionTerminado == 1);
                            proyecto.setTerminado(terminado);
                            break;
                        default:
                            Consola.mostrarOpcionInvalida();
                    }
                    guardarModificacionProyecto(proyecto);
                }
            }
            Consola.mostrarTextEspecifico("Saliendo...");

        } else {
            Consola.mostrarInexistencia();
        }
    }

    public static void guardarModificacionProyecto(Proyecto proyecto) throws SQLException, ClassNotFoundException {
        int respuesta = ServicioSql.modificarProyecto(proyecto);

        if (respuesta > 0) {
            Consola.mostrarOperacionCorrecta();
        }
    }

    public static void leerProyectos() throws SQLException, ClassNotFoundException {
        Consola.mostrarProyectos(ServicioSql.leerProyecto());
    }

    /**
     * public static void leerUnProyecto(int idProyecto)  throws SQLException, ClassNotFoundException {
     * Consola.mostrarProyecto(ServicioProyecto(idProyecto));
     * }
     **/ // POSIBLE MEJORA GENERAL EN TODaS LAS CLASES
    public static int entregarIdProyecto() {
        int idProyecto = Lector.pedirNumeroParaObjeto();

        return idProyecto;
    }

    public static void deleteProyecto(int idProyecto) throws SQLException, ClassNotFoundException {
        Proyecto proyecto = ServicioProyecto.buscarProyecto(idProyecto);
        int respuestaBd;

        if (proyecto != null) {
            try {
                respuestaBd = ServicioSql.eliminarProyecto(proyecto);
                if (respuestaBd > 0) {
                    Consola.mostrarOperacionCorrecta();

                } else {
                    Consola.mostrarOperacionIncorrecta();
                }
            } catch (SQLException e) {

                if (e.getErrorCode() == 1451) {
                    Consola.mostrarOperacionIncorrecta();
                    Consola.mostrarTextEspecifico("Hay registros asociados");
                } else {
                    Consola.mostrarTextEspecifico("No se ha podido eliminar el proyecto");
                    Consola.mostrarTextEspecifico(e.getMessage());
                }
            }
        } else {
            Consola.mostrarInexistencia();
        }
    }
}
