package modelo;

import java.math.BigDecimal;

public class Nomina {
    private int id;
    private BigDecimal importeFinal;
    private String mes;
    private int year;
    private int personaId;

    //"SELECT ID,importeFinal,year,mes,id_persona FROM nomina";
    public Nomina(int id, BigDecimal importeFinal, int year, String mes, int personaId) {
        this.id = id;
        this.importeFinal = importeFinal;
        this.mes = mes;
        this.personaId = personaId;
        this.year = year;
    }

    public Nomina(BigDecimal importeFinal, int year, String mes, int personaId) {
        this.importeFinal = importeFinal;
        this.mes = mes;
        this.personaId = personaId;
        this.year = year;
    }

    public Nomina(String cadena) {
        String[] datos = cadena.split("\\|", -1);
        this.id = Integer.parseInt(datos[0]);
        this.importeFinal = new BigDecimal(datos[1]);
        this.year = Integer.parseInt(datos[2]);
        this.mes = datos[3];
        this.personaId = Integer.parseInt(datos[4]);
    }


    public int getId() {
        return id;
    }

    public BigDecimal getImporteFinal() {
        return importeFinal;
    }

    public void setImporteFinal(BigDecimal importeFinal) {
        this.importeFinal = importeFinal;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getPersonaId() {
        return personaId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String toInsertSql() {
        return String.format("INSERT INTO nomina (importeFinal, year, mes, id_persona) " +
                "VALUES ( %s, %d, '%s', %d)", importeFinal, year, mes, personaId);
    }

    public String toUpdateSql() {
        return String.format("UPDATE nomina SET importeFinal=%s, year=%d, mes='%s', id_persona=%d " +
                "WHERE id=%d", importeFinal, year, mes, personaId, id);
    }

    public String toDeleteSql() {
        return "DELETE FROM nomina WHERE id=" + id;
    }

    @Override
    public String toString() {
        return String.format(
                "Nomina{id=%d, importeFinal=%s, mes='%s', year=%d, personaId=%d}",
                id, importeFinal, mes, year, personaId
        );
    }

    public String toFichero() {
        return id + "|" + importeFinal + "|" + year + "|" + mes + "|" + personaId;
    }


}

// OJO : TENER CONNTROL DE EXCEPCIONN.

/**
 *
 * UNA PERSONA TIENE VARIAS NOMINAS
 * UNA NOMINA PERTECE A UNA PERSONA
 * 1:N
 * <p>
 * UNA PERSONA PUEDE PERTENECER A VARIOS PROYECTOS
 * Y UN PROYECTO PUEDE TENER VARIAS PERSONAS M:N
 * <p>
 * <p>
 * PERSONA 1   -  N  NOMINA
 */