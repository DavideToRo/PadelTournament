package modelo;

public class Equipo {
    private int id;     //declaramos los atributos-variables
    private String nombre;

    private int puntos;//partidos ganados
    private int diferenciaSets;
    private int diferenciaJuegos;

    private int posicionSorteo;


    public Equipo(int id, String nombre, int puntos, int diferenciaSets, int diferenciaJuegos, int posicionSorteo){   //generamos constructor con parametros

        this.id=id;
        this.nombre=nombre;
        this.puntos = puntos;
        this.diferenciaSets = diferenciaSets;
        this.diferenciaJuegos = diferenciaJuegos;
        this.posicionSorteo = posicionSorteo;
    }
    public Equipo(){   //generamos un constructor vacio

    }
    public int getPosicionSorteo() {
        return posicionSorteo;
    }

    public void setPosicionSorteo(int posicionSorteo) {
        this.posicionSorteo = posicionSorteo;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getDiferenciaSets() {
        return diferenciaSets;
    }

    public void setDiferenciaSets(int diferenciaSets) {
        this.diferenciaSets = diferenciaSets;
    }

    public int getDiferenciaJuegos() {
        return diferenciaJuegos;
    }

    public void setDiferenciaJuegos(int diferenciaJuegos) {
        this.diferenciaJuegos = diferenciaJuegos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    @Override
    public String toString() {
        return String.format("ID: %02d | Nombre: %20s | Puntos: %02d | Diferencia Sets: %02d | Diferencia Juegos: %02d | Posicion Sorteo: %02d", id, nombre, puntos, diferenciaSets, diferenciaJuegos, posicionSorteo);
    }

}
