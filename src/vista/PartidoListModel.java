package vista;

import modelo.Equipo;
import modelo.Partido;
import modelo.PartidoEquipo;
import modelo.PuntuacionEquipoPartido;

public class PartidoListModel {
    private Partido partido;
    private PuntuacionEquipoPartido puntuacionEquipoPartido1;
    private PuntuacionEquipoPartido puntuacionEquipoPartido2;

    public PartidoListModel(Partido partido, PuntuacionEquipoPartido puntuacionEquipoPartido1, PuntuacionEquipoPartido puntuacionEquipoPartido2) {
        this.partido = partido;
        this.puntuacionEquipoPartido1 = puntuacionEquipoPartido1;
        this.puntuacionEquipoPartido2 = puntuacionEquipoPartido2;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public PuntuacionEquipoPartido getPuntuacionEquipoPartido1() {
        return puntuacionEquipoPartido1;
    }

    public void setPuntuacionEquipoPartido1(PuntuacionEquipoPartido puntuacionEquipoPartido1) {
        this.puntuacionEquipoPartido1 = puntuacionEquipoPartido1;
    }

    public PuntuacionEquipoPartido getPuntuacionEquipoPartido2() {
        return puntuacionEquipoPartido2;
    }

    public void setPuntuacionEquipoPartido2(PuntuacionEquipoPartido puntuacionEquipoPartido2) {
        this.puntuacionEquipoPartido2 = puntuacionEquipoPartido2;
    }

}