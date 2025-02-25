package vista;

import controlador.MySqlDao;
import controlador.TorneoProcesado;
import modelo.*;

import java.util.ArrayList;
import java.util.Scanner;

public class VistaTexto {
    Scanner sc;
    MySqlDao bd;



    public VistaTexto(MySqlDao bd) {
        sc = new Scanner(System.in);
        this.bd = bd;
    }

    public void iniciarApp() throws Exception {
        int opcion;
        do {
            opcion = mostrarMenu();

            switch (opcion) {
                case 1:
                    insertarPista();
                    break;
                case 2:
                    mostrarPistas();
                    break;
                case 3:
                    insertarEquipos();
                    break; //añadimos mas opciones al método iniciarApp
                case 4:
                    mostrarEquipos();
                    break;
                case 5:
                    insertarJugadores();
                    break;
                case 6:
                    mostrarJugadores();
                    break;
                case 7:
                    TorneoProcesado.crearEstructuraPartidos(bd);
                    break;
                case 8:
                    introducirResultadosPartido();
                    break;
                case 9:
                    TorneoProcesado.procesarResultadosRegular(bd);
                    break;
                case 10:
                    introducirResultadosPartidoCuartos();
                    break;
                case 11:
                    TorneoProcesado.procesarResultadosCuartos(bd);
                    break;
                case 12:
                    introducirResultadosPartidoSemifinales();
                    break;
                case 13:
                    TorneoProcesado.procesarResultadosSemifinales(bd);
                    break;
                case 14:
                    introducirResultadosPartidoFinal();
                    break;
                case 15:
                    mostrarCuadroCompleto();
            }

        } while (opcion != 0);
    }


    private int mostrarMenu() {
        int opcion;
        System.out.println("1. Insertar modelo.Pista");
        System.out.println("2. Mostrar Pistas Existentes");
        System.out.println("3. Insertar modelo.Equipos");              //añadimos opciones al menu
        System.out.println("4. Mostrar modelo.Equipos Existentes");
        System.out.println("5. Insertar modelo.Jugadores");
        System.out.println("6. Mostrar modelo.Jugadores Existentes");
        System.out.println("7. Crear estructura de partidos del Torneo");
        System.out.println("8. Introducir resultados de partido de Fase Regular");
        System.out.println("9. Procesar Fase Regular y establecer partidos de Cuartos de Final");
        System.out.println("10. Introducir resultados de Cuartos de Final");
        System.out.println("11. Procesar Fase Cuartos y establecer partidos de Semifinales");
        System.out.println("12. Introducir resultados de Semifinales");
        System.out.println("13. Procesar Fase Semifinales y establecer partido de Final");
        System.out.println("14. Introducir resultados de la Final");
        System.out.println("15. Mostrar cuadro completo.");
        System.out.println("0. Acabar");
        opcion = sc.nextInt();
        return opcion;
    }

    private void insertarPista() {
        sc.nextLine();
        System.out.println("Inserción de Pistas");
        System.out.println("-------------------");
        System.out.println("Indica el nombre de la pista a Insertar");
        String nombrePista = sc.nextLine();
        Pista pista = new Pista(0, nombrePista);
        if (bd.insertarPista(pista))
            System.out.println("La pista se ha insertado correctamente");
        else
            System.out.println("No se ha podido insertar la pista en la base de datos");
    }

    private void mostrarPistas() {
        ArrayList<Pista> pistas = bd.obtenerPistas();
        Pista pista;
        for (int i = 0; i < pistas.toArray().length; i++) {
            pista = pistas.get(i);
            System.out.println(pista.toString());
        }
    }

    private void insertarEquipos() {
        sc.nextLine();//no se quede bloqueado el scanner
        System.out.println("Vamos a insertar los equipos");
        System.out.println("-------------------");
        System.out.println("inserta el nombre del equipo");
        String nombreEquipo = sc.nextLine();                      //generamos el String llamando al metodo Scanner
        Equipo equipo = new Equipo(0, nombreEquipo, 0, 0, 0, 0);         //creamos un objeto de la clase equipos con el id0 autoincrementable

        if (bd.insertarEquipo(equipo))
            System.out.println("El Equipo se ha creado correctamente");
        else
            System.out.println("No se ha podido insertar  Equipo");

    }

    private void mostrarEquipos() {
        ArrayList<Equipo> equipos = bd.obtenerEquipos();
        Equipo equipo;
        for (int i = 0; i < equipos.toArray().length; i++) {
            equipo = equipos.get(i);
            System.out.println(equipo.toString());
        }
        /* for (modelo.Equipos equipo : equipos) {
            System.out.println(equipo.toString());
        }*/

    }

