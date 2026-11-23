import controlador.ControladorMaestro;
import controlador.InterfaceControlador;


public class Main {

    public static void main(String[] args) {

        // antes: ControladorMaestro.menuPrincipal();
        // Ahora la variable es del tipo de la interfaz y se llama a menu().
        InterfaceControlador controlador = new ControladorMaestro();
        controlador.menu();

    }
}

//
