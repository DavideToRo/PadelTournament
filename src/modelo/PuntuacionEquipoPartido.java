package modelo;

public class PuntuacionEquipoPartido {
    private int idPartido;
    private int idEquipo;
    private String nombre;
    private int juegosS1, juegosS2, juegosS3;

    public String getNombre() {
        return nombre;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public PuntuacionEquipoPartido(int idPartido, int idEquipo, String nombre, int juegosS1, int juegosS2, int juegosS3) {
        this.idPartido = idPartido;
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.juegosS1 = juegosS1;
        this.juegosS2 = juegosS2;
        this.juegosS3 = juegosS3;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public int getJuegosS1() {
        return juegosS1;
    }

    public int getJuegosS2() {
        return juegosS2;
    }

    public int getJuegosS3() {
        return juegosS3;
    }

    @Override
    public String toString() {
        return "modelo.PuntuacionEquipoPartido{" +
                "idPartido=" + idPartido +
                "idEquipo=" +idEquipo+
                ", nombre='" + nombre + '\'' +
                ", juegosS1=" + juegosS1 +
                ", juegosS2=" + juegosS2 +
                ", juegosS3=" + juegosS3 +
                '}';
    }
}