    private void mostrarJugadores() {
        ArrayList<Jugador> jugadores = bd.obtenerJugadores();
        Jugador jugador;
        if (jugadores == null)
            System.out.println("No se ha recuperado ningun jugador en la bbdd");
        else {
            for (int i = 0; i < jugadores.toArray().length; i++) {
                jugador = jugadores.get(i);
                System.out.println(jugador.toString());
            }
        }

    }

    private void insertarJugadores() {
        sc.nextLine();
        System.out.println("Inscribir jugadores");
        System.out.println("-------------------");
        System.out.println("Inserta el DNI del jugador");
        String dni = sc.nextLine();
        System.out.println("Indica el nombre del jugador");
        String nombre = sc.nextLine();
        System.out.println("Y por ultimo el identificador del grupo");
        int id = sc.nextInt();

        Jugador jugadores = new Jugador(dni, nombre, id);

        if (bd.insertarJugador(jugadores))
            System.out.println("El jugador se ha creado con exito");
        else
            System.out.println("No se ha podido crear el jugador");


    }

    private void introducirResultadosPartido() {
        Scanner sc = new Scanner(System.in);
        int idPartido, idEquipoLocal, idEquipoVisitante;
        int juegosEquipo1Set1, juegosEquipo1Set2, juegosEquipo1Set3 = 0, juegosEquipo2Set1, juegosEquipo2Set2, juegosEquipo2Set3 = 0;
        boolean mismoEquipoGanaDosPrimerosSets = false, puntuacionLocalActualizada = false, puntuacionVisitanteActualizada = false;
        //primero listamos todos los partidos existentes
        System.out.println("Listado de partidos existentes en el sistema");
        System.out.println("--------------------------------------------");
        ArrayList<Partido> partidos = bd.obtenerPartidosRegular();
        ArrayList<PuntuacionEquipoPartido> puntuacionesEquiposPartido;
        for (int i = 0; i < partidos.size(); i++) {
            System.out.println(partidos.get(i).toString());
            puntuacionesEquiposPartido = bd.obtenerPuntuacionEquiposPartido(partidos.get(i).getId());
            if (puntuacionesEquiposPartido.size() > 0) {
                System.out.println(puntuacionesEquiposPartido.get(0));
                System.out.println(puntuacionesEquiposPartido.get(1));
            } else {
                System.out.println("No estan definidos todavía los equipos de este partido");
            }


        }
        //interactuamos con el usuario para que meta los resultados del partido
        System.out.println("Introduce el id partido del que quieres meter los resultados");
        idPartido = sc.nextInt();
        System.out.println("Introduce el id del equipo que ha jugado como local");
        idEquipoLocal = sc.nextInt();
        System.out.println("Introduce el id del equipo que ha jugado como visitante");
        idEquipoVisitante = sc.nextInt();
        System.out.println("Introduce juegos del equipo local en el set 1");
        juegosEquipo1Set1 = sc.nextInt();
        System.out.println("Introduce juegos del equipo visitante en el set 1");
        juegosEquipo2Set1 = sc.nextInt();
        System.out.println("Introduce juegos del equipo local en el set 2");
        juegosEquipo1Set2 = sc.nextInt();
        System.out.println("Introduce juegos del equipo visitante en el set 2");
        juegosEquipo2Set2 = sc.nextInt();
        if (!((juegosEquipo1Set1 > juegosEquipo2Set1 && juegosEquipo1Set2 > juegosEquipo2Set2) || (juegosEquipo2Set1 > juegosEquipo1Set1 && juegosEquipo2Set2 > juegosEquipo1Set2))) {
            System.out.println("Introduce juegos del equipo local en el set 3");
            juegosEquipo1Set3 = sc.nextInt();
            System.out.println("Introduce juegos del equipo visitante en el set 3");
            juegosEquipo2Set3 = sc.nextInt();
        }
        PartidoEquipo puntuacionLocal = new PartidoEquipo(idPartido, idEquipoLocal, juegosEquipo1Set1, juegosEquipo1Set2, juegosEquipo1Set3);
        puntuacionLocalActualizada = bd.actualizarPuntuacionEquipoPartido(puntuacionLocal);
        PartidoEquipo puntuacionVisitante = new PartidoEquipo(idPartido, idEquipoVisitante, juegosEquipo2Set1, juegosEquipo2Set2, juegosEquipo2Set3);
        puntuacionVisitanteActualizada = bd.actualizarPuntuacionEquipoPartido(puntuacionVisitante);
        if (puntuacionLocalActualizada && puntuacionVisitanteActualizada)
            System.out.println("Las puntuaciones del partido se han actualizado correctamente");
        else
            System.out.println("Ha habido algún problema al actulizar la puntuación del partido");

    }

