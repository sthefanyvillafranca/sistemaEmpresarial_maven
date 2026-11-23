package repositorio;

import java.sql.SQLException;

// Contrato de una fuente de datos: quien lo cumpla tiene que saber
// agregar, modificar y eliminar a partir de una sentencia ya escrita.
// Aquí solo van las firmas; el código está en BaseDeDatos.
public interface InterfaceDatos {

    public abstract int agregar(String sql) throws SQLException, ClassNotFoundException;

    public abstract int modificar(String sql) throws SQLException, ClassNotFoundException;

    public abstract int eliminar(String sql) throws SQLException, ClassNotFoundException;

}
