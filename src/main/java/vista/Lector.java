package vista;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Lector {
    private static Scanner sc = new Scanner(System.in);
    // solo se crea una vez, pertenece a la clase. por eso es static
    // asi no tengo que poner un Lector lector = new Lector(); en cada metodo del controlador

    public static int pedirNumero() {

        int num = 0;
        boolean valido = false;

        while (!valido) {
            Consola.mostrarTextEspecifico("Seleccione una opción: ");

            try {
                num = sc.nextInt();
                sc.nextLine();// el next line consume lo sobrante en la linea de entrada,
                // como un salto de linea, u otra cosa que no sea un numero.
                valido = true; // el bucle termina solo cuando el número es válido.


            } catch (InputMismatchException e) { // si scanner falla por ingresar valor incorrecto,
                // ese dato ingresado en la entrada queda "pendiente"
                sc.nextLine();                 // so, capturo el error, y pongo un nextLine que consume al dato erroneo
                // y así luego puedo volver a preguntar gracias al bucle
                System.out.println("Ingresa un numero válido!");

            }
        }

        return num;
    }

    public static int pedirNumeroParaObjeto() {
        Consola.mostrarTextEspecifico("Escribe el numero del ID de lo que buscas: ");
        int num = sc.nextInt();
        sc.nextLine();
        return num;
    }


    /**
     * Pedir dato simple con sc.nextLine();
     *
     * @return String
     */
    public static String pedirDato() {
        String dato = sc.nextLine();
        return dato;
    }

    public static int pedirNumSimple() {
        int num = 0;
        boolean valido = false;

        while (!valido) {
            try {
                num = sc.nextInt();
                sc.nextLine();
                if (num >= 0) {
                    valido = true;
                } else {
                    Consola.mostrarNumInvalido();
                }
            } catch (InputMismatchException e) {
                Consola.mostrarNumInvalido();
                sc.nextLine();
            }
        }

        return num;
    }

    /**
     * PedirValor
     * Regresa un BigDecimall
     * todo : validacion de entrada
     */
    public static BigDecimal pedirValor() {
        BigDecimal valor;
        valor = new BigDecimal(sc.nextLine());
        return valor;
    }

    /**
     * Sirve para pedir una fecha correctamente
     * (yyyy-MM-dd)
     *
     * @return LocalDate
     */
    public static LocalDate pedirDate() { //2026-11-02 (yyyy-MM-dd).
        LocalDate date = null;
        boolean valido = false;
        while (!valido) {
            Consola.mostrarTextEspecifico("Escribe una fecha: ");
            try {
                date = LocalDate.parse(sc.nextLine()); //doc: parse Obtains an instance of LocalDate
                // from a text string such as 2007-12-03.
                // por lo que puedes poner un nextline dentro de parse
                valido = true;

            } catch (DateTimeParseException exception) {
                Consola.mostrarTextEspecifico("Debes ingresar un formato de fecha válido 2026-11-02 (yyyy-MM-dd)");
            }
        }
        return date;
    } // en este metodo el comportamiento es muy diferente a cuando uso el next int
    // pues si una persona escribe "gatitos" en vez de un numero cuando uso el next int
    // ocurre un error y esa entrada nunca se consume, por eso en pedirNumero se mete un nextline
    // que consume el error
    // pero aqui es diferente: next line se "come" lo que le pasas por entrada (un formato incorrecto).
    // y el error se produce con el parse al intentar transformarlo a fecha, falla
    // por eso pongo DateTimeParseException
    // que es una excepcion que parse arroja :)
    // la documentación lo dice

    // queria probar con un tipo de dato especifico, pero no sé si hubiera sido mejor como un simple string
    /**
     * Throws:
     * DateTimeParseException – if the text cannot be parsed
     */


    //llave final
}

