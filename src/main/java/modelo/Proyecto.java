package modelo;

import java.sql.Date;
import java.time.LocalDate;

public class Proyecto {
    private int id;
    private String nombre;
    private LocalDate deadLine;
    private String descripcion;
    private boolean terminado;

    // esto forma parte de una relacion m:n que tiene una tabla intermedia en la base de datos

    public Proyecto(int id, String nombre, LocalDate deadLine, String descripcion, boolean terminado) {
        this.id = id;
        this.nombre = nombre;
        this.deadLine = deadLine;
        this.descripcion = descripcion;
        this.terminado = terminado;
    }

    public Proyecto(String nombre, LocalDate deadLine, String descripcion, boolean terminado) {
        this.nombre = nombre;
        this.deadLine = deadLine;
        this.descripcion = descripcion;
        this.terminado = terminado;
    }

    public Proyecto(String cadena) {
        String[] datos = cadena.split("\\|", -1);
        this.id = Integer.parseInt(datos[0]);
        this.nombre = datos[1];
        this.deadLine = LocalDate.parse(datos[2]);
        this.descripcion = datos[3];
        this.terminado = Boolean.parseBoolean(datos[4]);
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public String toInsertSql() {
        return "INSERT INTO proyecto (nombre, deadLine, descripcion, terminado) " +
                "VALUES ('" + nombre + "','" + deadLine + "','" + descripcion + "'," + terminado + ")";
    }

    public String toUpdateSql() {
        return String.format("UPDATE proyecto SET Nombre='%s', " +
                "deadLine='%s', descripcion='%s', terminado=%b WHERE ID=%d", nombre, deadLine, descripcion, terminado, id);

    }

    public String toDeleteSql() {
        return "DELETE FROM proyecto WHERE ID=" + id;
    }

    public String toFichero() {
        return id + "|" + nombre + "|" + deadLine + "|" + descripcion + "|" + terminado;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", deadLine=" + deadLine +
                ", descripcion='" + descripcion + '\'' +
                ", terminado=" + terminado +
                '}';
    }


}

/**
 * C1 - C2
 * <p>
 * EN LA BBDD YA HAY UNA TABLA INTERMEDIA
 * <p>
 * METES LOS DATOS PRIMERO EN CADA UNA DE SUS TABLAS
 * Y LUEGO EL ID DE UNA Y EL ID DE LA OTRA LAS METES EN ESA TABLA INTERMEDIA
 * NO NECESITAS CREAR UNA CLASE INTERMEDIA
 * <p>
 * <p>
 * TO DO EN EL PROYECTO
 * // investigar cómo hacer una fecha real relacionada con la bbdd - DONE: localdate
 */