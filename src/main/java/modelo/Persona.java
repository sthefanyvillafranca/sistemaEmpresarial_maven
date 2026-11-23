package modelo;

public class Persona {
    private int id;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String cargo;


    public Persona(int id, String nombre, String apellidos, String direccion, String cargo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.cargo = cargo;
    }

    public Persona(String nombre, String apellidos, String direccion, String cargo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.cargo = cargo;
    }

    public Persona(String cadena) {
        String[] datos = cadena.split("\\|", -1); //  split con -1: sugerencia de Claude (IA)
        // para conservar los campos vacíos del final de la línea
        this.id = Integer.parseInt(datos[0]);
        this.nombre = datos[1];
        this.apellidos = datos[2];
        this.direccion = datos[3];
        this.cargo = datos[4];
    } // este constructor lo vimos en clases, solo reutilicé adaptándolo a Persona.


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;

    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String toFichero() {
        return id + "|" + nombre + "|" + apellidos + "|" + direccion + "|" + cargo;
    }


    public String toInsertSql() {
        return String.format("INSERT INTO persona (nombre, apellidos," +
                " direccion, cargo) VALUES ('%s', '%s', '%s', '%s')", nombre, apellidos, direccion, cargo);
    }

    public String toUpdateSql() {
        return String.format("UPDATE persona SET nombre='%s', apellidos='%s'," +
                " direccion='%s', cargo='%s' WHERE id=%d", nombre, apellidos, direccion, cargo, id);
    }

    public String toDeleteSql() {
        return "DELETE FROM persona WHERE id=" + id;
    }


    @Override
    public String toString() {
        return String.format(
                "Persona{id=%d, nombre='%s', apellidos='%s', direccion='%s', cargo='%s'}",
                id, nombre, apellidos, direccion, cargo
        );
    }
}
