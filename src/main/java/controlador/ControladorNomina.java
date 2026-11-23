package controlador;

import modelo.Nomina;
import modelo.Persona;
import servicio.ServicioNomina;
import servicio.ServicioPersona;
import servicio.ServicioSql;
import vista.Consola;
import vista.Lector;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ControladorNomina {


    /////////////////////////////// METODO IMPORTANTE /////////////////////////////

    /**
     *
     * Llama a los metodos que van realizando el CRUD
     * de nómina dentro del mismo controlador
     * Hasta aquí se propagan muchas de las excepciones
     * y se gestionan :)
     */
    public static void gestionarNomina() {
        int opcion = 99;

        while (opcion != 0) {
            Consola.mostrarMenuNominas();

            try {
                opcion = Lector.pedirNumero();

                switch (opcion) {
                    case 1:
                        leerNominas();
                        break;
                    case 2:
                        registrarNomina();
                        break;
                    case 3:
                        modificarNomina();
                        break;
                    case 4:
                        deleteNomina(entregarIdNomina());
                        break;
                    case 0:
                        Consola.mostrarTextEspecifico("Salida de Menu Nomina");
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

    public static void registrarNomina() throws SQLException, ClassNotFoundException {
        Nomina nomina;
        BigDecimal importeFinal;
        int year;
        String mes;
        int personaId;

        Consola.mostrarTextEspecifico("Si quieres registrar una nomina " +
                "debe estar asociada al ID de una persona");

        Consola.mostrarPersonas(ServicioSql.leerPersonas());

        Consola.pedirDato("Id de la persona correspondiente");
        personaId = Lector.pedirNumSimple();
        Persona personaBuscada = ServicioPersona.buscarPersona(personaId);
        if (personaBuscada != null) {

            Consola.pedirDato("Importe final (ejemplo: 1500.50)");
            importeFinal = Lector.pedirValor();
            Consola.pedirDato("Anio");
            year = Lector.pedirNumSimple();
            Consola.pedirDato("mes");
            mes = Lector.pedirDato();
            nomina = new Nomina(importeFinal, year, mes, personaId);
            ServicioSql.insertarNomina(nomina);
            Consola.mostrarOperacionCorrecta();
        } else {
            Consola.mostrarInexistencia();
        }
    }

    public static void modificarNomina() throws SQLException, ClassNotFoundException {
        int idNomina = Lector.pedirNumeroParaObjeto();
        Nomina nomina = ServicioNomina.buscarNomina(idNomina);
        String dato;
        int datoYear;
        BigDecimal importeFinal;

        if (nomina != null) {
            int opcion = 99;

            while (opcion != 0) {
                Consola.mostrarAtributosNomina();
                opcion = Lector.pedirNumero();

                if (opcion >= 1 && opcion <= 3) {
                    Consola.mostrarTextEspecifico("Escribe el dato nuevo");

                    switch (opcion) {
                        case 1:
                            Consola.pedirDato("Importe final (ejemplo: 3555.55)");
                            importeFinal = Lector.pedirValor();
                            nomina.setImporteFinal(importeFinal);
                            break;
                        case 2:
                            datoYear = Lector.pedirNumSimple();
                            nomina.setYear(datoYear);
                            break;
                        case 3:
                            dato = Lector.pedirDato();
                            nomina.setMes(dato);
                            break;
                        default:
                            Consola.mostrarOpcionInvalida();
                            break;
                    }
                    guardarModificacionNomina(nomina);
                }
            }
            Consola.mostrarTextEspecifico("Saliendo...");

        } else {
            Consola.mostrarInexistencia();
        }
    }

    public static void guardarModificacionNomina(Nomina nomina) throws SQLException, ClassNotFoundException {
        int respuesta = ServicioSql.modificarNomina(nomina);

        if (respuesta > 0) {
            Consola.mostrarOperacionCorrecta();
        }
    }

    public static void leerNominas() throws SQLException, ClassNotFoundException {
        Consola.mostrarNominas(ServicioSql.leerNomina());
    }

    public static int entregarIdNomina() {
        int idNomina = Lector.pedirNumeroParaObjeto();

        return idNomina;
    }

    public static void deleteNomina(int idNomina) throws SQLException, ClassNotFoundException {
        Nomina nomina = ServicioNomina.buscarNomina(idNomina);
        int respuestaBd;

        if (nomina != null) {
            try {
                respuestaBd = ServicioSql.eliminarNomina(nomina);
                if (respuestaBd > 0) {
                    Consola.mostrarOperacionCorrecta();

                } else {
                    Consola.mostrarOperacionIncorrecta();
                }
            } catch (SQLException e) {
                Consola.mostrarTextEspecifico("No se ha podido eliminar la nomina");
                Consola.mostrarTextEspecifico(e.getMessage());
            }
        } else {
            Consola.mostrarInexistencia();
        }
    }
}
