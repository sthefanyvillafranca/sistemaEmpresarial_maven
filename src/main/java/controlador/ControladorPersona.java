package controlador;

import modelo.Persona;
import servicio.ServicioPersona;
import servicio.ServicioSql;
import vista.Consola;
import vista.Lector;

import java.sql.SQLException;


public class ControladorPersona {


    /////////////////////////////// METODO IMPORTANTE /////////////////////////////
    /**
     * Gestionar Persona
     * (La primera clase que creé en un controlador)
     * Reune the whole CRUD (los metodos) de Persona
     * - Llamadas a los metodos dentro del mismo controlador
     * - Estos metodos a su vez mezclan el uso de la consola y el servicioSQL y ServicioPersona
     * Aqui finalmente gestiono las excepciones que he propagado hasta aquí:
     * SQLException
     * ClassNotFoundException
     */
    public static void gestionarPersona() {
        int opcion = 99;

        while (opcion != 0) {
            Consola.mostrarMenuPersonas();

            try {
                opcion = Lector.pedirNumero();

                switch (opcion) {
                    case 1:
                        leerPersonas();
                        break;
                    case 2:
                        registrarPersona();
                        break;
                    case 3:
                        modificarPersona();
                        break;
                    case 4:
                        deletePersona(entregarIdPersona());
                        break;
                    case 0:
                        Consola.mostrarTextEspecifico("Salida de Menu Persona");
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


    public static void registrarPersona() throws SQLException, ClassNotFoundException {
        Persona persona;
        String nombre;
        String apellidos;
        String direccion;
        String cargo;

        Consola.pedirDato("Nombre");
        nombre = Lector.pedirDato();
        Consola.pedirDato("apellidos");
        apellidos = Lector.pedirDato();
        Consola.pedirDato("direccion");
        direccion = Lector.pedirDato();
        Consola.pedirDato("cargo");
        cargo = Lector.pedirDato();

        persona = new Persona(nombre, apellidos, direccion, cargo);
        ServicioSql.insertarPersona(persona);
        Consola.mostrarOperacionCorrecta();


    }


    public static void modificarPersona() throws SQLException, ClassNotFoundException {
        int idPersona = Lector.pedirNumeroParaObjeto();
        Persona persona = ServicioPersona.buscarPersona(idPersona);

        if (persona != null) {
            int opcion = 99;

            while (opcion != 0) {
                Consola.mostrarAtributosPersona();
                opcion = Lector.pedirNumero();

                if (opcion >= 1 && opcion <= 4) {
                    Consola.pedirDato("Escribe el nuevo dato a cambiar: ");
                    String dato = Lector.pedirDato();

                    switch (opcion) {
                        case 1:
                            persona.setNombre(dato);
                            break;
                        case 2:
                            persona.setApellidos(dato);
                            break;
                        case 3:
                            persona.setDireccion(dato);
                            break;
                        case 4:
                            persona.setCargo(dato);
                            break;
                        default:
                            Consola.mostrarOpcionInvalida();
                            break;
                    }
                    guardarModificacionPersona(persona);
                }
            }
            Consola.mostrarTextEspecifico("Saliendo...");

        } else { // si el resultado de buscar persona es null directament no hace ese código
            // buscarpersona puede regresar un null :)
            Consola.mostrarInexistencia();
        }

    }

    public static void guardarModificacionPersona(Persona persona) throws SQLException, ClassNotFoundException {
        int respuesta = ServicioSql.modificarPersona(persona);

        if (respuesta > 0) {
            Consola.mostrarOperacionCorrecta();
        }

    }

    public static void leerPersonas() throws SQLException, ClassNotFoundException {
        Consola.mostrarPersonas(ServicioSql.leerPersonas());

    }

    public static int entregarIdPersona() {
        int idPersona = Lector.pedirNumeroParaObjeto();

        return idPersona;
    }


    /**
     *
     * @param idPersona
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void deletePersona(int idPersona) throws SQLException, ClassNotFoundException {
        Persona persona = ServicioPersona.buscarPersona(idPersona);
        int respuestaBd;

        if (persona != null) {
            try {
                respuestaBd = ServicioSql.eliminarPersona(persona);
                if (respuestaBd > 0) {
                    Consola.mostrarOperacionCorrecta();

                } else { // if respuestaBd es 0 porque no ocurrió ningun cambio
                    Consola.mostrarOperacionIncorrecta();
                }
            } catch (SQLException e) {

                if (e.getErrorCode() == 1451) {
                    Consola.mostrarOperacionIncorrecta();
                    Consola.mostrarTextEspecifico("Hay registros asociados");
                } else {
                    Consola.mostrarTextEspecifico("No se ha podido eliminar a la persona");
                    Consola.mostrarTextEspecifico(e.getMessage());
                }
            }
        } else {
            Consola.mostrarInexistencia(); //este dice "El dato que busca no existe"
        }
    } // no puedo eliminar el throws porque ServicioPersona.buscarPersona arroja excepción también


    //llave final
}


/**
 * TODO:
 * este codigo ha visto mi alma pero necesita refactorización RECORDAR al final
 */
