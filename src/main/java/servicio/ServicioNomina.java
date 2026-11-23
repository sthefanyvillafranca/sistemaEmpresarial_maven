package servicio;

import modelo.Nomina;
import modelo.Persona;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServicioNomina {

    /**
     *
     * @param idNomina
     * @return Nomina o null
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Nomina buscarNomina(int idNomina) throws SQLException, ClassNotFoundException {

        ArrayList<Nomina> lista = ServicioSql.leerNomina();
        Nomina nominaBuscada = null;

        for (Nomina nomina : lista) {
            if (nomina.getId() == idNomina) {
                nominaBuscada = nomina;
            }
        }

        return nominaBuscada;

    }

    public static Nomina buscarNominaPorPersonaYearMes(Nomina nominaBuscada) throws SQLException, ClassNotFoundException {

        ArrayList<Nomina> lista = ServicioSql.leerNomina(); //llamo a servicioSql
        String mes = nominaBuscada.getMes().trim(); // trim para remover espacios just in case
        int year = nominaBuscada.getYear();
        int personaId = nominaBuscada.getPersonaId();
        Nomina nominaResultado = null;

        for (Nomina elemento : lista) { // equalsignorecase ignora si entra como mayus o minus
            if (elemento.getPersonaId() == personaId && elemento.getYear() == year && elemento.getMes().equalsIgnoreCase(mes)) {
                nominaResultado = elemento;                                         // obtienes el mes del elemento
                // al comparar el equals ingore case para que
                // acepte diferentes M o m
            }
        }

        return nominaResultado; //busqueda especifica para que no hubiera duplicaciones en la importacion
    }//
}
