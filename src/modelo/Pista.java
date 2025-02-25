package modelo;

public class Pista {
    private int id;
    private String nombre;

    public Pista(){

    }
    public Pista(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return String.format("ID: %02d | Nombre: %s ", id, nombre);

    }
}