    private void introducirResultadosPartidoCuartos() {
        Scanner sc = new Scanner(System.in);
        int idPartido, idEquipoLocal, idEquipoVisitante;
        int juegosEquipo1Set1, juegosEquipo1Set2, juegosEquipo1Set3 = 0, juegosEquipo2Set1, juegosEquipo2Set2, juegosEquipo2Set3 = 0;
        boolean mismoEquipoGanaDosPrimerosSets = false, puntuacionLocalActualizada = false, puntuacionVisitanteActualizada = false;
        //primero listamos todos los partidos existentes
        System.out.println("Listado de partidos de Cuartos Final");
        System.out.println("--------------------------------------------");
        ArrayList<Partido> partidos = bd.obtenerPartidosCuadro("Cuartos de final");
        ArrayList<PuntuacionEquipoPartido> puntuacionesEquiposPartido;
        for (int i = 0; i < partidos.size(); i++) {
            System.out.println(partidos.get(i).toString());
            puntuacionesEquiposPartido = bd.obtenerPuntuacionEquiposPartido(partidos.get(i).getId());
            if (puntuacionesEquiposPartido.size() > 0) {
                System.out.println(puntuacionesEquiposPartido.get(0));
                System.out.println(puntuacionesEquiposPartido.get(1));
            } else {
                System.out.println("No estan definidos todavía los equipos de este partido");
            }


        }
        //interactuamos con el usuario para que meta los resultados del partido
        System.out.println("Introduce el id partido del que quieres meter los resultados");
        idPartido = sc.nextInt();
        System.out.println("Introduce el id del equipo que ha jugado como local");
        idEquipoLocal = sc.nextInt();
        System.out.println("Introduce el id del equipo que ha jugado como visitante");
        idEquipoVisitante = sc.nextInt();
        System.out.println("Introduce juegos del equipo local en el set 1");
        juegosEquipo1Set1 = sc.nextInt();
        System.out.println("Introduce juegos del equipo visitante en el set 1");
        juegosEquipo2Set1 = sc.nextInt();
        System.out.println("Introduce juegos del equipo local en el set 2");
        juegosEquipo1Set2 = sc.nextInt();
        System.out.println("Introduce juegos del equipo visitante en el set 2");
        juegosEquipo2Set2 = sc.nextInt();
        if (!((juegosEquipo1Set1 > juegosEquipo2Set1 && juegosEquipo1Set2 > juegosEquipo2Set2) || (juegosEquipo2Set1 > juegosEquipo1Set1 && juegosEquipo2Set2 > juegosEquipo1Set2))) {
            System.out.println("Introduce juegos del equipo local en el set 3");
            juegosEquipo1Set3 = sc.nextInt();
            System.out.println("Introduce juegos del equipo visitante en el set 3");
            juegosEquipo2Set3 = sc.nextInt();
        }
        PartidoEquipo puntuacionLocal = new PartidoEquipo(idPartido, idEquipoLocal, juegosEquipo1Set1, juegosEquipo1Set2, juegosEquipo1Set3);
        puntuacionLocalActualizada = bd.actualizarPuntuacionEquipoPartido(puntuacionLocal);
        PartidoEquipo puntuacionVisitante = new PartidoEquipo(idPartido, idEquipoVisitante, juegosEquipo2Set1, juegosEquipo2Set2, juegosEquipo2Set3);
        puntuacionVisitanteActualizada = bd.actualizarPuntuacionEquipoPartido(puntuacionVisitante);
        if (puntuacionLocalActualizada && puntuacionVisitanteActualizada)
            System.out.println("Las puntuaciones del partido se han actualizado correctamente");
        else
            System.out.println("Ha habido algún problema al actulizar la puntuación del partido");

    }

