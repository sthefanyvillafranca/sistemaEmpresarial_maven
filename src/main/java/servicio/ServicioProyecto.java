package servicio;

import modelo.Persona;
import modelo.Proyecto;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServicioProyecto {

    /**
     *
     * @param idProyecto
     * @return Proyecto o null
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Proyecto buscarProyecto(int idProyecto) throws SQLException, ClassNotFoundException {

        ArrayList<Proyecto> lista = ServicioSql.leerProyecto();
        Proyecto proyectoBuscado = null;

        for (Proyecto proyecto : lista) {
            if (proyecto.getId() == idProyecto) {
                proyectoBuscado = proyecto;
            }
        }

        return proyectoBuscado;

    }

    public static Proyecto buscarProyectoPorNombre(Proyecto proyectoBuscado) throws SQLException, ClassNotFoundException {

        ArrayList<Proyecto> lista = ServicioSql.leerProyecto();
        String nombre = proyectoBuscado.getNombre().trim();
        Proyecto proyectoResultado = null;

        for (Proyecto elemento : lista) {
            if (elemento.getNombre().equalsIgnoreCase(nombre)) {
                proyectoResultado = elemento;
            }
        }

        return proyectoResultado; //busquedas especificas para que no hubiera duplicaciones en la importacion
    }
}
