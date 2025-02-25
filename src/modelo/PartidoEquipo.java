package modelo;

public class PartidoEquipo {
    private int idPartido;
    private int idEquipo;
    private int juegosS1;
    private int juegosS2;
    private int juegosS3;

    public PartidoEquipo(int idPartido, int idEquipo, int juegosS1, int juegosS2, int juegosS3) {
        this.idPartido = idPartido;
        this.idEquipo = idEquipo;
        this.juegosS1 = juegosS1;
        this.juegosS2 = juegosS2;
        this.juegosS3 = juegosS3;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getJuegosS1() {
        return juegosS1;
    }

    public void setJuegosS1(int juegosS1) {
        this.juegosS1 = juegosS1;
    }

    public int getJuegosS2() {
        return juegosS2;
    }

    public void setJuegosS2(int juegosS2) {
        this.juegosS2 = juegosS2;
    }

    public int getJuegosS3() {
        return juegosS3;
    }

    public void setJuegosS3(int juegosS3) {
        this.juegosS3 = juegosS3;
    }
}
