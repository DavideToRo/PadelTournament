package modelo;

public class Partido {
    private int id;
    private int dia;
    private int mes;
    private int anyo;
    private int hora;

    private int id_pista;
    private String denominacion;

    public Partido (){

    }

    public Partido(int id,int dia, int mes, int anyo, int hora, int id_pista, String grupo) {
        this.id = id;
        this.dia = dia;
        this.mes = mes;
        this.anyo = anyo;
        this.hora = hora;
        this.id_pista = id_pista;
        this.denominacion = grupo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getIdPista() {
        return id_pista;
    }

    public void setIdPista(int id_pista) {
        this.id_pista = id_pista;
    }

    public String getDenominacion() {
        return denominacion;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "id=" + id +
                ", dia=" + dia +
                ", mes=" + mes +
                ", anyo=" + anyo +
                ", hora=" + hora +
                ", id_pista=" + id_pista +
                ", denominacion='" + denominacion + '\'' +
                '}';
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }
}
