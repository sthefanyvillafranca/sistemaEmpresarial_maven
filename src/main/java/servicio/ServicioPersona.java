package servicio;

import modelo.Persona;
import repositorio.BaseDeDatos;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServicioPersona {

    /**
     * BUSCA a una PERSONA por ID
     *
     * @param idPersona
     * @return Persona o null
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Persona buscarPersona(int idPersona) throws SQLException, ClassNotFoundException {

        ArrayList<Persona> lista = ServicioSql.leerPersonas(); //llamo a servicioSql
        Persona personaBuscada = null;

        for (Persona persona : lista) {
            if (persona.getId() == idPersona) {
                personaBuscada = persona;
            }
        }

        return personaBuscada;

    } //


    public static Persona buscarPersonaNombreApellido(Persona personaBuscada) throws SQLException, ClassNotFoundException {

        ArrayList<Persona> lista = ServicioSql.leerPersonas(); //llamo a servicioSql
        String nombre = personaBuscada.getNombre().trim(); // trim para remover espacios just in case
        String apellido = personaBuscada.getApellidos().trim();
        Persona personaResultado = null;

        for (Persona elemento : lista) { // equalsignorecase ignora si entra como mayus o minus
            if (elemento.getNombre().equalsIgnoreCase(nombre) && elemento.getApellidos().equalsIgnoreCase(apellido)) {
                personaResultado = elemento;
            }
        }

        return personaResultado;
    }//


}
