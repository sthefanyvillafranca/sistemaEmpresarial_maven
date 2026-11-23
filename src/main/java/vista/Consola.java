package vista;

import modelo.Nomina;
import modelo.Persona;
import modelo.Proyecto;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class Consola implements InterfaceVista {

    // menu() es lo que exige InterfaceVista. Solo llama a mi mostrarMenuPrincipal().
    @Override
    public void menu() {
        mostrarMenuPrincipal();
    }

    // PRESENTACIÓN DEL MENÚ PRINCIPAL HECHO CON IA
    public static void mostrarMenuPrincipal() {

        System.out.println();
        System.out.println("==================================================");
        System.out.println("           SISTEMA EMPRESARIAL                    ");
        System.out.println("==================================================");
        System.out.println("              MENÚ PRINCIPAL                      ");
        System.out.println("--------------------------------------------------");
        System.out.println("  [1] Gestión de personal");
        System.out.println("  [2] Gestión de proyectos");
        System.out.println("  [3] Gestión de nóminas");
        System.out.println("  [4] Asignar persona a proyecto");
        System.out.println("  [5] Mostrar personas asignadas a proyectos");
        System.out.println("  [6] Eliminar vinculo de una persona a proyecto");
        System.out.println("  [7] Mostrar cuántas nónimas tiene cada persona");
        System.out.println("  [8] Abrir menú de exportación e importación");
        System.out.println("--------------------------------------------------");
        System.out.println("  [0] Salir del sistema");
        System.out.println("==================================================");

    }

    public static void mostrarMenuPersonas() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("            GESTIÓN DE PERSONAL           ");
        System.out.println("==========================================");
        System.out.println("  [1] Consultar empleados");
        System.out.println("  [2] Registrar empleado");
        System.out.println("  [3] Modificar empleado");
        System.out.println("  [4] Eliminar empleado");
        System.out.println("------------------------------------------");
        System.out.println("  [0] Volver al menú principal");
        System.out.println("==========================================");
    }

    public static void mostrarPersonas(ArrayList<Persona> lista) {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("           LISTADO DE EMPLEADOS           ");
        System.out.println("==========================================");

        System.out.println("Total de empleados: " + lista.size());
        System.out.println();

        for (Persona persona : lista) {

            System.out.println("------------------------------------------");

            System.out.printf(" ID:        %d%n", persona.getId());
            System.out.printf(" Nombre:    %s %s%n",
                    persona.getNombre(), persona.getApellidos());
            System.out.printf(" Cargo:     %s%n", persona.getCargo());
            System.out.printf(" Dirección: %s%n", persona.getDireccion());

        }

        System.out.println("------------------------------------------");
    }

    /**
     * Metodo que usa println para preguntar
     * "Por favor, ingresar:" + dato
     *
     * @param dato
     */
    public static void pedirDato(String dato) {
        System.out.println("Por favor, ingresar: " + dato);
    }


    public static void mostrarAtributosPersona() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("            Datos de un empleado          ");
        System.out.println("==========================================");
        System.out.println("  [1] Nombre");
        System.out.println("  [2] Apellidos");
        System.out.println("  [3] Direccion");
        System.out.println("  [4] Cargo");
        System.out.println("  [0] Cancelar");
        System.out.println("------------------------------------------");

    }

    public static void mostrarNominas(ArrayList<Nomina> lista) {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("            LISTADO DE NÓMINAS            ");
        System.out.println("==========================================");

        System.out.println("Total de nóminas: " + lista.size());
        System.out.println();

        for (Nomina nomina : lista) {

            System.out.println("------------------------------------------");

            System.out.printf(" ID:         %d%n", nomina.getId());
            System.out.printf(" Importe:    %s%n", nomina.getImporteFinal());
            System.out.printf(" Año:        %d%n", nomina.getYear());
            System.out.printf(" Mes:        %s%n", nomina.getMes());
            System.out.printf(" ID persona: %d%n", nomina.getPersonaId());

        }

        System.out.println("------------------------------------------");
    }

    public static void mostrarAtributosNomina() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("             Datos de una nómina          ");
        System.out.println("==========================================");
        System.out.println("  [1] Importe");
        System.out.println("  [2] Año");
        System.out.println("  [3] Mes");
        System.out.println("  [4] ID de la persona");
        System.out.println("  [0] Cancelar");
        System.out.println("------------------------------------------");

    }

    public static void mostrarProyectos(ArrayList<Proyecto> lista) {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("           LISTADO DE PROYECTOS           ");
        System.out.println("==========================================");

        System.out.println("Total de proyectos: " + lista.size());
        System.out.println();

        for (Proyecto proyecto : lista) {

            System.out.println("------------------------------------------");

            System.out.printf(" ID:          %d%n", proyecto.getId());
            System.out.printf(" Nombre:      %s%n", proyecto.getNombre());
            System.out.printf(" Deadline:    %s%n", proyecto.getDeadLine());
            System.out.printf(" Descripción: %s%n", proyecto.getDescripcion());
            System.out.printf(" Estado:      %s%n", proyecto.isTerminado() ? "Terminado" : "Pendiente");

        }

        System.out.println("------------------------------------------");
    }

    public static void mostrarMenuFicheros() {

        System.out.println();
        System.out.println("==============================================");
        System.out.println("           FICHEROS Y BASE DE DATOS       ");
        System.out.println("==============================================");
        System.out.println("  [1] Exportar empleados a fichero");
        System.out.println("  [2] Exportar proyectos a fichero");
        System.out.println("  [3] Exportar nóminas a fichero");
        System.out.println("  [4] Leer fichero de empleados");
        System.out.println("  [5] Insertar fichero de empleados a la BBDD");
        System.out.println("  [6] Leer fichero de proyectos");
        System.out.println("  [7] Insertar fichero de proyectos a la BBDD");
        System.out.println("  [8] Leer fichero de nóminas");
        System.out.println("  [9] Insertar fichero de nóminas a la BBDD");
        System.out.println("----------------------------------------------");
        System.out.println("  [0] Volver al menú principal");
        System.out.println("==============================================");

    }

    public static void mostrarPersonasProyectos(ArrayList<String> lista) {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("        PERSONAS ASIGNADAS A PROYECTOS    ");
        System.out.println("==========================================");

        for (String linea : lista) {
            System.out.println(" " + linea);
        }

        System.out.println("------------------------------------------");
    }

    public static void mostrarNominasPorPersona(ArrayList<String> lista) {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("         NÓMINAS POR PERSONA              ");
        System.out.println("==========================================");

        for (String linea : lista) {
            System.out.println(" " + linea);
        }

        System.out.println("------------------------------------------");
    }

    public static void mostrarAtributosProyecto() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("            Datos de un proyecto          ");
        System.out.println("==========================================");
        System.out.println("  [1] Nombre");
        System.out.println("  [2] Deadline");
        System.out.println("  [3] Descripción");
        System.out.println("  [4] Terminado");
        System.out.println("  [0] Cancelar");
        System.out.println("------------------------------------------");

    }

    public static void procesar() {
        System.out.println("------Procesando...---------");
    }

    /**
     * Imprime "Operación realizada exitosamente"
     */
    public static void mostrarOperacionCorrecta() {
        System.out.println();
        System.out.println(">> Operación realizada exitosamente");
    }

    /**
     * Muestra "No se ha podido completar la operacion"
     */
    public static void mostrarOperacionIncorrecta() {
        System.out.println();
        System.out.println(">> No se ha podido completar la operación o no ha podido efectuarse ningún cambio");
    }

    /**
     * Muestra "El dato que busca no existe"
     */
    public static void mostrarInexistencia() {
        System.out.println();
        System.out.println(">> El dato que busca no existe");
    }

    /**
     * Permite al controlador Mostrar un mensaje más específico.
     *
     * @param mensaje
     */
    public static void mostrarTextEspecifico(String mensaje) {
        System.out.println(">> " + mensaje);
    }

    public static void mostrarMenuProyectos() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("           GESTIÓN DE PROYECTOS           ");
        System.out.println("==========================================");
        System.out.println("  [1] Consultar proyectos");
        System.out.println("  [2] Registrar proyecto");
        System.out.println("  [3] Modificar proyecto");
        System.out.println("  [4] Eliminar proyecto");
        System.out.println("------------------------------------------");
        System.out.println("  [0] Volver al menú principal");
        System.out.println("==========================================");

    }

    /**
     * Imprime "Por favor, ingrese el ID de lo que buscas:"
     */
    public static void mostrarPedirId() {
        System.out.println("Por favor, ingrese el ID de lo que buscas: ");
    }

    public static void mostrarMenuNominas() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("            GESTIÓN DE NÓMINAS            ");
        System.out.println("==========================================");
        System.out.println("  [1] Consultar nóminas");
        System.out.println("  [2] Registrar nómina");
        System.out.println("  [3] Modificar nómina");
        System.out.println("  [4] Eliminar nómina");
        System.out.println("------------------------------------------");
        System.out.println("  [0] Volver al menú principal");
        System.out.println("==========================================");

    }

    public static void iniciar() {
        System.out.println("/////////////// Iniciando...");
    }

    public static void mostrarOpcionInvalida() {
        System.out.println(">> Opción inválida");
    }

    public static void mostrarNumInvalido() {
        System.out.println(">> Has ingresado un número inválido, vuelve a intentarlo");
    }

    /**
     * Menu para mostrar estado del proyecto
     * con opciones: (1) terminado |
     * (2) Pendiente |
     * compatible con mi uso del boolean para Proyecto
     */
    public static void mostrarEstadoProyecto() {

        System.out.println();
        System.out.println("  ¿En qué estado está el proyecto?");
        System.out.println("  [1] Terminado");
        System.out.println("  [0] Pendiente");

    }
}
