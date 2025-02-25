package modelo;

public class Jugador {
    private int id;
    private String dni;
    private String nombre;
    private int idEquipo;

    public Jugador(){

    }
    public Jugador(int id, String dni, String nombre, int idEquipo) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.idEquipo = idEquipo;
    }

    public Jugador(String dni, String nombre, int idEquipo) {
        this.dni = dni;
        this.nombre = nombre;
        this.idEquipo = idEquipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    @Override
    public String toString() {
        return String.format("ID: %02d | DNI: %10s | Nombre: %40s | ID Equipo: %02d", id, dni, nombre, idEquipo);
    }
}