    private void introducirResultadosPartidoSemifinales() {
        Scanner sc = new Scanner(System.in);
        int idPartido, idEquipoLocal, idEquipoVisitante;
        boolean puntuacionLocalActualizada, puntuacionVisitanteActualizada;
        int juegosEquipo1Set1, juegosEquipo1Set2, juegosEquipo1Set3 = 0, juegosEquipo2Set1, juegosEquipo2Set2, juegosEquipo2Set3 = 0;

        //primero listamos todos los partidos existentes
        System.out.println("Listado de partidos de Semifinales");
        System.out.println("--------------------------------------------");
        ArrayList<Partido> partidos = bd.obtenerPartidosCuadro("Semifinales");
        ArrayList<PuntuacionEquipoPartido> puntuacionesEquiposPartido;
        for (int i = 0; i < partidos.size(); i++) {
            System.out.println(partidos.get(i).toString());
            puntuacionesEquiposPartido = bd.obtenerPuntuacionEquiposPartido(partidos.get(i).getId());
            if (puntuacionesEquiposPartido.size() > 0) {
                System.out.println(puntuacionesEquiposPartido.get(0));
                System.out.println(puntuacionesEquiposPartido.get(1));
            } else {
                System.out.println("No estan definidos todavía los equipos de este partido");
            }
        }
        //interactuamos con el usuario para que meta los resultados del partido
        System.out.println("Introduce el id partido del que quieres meter los resultados");
        idPartido = sc.nextInt();
        System.out.println("Introduce el id del equipo que ha jugado como local");
        idEquipoLocal = sc.nextInt();
        System.out.println("Introduce el id del equipo que ha jugado como visitante");
        idEquipoVisitante = sc.nextInt();
        System.out.println("Introduce juegos del equipo local en el set 1");
        juegosEquipo1Set1 = sc.nextInt();
        System.out.println("Introduce juegos del equipo visitante en el set 1");
        juegosEquipo2Set1 = sc.nextInt();
        System.out.println("Introduce juegos del equipo local en el set 2");
        juegosEquipo1Set2 = sc.nextInt();
        System.out.println("Introduce juegos del equipo visitante en el set 2");
        juegosEquipo2Set2 = sc.nextInt();
        if (!((juegosEquipo1Set1 > juegosEquipo2Set1 && juegosEquipo1Set2 > juegosEquipo2Set2) || (juegosEquipo2Set1 > juegosEquipo1Set1 && juegosEquipo2Set2 > juegosEquipo1Set2))) {
            System.out.println("Introduce juegos del equipo local en el set 3");
            juegosEquipo1Set3 = sc.nextInt();
            System.out.println("Introduce juegos del equipo visitante en el set 3");
            juegosEquipo2Set3 = sc.nextInt();
        }
        PartidoEquipo puntuacionLocal = new PartidoEquipo(idPartido, idEquipoLocal, juegosEquipo1Set1, juegosEquipo1Set2, juegosEquipo1Set3);
        puntuacionLocalActualizada = bd.actualizarPuntuacionEquipoPartido(puntuacionLocal);
        PartidoEquipo puntuacionVisitante = new PartidoEquipo(idPartido, idEquipoVisitante, juegosEquipo2Set1, juegosEquipo2Set2, juegosEquipo2Set3);
        puntuacionVisitanteActualizada = bd.actualizarPuntuacionEquipoPartido(puntuacionVisitante);
        if (puntuacionLocalActualizada && puntuacionVisitanteActualizada)
            System.out.println("Las puntuaciones del partido se han actualizado correctamente");
        else
            System.out.println("Ha habido algún problema al actulizar la puntuación del partido");

    }

    private void introducirResultadosPartidoFinal() {
        Scanner sc = new Scanner(System.in);
        int idPartido, idEquipoLocal, idEquipoVisitante;
        boolean puntuacionLocalActualizada, puntuacionVisitanteActualizada;
        int juegosEquipo1Set1, juegosEquipo1Set2, juegosEquipo1Set3 = 0, juegosEquipo2Set1, juegosEquipo2Set2, juegosEquipo2Set3 = 0;

        //primero listamos todos los partidos existentes
        System.out.println("Final");
        System.out.println("-----");
        ArrayList<Partido> partidos = bd.obtenerPartidosCuadro("Final");
        ArrayList<PuntuacionEquipoPartido> puntuacionesEquiposPartido;

            System.out.println(partidos.get(0).toString());
            puntuacionesEquiposPartido = bd.obtenerPuntuacionEquiposPartido(partidos.get(0).getId());
            if (puntuacionesEquiposPartido.size() > 0) {
                System.out.println(puntuacionesEquiposPartido.get(0));
                System.out.println(puntuacionesEquiposPartido.get(1));
            } else {
                System.out.println("No estan definidos todavía los equipos de la Final");
            }

        //interactuamos con el usuario para que meta los resultados del partido
        idPartido = partidos.get(0).getId();
        idEquipoLocal = puntuacionesEquiposPartido.get(0).getIdEquipo();
        idEquipoVisitante = puntuacionesEquiposPartido.get(1).getIdEquipo();

        System.out.println("Introduce juegos del equipo local en el set 1");
        juegosEquipo1Set1 = sc.nextInt();
        System.out.println("Introduce juegos del equipo visitante en el set 1");
        juegosEquipo2Set1 = sc.nextInt();
        System.out.println("Introduce juegos del equipo local en el set 2");
        juegosEquipo1Set2 = sc.nextInt();
        System.out.println("Introduce juegos del equipo visitante en el set 2");
        juegosEquipo2Set2 = sc.nextInt();
        if (!((juegosEquipo1Set1 > juegosEquipo2Set1 && juegosEquipo1Set2 > juegosEquipo2Set2) || (juegosEquipo2Set1 > juegosEquipo1Set1 && juegosEquipo2Set2 > juegosEquipo1Set2))) {
            System.out.println("Introduce juegos del equipo local en el set 3");
            juegosEquipo1Set3 = sc.nextInt();
            System.out.println("Introduce juegos del equipo visitante en el set 3");
            juegosEquipo2Set3 = sc.nextInt();
        }


        PartidoEquipo puntuacionLocal = new PartidoEquipo(idPartido, idEquipoLocal, juegosEquipo1Set1, juegosEquipo1Set2, juegosEquipo1Set3);
        puntuacionLocalActualizada = bd.actualizarPuntuacionEquipoPartido(puntuacionLocal);
        PartidoEquipo puntuacionVisitante = new PartidoEquipo(idPartido, idEquipoVisitante, juegosEquipo2Set1, juegosEquipo2Set2, juegosEquipo2Set3);
        puntuacionVisitanteActualizada = bd.actualizarPuntuacionEquipoPartido(puntuacionVisitante);
        if (puntuacionLocalActualizada && puntuacionVisitanteActualizada)
            System.out.println("Las puntuaciones de la final se han actualizado correctamente");
        else
            System.out.println("Ha habido algún problema al actulizar la puntuación del partido");

    }

    void mostrarCuadroCompleto(){
        //primero listamos todos los partidos existentes
        System.out.println("Listado de partidos de Cuartos Final");
        System.out.println("--------------------------------------------");
        ArrayList<Partido> partidos = bd.obtenerPartidosCuadro("Cuartos de final");
        ArrayList<PuntuacionEquipoPartido> puntuacionesEquiposPartido;
        for (int i = 0; i < partidos.size(); i++) {
            System.out.println(partidos.get(i).toString());
            puntuacionesEquiposPartido = bd.obtenerPuntuacionEquiposPartido(partidos.get(i).getId());
            if (puntuacionesEquiposPartido.size() > 0) {
                System.out.println(puntuacionesEquiposPartido.get(0));
                System.out.println(puntuacionesEquiposPartido.get(1));
            } else {
                System.out.println("No estan definidos todavía los equipos de este partido");
            }
        }
        System.out.println("");
        System.out.println("Listado de partidos de Semifinales");
        System.out.println("--------------------------------------------");
        partidos = bd.obtenerPartidosCuadro("Semifinales");
        for (int i = 0; i < partidos.size(); i++) {
            System.out.println(partidos.get(i).toString());
            puntuacionesEquiposPartido = bd.obtenerPuntuacionEquiposPartido(partidos.get(i).getId());
            if (puntuacionesEquiposPartido.size() > 0) {
                System.out.println(puntuacionesEquiposPartido.get(0));
                System.out.println(puntuacionesEquiposPartido.get(1));
            } else {
                System.out.println("No estan definidos todavía los equipos de las semifinales");
            }
        }
        System.out.println("");
        System.out.println("Final");
        System.out.println("--------------------------------------------");
        partidos = bd.obtenerPartidosCuadro("Final");

            System.out.println(partidos.get(0).toString());
            puntuacionesEquiposPartido = bd.obtenerPuntuacionEquiposPartido(partidos.get(0).getId());
            if (puntuacionesEquiposPartido.size() > 0) {
                System.out.println(puntuacionesEquiposPartido.get(0));
                System.out.println(puntuacionesEquiposPartido.get(1));
            } else {
                System.out.println("No estan definidos todavía los equipos de la final");
            }

    }
}


