package controlador;

import modelo.Equipo;
import modelo.Partido;
import modelo.PartidoEquipo;
import modelo.Pista;

import java.util.*;

public class TorneoProcesado {


    public static void crearEstructuraPartidos(MySqlDao bd) throws Exception {
        ArrayList<Equipo> equiposInicial =bd.obtenerEquipos();
        if(equiposInicial.size()!=16){
            throw new Exception("La cantidad de equipos debe ser exactamente 16. Actualmente hay " +equiposInicial.size()+".");

        }


        ArrayList<Equipo> equiposSorteados = new ArrayList<Equipo>();
        ArrayList<Pista> pistas = bd.obtenerPistas();
        if (pistas.size()==0){
            throw new Exception("Tienes que insertar alguna pista");
        }
        Equipo equipo;
        equiposInicial = bd.obtenerEquipos();
        //ahora llevo a cabo el sorteo de los equipos.  La salida de este proceso es equiposSorteados;
        int numEquipos = 16;
        int posAleatoria;
        while (numEquipos > 1) {
            posAleatoria = (int) (Math.random() * numEquipos);
            equipo = equiposInicial.get(posAleatoria);
            equiposInicial.remove(posAleatoria);
            equiposSorteados.add(equipo);
            numEquipos--;
        }
        equipo = equiposInicial.get(0);   //situacion para el ultimo equipo
        equiposSorteados.add(equipo);
        //voy a partir de la idea que las horas de torneo son entre las 12:00 y las 22:00, ultimos partidos a las 20:00
        //y empiezo el día 1/12/2023 y el torneo se prolonga los dias que necesite según numero de pistas
        int hora = 12;
        int dia = 1, mes = 12, anyo = 2023;
        pistas = bd.obtenerPistas();
        int numPistas = pistas.size();
        int contPistas = 0;



            List<Partido> partidos = new ArrayList<>();
            Map<Partido, List<Integer>> partidoEquiposMap = new HashMap<>();

//funciona pero tenemos que insertar pistas despues, no lo borramos por motivos academicos ;)
           /* for (int grupo = 0; grupo < 4; grupo++) {
                int inicio = grupo * 4;
                int fin = inicio + 3;
                for (int i = inicio; i <= fin; i++) {
                    for (int j = i + 1; j <= fin; j++) {
                        Partido partido = new Partido(0, dia, mes, anyo, hora, pistas.get(contPistas % pistas.size()).getId(), "Grupo " + (char)('A' + grupo));
                        partidos.add(partido);
                        partidoEquiposMap.put(partido, Arrays.asList(equiposSorteados.get(i).getId(), equiposSorteados.get(j).getId()));
                        contPistas = (contPistas + 1) % pistas.size();
                    }
                }
            }*/
// de esta manera generamos los encuentros aleatorios pero no los insertamos todavia por que necesitamos hacerlos de forma aleatoria
 //si no, el bucle nos lo pone de forma consecutiva.

        for (int grupo = 0; grupo < 4; grupo++) {
            int inicio = grupo * 4;
            int fin = inicio + 3;
            for (int i = inicio; i <= fin; i++) {
                for (int j = i + 1; j <= fin; j++) {
                    // Crea el partido sin asignar pista (usar 0 o un valor predeterminado)
                    Partido partido = new Partido(0, dia, mes, anyo, hora, 0, "Grupo " + (char)('A' + grupo)); // Pista no asignada aquí
                    partidos.add(partido);
                    partidoEquiposMap.put(partido, Arrays.asList(equiposSorteados.get(i).getId(), equiposSorteados.get(j).getId()));
                }
            }
        }
//comentado para la posteridad
       /* for (int grupo = 0; grupo < 4; grupo++) {
            int inicio = grupo * 4;
            int fin = inicio + 3;
            for (int i = inicio; i <= fin; i++) {
                for (int j = i + 1; j <= fin; j++) {
                    Partido partido = new Partido(0, dia, mes, anyo, hora, 0, "Grupo " + (char)('A' + grupo)); // Asigna 0 o un valor predeterminado a id_pista
                    partidos.add(partido);
                    partidoEquiposMap.put(partido, Arrays.asList(equiposSorteados.get(i).getId(), equiposSorteados.get(j).getId()));
                }
            }
        }*/
//con este metodo estatico mezclamos aleatoriamento los encuentros, para que los mismo equipos no jueguen a al vez a la misma hora.
        Collections.shuffle(partidos);



        //tambien funciona

       /* for (Partido partido : partidos) {
            List<Integer> idsEquipos = partidoEquiposMap.get(partido);
            if (idsEquipos != null && idsEquipos.size() == 2) {
                int idEquipo1 = idsEquipos.get(0);
                int idEquipo2 = idsEquipos.get(1);

                // Inserta el partido y  en la base de datos.
                bd.insertarEstructuraCompletaPartidoRegular(partido, idEquipo1, idEquipo2);


                if (contPistas < numPistas - 1) {
                    contPistas++;
                } else {
                    contPistas = 0;
                    hora = hora + 2;

                }
            }
        }*/



        for (Partido partido : partidos) {
            List<Integer> idsEquipos = partidoEquiposMap.get(partido);
            if (idsEquipos != null && idsEquipos.size() == 2) {
                int idEquipo1 = idsEquipos.get(0);
                int idEquipo2 = idsEquipos.get(1);

                // Asigna la pista y la hora actual al partido antes de insertarlo
                partido.setIdPista(pistas.get(contPistas % pistas.size()).getId()); // Asigna la pista
                // En cada pasada asignamos una hora mes y dia.
                partido.setHora(hora);
                partido.setDia(dia);
                partido.setAnyo(anyo);
                partido.setMes(mes);

                // Inserta el partido en la base de datos
                bd.insertarEstructuraCompletaPartidoRegular(partido, idEquipo1, idEquipo2);

                // Actualiza contadores para el próximo partido
                contPistas++;
                if (contPistas % pistas.size() == 0) {
                    hora += 2; // Ajusta la hora después de usar todas las pistas
                    if (hora > 20) { // se puede modificar, mas adelante lo cambiaremos
                        hora = 12; // Reinicia a la hora inicial para el siguiente día
                        dia++; // Aumenta el día
                    }
                }
            }
        }




//es el antiguo metodo, el problema que poniamos los equipos a juagar de forma consecutiva.

       /*for (int i = 0; i <= 3; i++) {
            for (int j = i + 1; j <= 3; j++) {
                if (i != j) {
                    //System.out.println("Estoy en la pasada i:"+i+"j:"+j);
                    Partido partido = new Partido(0, dia, mes, anyo, hora, pistas.get(contPistas).getId(), "Grupo A");
                    bd.insertarEstructuraCompletaPartidoRegular(partido, equiposSorteados.get(i).getId(), equiposSorteados.get(j).getId());
                    //bd.insertarEstructuraCompletaPartidoRegular(partido, i, j);
                    if (contPistas < numPistas - 1) {
                        contPistas++;
                    } else {
                        contPistas = 0;
                        hora = hora + 2;
                        //creaeremos métodos para actualizar hora dia mes y año
                    }

                }
            }
        }
        for (int i = 4; i <= 7; i++) {
            for (int j = i + 1; j <= 7; j++) {
                if (i != j) {
                    //System.out.println("Estoy en la pasada i:"+i+"j:"+j);
                    Partido partido = new Partido(0, dia, mes, anyo, hora, pistas.get(contPistas).getId(), "Grupo B");
                    bd.insertarEstructuraCompletaPartidoRegular(partido, equiposSorteados.get(i).getId(), equiposSorteados.get(j).getId());
                    //bd.insertarEstructuraCompletaPartidoRegular(partido, i, j);
                    if (contPistas < numPistas - 1) {
                        contPistas++;
                    } else {
                        contPistas = 0;
                        hora = hora + 2;
                        //creaeremos métodos para actualizar hora dia mes y año
                    }

                }
            }
        }
        for (int i = 8; i <= 11; i++) {
            for (int j = i + 1; j <= 11; j++) {
                if (i != j) {
                    //System.out.println("Estoy en la pasada i:"+i+"j:"+j);
                    Partido partido = new Partido(0, dia, mes, anyo, hora, pistas.get(contPistas).getId(), "Grupo C");
                    bd.insertarEstructuraCompletaPartidoRegular(partido, equiposSorteados.get(i).getId(), equiposSorteados.get(j).getId());
                    //bd.insertarEstructuraCompletaPartidoRegular(partido, i, j);
                    if (contPistas < numPistas - 1) {
                        contPistas++;
                    } else {
                        contPistas = 0;
                        hora = hora + 2;
                        //creaeremos métodos para actualizar hora dia mes y año
                    }

                }
            }
        }
        for (int i = 12; i <= 15; i++) {
            for (int j = i + 1; j <= 15; j++) {
                if (i != j) {
                    //System.out.println("Estoy en la pasada i:"+i+"j:"+j);
                    Partido partido = new Partido(0, dia, mes, anyo, hora, pistas.get(contPistas).getId(), "Grupo D");
                    bd.insertarEstructuraCompletaPartidoRegular(partido, equiposSorteados.get(i).getId(), equiposSorteados.get(j).getId());
                    //bd.insertarEstructuraCompletaPartidoRegular(partido, i, j);
                    if (contPistas < numPistas - 1) {
                        contPistas++;
                    } else {
                        contPistas = 0;
                        hora = hora + 2;
                        //creaeremos métodos para actualizar hora dia mes y año
                    }

                }
            }
        }*/


        //--------------------------------

//Ahora vamos a crear la estructura del cuadro
        //Primero los cuartos de final: 4 partidos que enfrentan a los dos mejores de cada grupo
        hora = 12;
        dia = 10;
        contPistas = 0;
        for (int i = 0; i < 4; i++) {
            Partido partido = new Partido(0, dia, mes, anyo, hora, pistas.get(contPistas).getId(), "Cuartos de Final");
            bd.insertarPartidoCuadro(partido);
            if (contPistas < numPistas - 1) {
                contPistas++;
            } else {
                contPistas = 0;
                hora = hora + 2;
            }
        }
        //Ahora creamos los partidos de semifinales: 2 partidos, primero se juega una y luego la otra
        //como son semifinales decidimos que tienen espectación y primero se juega una en la pista 0 y luego la otra en la misma pista
        hora = 12;
        dia = 11;
        for (int i = 0; i < 2; i++) {
            Partido partido = new Partido(0, dia, mes, anyo, hora, pistas.get(0).getId(), "Semifinales");
            bd.insertarPartidoCuadro(partido);
            hora = hora + 2;
        }
        //Creamos el partido correspondiente a la final
        hora = 12;
        dia = 12;
        Partido partido = new Partido(0, dia, mes, anyo, hora, pistas.get(0).getId(), "Final");
        bd.insertarPartidoCuadro(partido);

        //Es el momento de actualizar en la base de datos la posición del equipo en el Sorteo para recuperarla posteriormente en el proceso que computa los resultados de la fase regular
        for (int i = 0; i < equiposSorteados.size(); i++) {
            bd.actualizarPosicionSorteoEnEquipo(equiposSorteados.get(i).getId(), i);
        }

    }
    public static void procesarResultadosRegular(MySqlDao bd) {  //este procedimiento computa victorias y derrotas actualizando la puntuación en el ArrayList de equiposSorteados y posteriormente construye el arraylist de equiposClasificados para generar las entradas en la tabla partido_equipo correspondientes a cuartos de final
        int j;
        int diferenciaSets, diferenciaJuegosLocal, diferenciaJuegosVisitante;
        PartidoEquipo local, visitante;
        int idEquipoGanador, idEquipoPerdedor, contSetsGanadosLocal, contSetsGanadosVisitante;
        ArrayList<PartidoEquipo> partidosEquipos = bd.obtenerPartidoEquipoRegular();
        ArrayList<Equipo> equiposSorteados = bd.obtenerEquiposPorPosicionSorteo();
        for (int i = 0; i < partidosEquipos.size(); i = i + 2) {
            local = partidosEquipos.get(i);
            visitante = partidosEquipos.get(i + 1);
            contSetsGanadosLocal = 0;
            contSetsGanadosVisitante = 0;
            if (local.getJuegosS1() > visitante.getJuegosS1())
                contSetsGanadosLocal++;
            else
                contSetsGanadosVisitante++;
            diferenciaJuegosLocal = local.getJuegosS1()-visitante.getJuegosS1();
            diferenciaJuegosVisitante = visitante.getJuegosS1()-local.getJuegosS1();

            if (local.getJuegosS2() > visitante.getJuegosS2())
                contSetsGanadosLocal++;
            else
                contSetsGanadosVisitante++;
            diferenciaJuegosLocal = diferenciaJuegosLocal+local.getJuegosS2()-visitante.getJuegosS2();
            diferenciaJuegosVisitante = diferenciaJuegosVisitante+visitante.getJuegosS2()-local.getJuegosS2();

            if (local.getJuegosS3() > visitante.getJuegosS3())  //en este caso puede haber 0 0 en este set y por esto es diferente
                contSetsGanadosLocal++;
            else if (local.getJuegosS3() < visitante.getJuegosS3())
                contSetsGanadosVisitante++;

            diferenciaJuegosLocal = diferenciaJuegosLocal+local.getJuegosS3()-visitante.getJuegosS3();
            diferenciaJuegosVisitante = diferenciaJuegosVisitante+visitante.getJuegosS3()-local.getJuegosS3();

            diferenciaSets = Math.abs(contSetsGanadosLocal - contSetsGanadosVisitante);

            if (contSetsGanadosLocal > contSetsGanadosVisitante) {
                //localizar en el arrayList de sorteados el id del local y actulizar su puntuacion como ganador: puntos + 1 y diferencias en positivo
                for (j = 0; equiposSorteados.get(j).getId() != local.getIdEquipo(); j++){
                    System.out.println(equiposSorteados.get(j).getId());
                    System.out.println( local.getIdEquipo());
                }
                //localizo en equiposSorteados al elemento que te el Id del equipo local que en este if es el ganador
                equiposSorteados.get(j).setPuntos(equiposSorteados.get(j).getPuntos() + 1);
                equiposSorteados.get(j).setDiferenciaSets(equiposSorteados.get(j).getDiferenciaSets() + diferenciaSets);
                equiposSorteados.get(j).setDiferenciaJuegos(equiposSorteados.get(j).getDiferenciaJuegos() + diferenciaJuegosLocal);
                //localizar en el arrayList de sorteados el id del visitante y actulizar su puntuacion como perdedor: (añadir cero a puntos--> no hago nada con esto) y diferencias en negativo
                for (j = 0; equiposSorteados.get(j).getId() != visitante.getIdEquipo(); j++) ;
                equiposSorteados.get(j).setDiferenciaSets(equiposSorteados.get(j).getDiferenciaSets() - diferenciaSets);
                equiposSorteados.get(j).setDiferenciaJuegos(equiposSorteados.get(j).getDiferenciaJuegos() + diferenciaJuegosVisitante);
            } else {
                //localizar en el arrayList de sorteados el id del visitante y actulizar su puntuacion como ganador: puntos + 1 y diferencias en positivo
                for (j = 0; equiposSorteados.get(j).getId() != visitante.getIdEquipo(); j++){
                    System.out.println(equiposSorteados.get(j).getId());
                    System.out.println( visitante.getIdEquipo());
                }
/*                for (j = 0; equiposSorteados.get(j).getId() != visitante.getIdEquipo(); j++)
                    ;  //localizo en equiposSorteados al elemento que te el Id del equipo local que en este if es el ganador
    */          equiposSorteados.get(j).setPuntos(equiposSorteados.get(j).getPuntos() + 1);
                equiposSorteados.get(j).setDiferenciaSets(equiposSorteados.get(j).getDiferenciaSets() + diferenciaSets);
                equiposSorteados.get(j).setDiferenciaJuegos(equiposSorteados.get(j).getDiferenciaJuegos() + diferenciaJuegosVisitante);
                //localizar en el arrayList de sorteados el id del local y actulizar su puntuacion como perdedor: (añadir cero a puntos--> no hago nada con esto) y diferencias en negativo
                for (j = 0; equiposSorteados.get(j).getId() != local.getIdEquipo(); j++) ;
                equiposSorteados.get(j).setDiferenciaSets(equiposSorteados.get(j).getDiferenciaSets() - diferenciaSets);
                equiposSorteados.get(j).setDiferenciaJuegos(equiposSorteados.get(j).getDiferenciaJuegos() + diferenciaJuegosLocal);
            }
        }
//como punto de control vamos a visualizar la clasificación del grupoA
        System.out.println("Mostramos clasificación del Grupo A");
        for (int i = 0; i<4;i++)
            System.out.println(equiposSorteados.get(i).toString());

        //vamos con la construcción del ArrayList de Clasificados
        ArrayList<Equipo> equiposClasificados = new ArrayList<>();
        int i = 1;
        int posPrimero = 0;
        while (i <= 3) {
            if (equiposSorteados.get(i).getPuntos() > equiposSorteados.get(posPrimero).getPuntos()) {
                posPrimero = i;
            } else if ((equiposSorteados.get(i).getPuntos() == equiposSorteados.get(posPrimero).getPuntos()) && (equiposSorteados.get(i).getDiferenciaSets() > equiposSorteados.get(posPrimero).getDiferenciaSets())) {
                posPrimero = i;
            } else if ((equiposSorteados.get(i).getDiferenciaSets() == equiposSorteados.get(posPrimero).getDiferenciaSets()) &&(equiposSorteados.get(i).getDiferenciaJuegos() > equiposSorteados.get(posPrimero).getDiferenciaJuegos())) {
                posPrimero = i;
            }
            i++;
        }
        //ya tengo un i la posición del array equiposSorteados que se corresponde con el primero del GrupoA
        int posSegundo;
        if (posPrimero == 0) {
            posSegundo = 1;
            i = 2;
        } else {
            posSegundo = 0;
            i = 1;
        }
        while (i <= 3) {
            if (i!=posPrimero) {
                if (equiposSorteados.get(i).getPuntos() > equiposSorteados.get(posPrimero).getPuntos()) {
                    posPrimero = i;
                } else if ((equiposSorteados.get(i).getPuntos() == equiposSorteados.get(posPrimero).getPuntos()) && (equiposSorteados.get(i).getDiferenciaSets() > equiposSorteados.get(posPrimero).getDiferenciaSets())) {
                    posPrimero = i;
                } else if ((equiposSorteados.get(i).getDiferenciaSets() == equiposSorteados.get(posPrimero).getDiferenciaSets()) && (equiposSorteados.get(i).getDiferenciaJuegos() > equiposSorteados.get(posPrimero).getDiferenciaJuegos())) {
                    posPrimero = i;
                }
            }
            i++;
        }
        //cuando llega aqui la lógica posPrimero refleja la posición del equipo entre las posiciones 0 y 3 de equiposSorteado que ha quedado primero y posSegundo la del que ha quedado Segundo
        equiposClasificados.add(equiposSorteados.get(posPrimero));  //se inserta en equiposClasificados el primer equipo del grupoA
        equiposClasificados.add(equiposSorteados.get(posSegundo));  //se inserta en equiposClasificados el segundo equipo del grupoA

//repito la lógica anterior una vez para cada uno de los siguiente grupos
        i = 5;
        posPrimero = 4;
        while (i <= 7) {
            if (equiposSorteados.get(i).getPuntos() > equiposSorteados.get(posPrimero).getPuntos()) {
                posPrimero = i;
            } else if ((equiposSorteados.get(i).getPuntos() == equiposSorteados.get(posPrimero).getPuntos()) && (equiposSorteados.get(i).getDiferenciaSets() > equiposSorteados.get(posPrimero).getDiferenciaSets())) {
                posPrimero = i;
            } else if ((equiposSorteados.get(i).getDiferenciaSets() == equiposSorteados.get(posPrimero).getDiferenciaSets()) &&(equiposSorteados.get(i).getDiferenciaJuegos() > equiposSorteados.get(posPrimero).getDiferenciaJuegos())) {
                posPrimero = i;
            }
            i++;
        }
        //ya tengo un i la posición del array equiposSorteados que se corresponde con el primero del GrupoA

        if (posPrimero == 4) {
            posSegundo = 5;
            i = 6;
        } else {
            posSegundo = 4;
            i = 5;
        }
        while (i <= 7) {
            if (i!=posPrimero) {
                if (equiposSorteados.get(i).getPuntos() > equiposSorteados.get(posPrimero).getPuntos()) {
                    posPrimero = i;
                } else if ((equiposSorteados.get(i).getPuntos() == equiposSorteados.get(posPrimero).getPuntos()) && (equiposSorteados.get(i).getDiferenciaSets() > equiposSorteados.get(posPrimero).getDiferenciaSets())) {
                    posPrimero = i;
                } else if ((equiposSorteados.get(i).getDiferenciaSets() == equiposSorteados.get(posPrimero).getDiferenciaSets()) && (equiposSorteados.get(i).getDiferenciaJuegos() > equiposSorteados.get(posPrimero).getDiferenciaJuegos())) {
                    posPrimero = i;
                }
            }
            i++;
        }
        //cuando llega aqui la lógica posPrimero refleja la posición del equipo entre las posiciones 4 y 7 de equiposSorteado que ha quedado primero y posSegundo la del que ha quedado Segundo
        equiposClasificados.add(equiposSorteados.get(posPrimero));  //se inserta en equiposClasificados el primer equipo del grupoA
        equiposClasificados.add(equiposSorteados.get(posSegundo));  //se inserta en equiposClasificados el segundo equipo del grupoA
//ahora con el grupo C
        i = 9;
        posPrimero = 8;
        while (i <= 11) {
            if (equiposSorteados.get(i).getPuntos() > equiposSorteados.get(posPrimero).getPuntos()) {
                posPrimero = i;
            } else if ((equiposSorteados.get(i).getPuntos() == equiposSorteados.get(posPrimero).getPuntos()) && (equiposSorteados.get(i).getDiferenciaSets() > equiposSorteados.get(posPrimero).getDiferenciaSets())) {
                posPrimero = i;
            } else if ((equiposSorteados.get(i).getDiferenciaSets() == equiposSorteados.get(posPrimero).getDiferenciaSets()) &&(equiposSorteados.get(i).getDiferenciaJuegos() > equiposSorteados.get(posPrimero).getDiferenciaJuegos())) {
                posPrimero = i;
            }
            i++;
        }
        //ya tengo un i la posición del array equiposSorteados que se corresponde con el primero del GrupoA
        if (posPrimero == 8) {
            posSegundo = 9;
            i = 10;
        } else {
            posSegundo = 8;
            i = 9;
        }
        while (i <= 11) {
            if (i!=posPrimero) {
                if (equiposSorteados.get(i).getPuntos() > equiposSorteados.get(posPrimero).getPuntos()) {
                    posPrimero = i;
                } else if ((equiposSorteados.get(i).getPuntos() == equiposSorteados.get(posPrimero).getPuntos()) && (equiposSorteados.get(i).getDiferenciaSets() > equiposSorteados.get(posPrimero).getDiferenciaSets())) {
                    posPrimero = i;
                } else if ((equiposSorteados.get(i).getDiferenciaSets() == equiposSorteados.get(posPrimero).getDiferenciaSets()) && (equiposSorteados.get(i).getDiferenciaJuegos() > equiposSorteados.get(posPrimero).getDiferenciaJuegos())) {
                    posPrimero = i;
                }
            }
            i++;
        }
        //cuando llega aqui la lógica posPrimero refleja la posición del equipo entre las posiciones 8 y 11 de equiposSorteado que ha quedado primero y posSegundo la del que ha quedado Segundo
        equiposClasificados.add(equiposSorteados.get(posPrimero));  //se inserta en equiposClasificados el primer equipo del grupoA
        equiposClasificados.add(equiposSorteados.get(posSegundo));  //se inserta en equiposClasificados el segundo equipo del grupoA
//ahora grupo D
        i = 13;
        posPrimero = 12;
        while (i <= 15) {
            if (equiposSorteados.get(i).getPuntos() > equiposSorteados.get(posPrimero).getPuntos()) {
                posPrimero = i;
            } else if ((equiposSorteados.get(i).getPuntos() == equiposSorteados.get(posPrimero).getPuntos()) && (equiposSorteados.get(i).getDiferenciaSets() > equiposSorteados.get(posPrimero).getDiferenciaSets())) {
                posPrimero = i;
            } else if ((equiposSorteados.get(i).getDiferenciaSets() == equiposSorteados.get(posPrimero).getDiferenciaSets()) &&(equiposSorteados.get(i).getDiferenciaJuegos() > equiposSorteados.get(posPrimero).getDiferenciaJuegos())) {
                posPrimero = i;
            }
            i++;
        }
        //ya tengo un i la posición del array equiposSorteados que se corresponde con el primero del GrupoA
        if (posPrimero == 12) {
            posSegundo = 13;
            i = 14;
        } else {
            posSegundo = 12;
            i = 13;
        }
        while (i <= 15){
            if (i!=posPrimero) {
                if (equiposSorteados.get(i).getPuntos() > equiposSorteados.get(posPrimero).getPuntos()) {
                    posPrimero = i;
                } else if ((equiposSorteados.get(i).getPuntos() == equiposSorteados.get(posPrimero).getPuntos()) && (equiposSorteados.get(i).getDiferenciaSets() > equiposSorteados.get(posPrimero).getDiferenciaSets())) {
                    posPrimero = i;
                } else if ((equiposSorteados.get(i).getDiferenciaSets() == equiposSorteados.get(posPrimero).getDiferenciaSets()) && (equiposSorteados.get(i).getDiferenciaJuegos() > equiposSorteados.get(posPrimero).getDiferenciaJuegos())) {
                    posPrimero = i;
                }
            }
            i++;
        }
        //cuando llega aqui la lógica posPrimero refleja la posición del equipo entre las posiciones 12 y 15 de equiposSorteado que ha quedado primero y posSegundo la del que ha quedado Segundo
        equiposClasificados.add(equiposSorteados.get(posPrimero));  //se inserta en equiposClasificados el primer equipo del grupoA
        equiposClasificados.add(equiposSorteados.get(posSegundo));  //se inserta en equiposClasificados el segundo equipo del grupoA

        //TEMPORAL: SOLO PARA VERIFICAR
        System.out.println("Aqui viene la clasificación del Grupo A");
        for(j=0; j<4; j++)
            System.out.println(equiposSorteados.get(j).toString());
        for(j=0; j<2; j++)
            System.out.println(equiposClasificados.get(j).toString());

        System.out.println("Esta son los equipos clasificados: 1 y 2 de cada grupo A, B, C y D respectivamente");
        for(i=0; i<equiposClasificados.size(); i++)
            System.out.println(equiposClasificados.get(i).toString());

//ahora nos toca consultar los id de los partidos de cuartos y hacer las inserciones en la tabla partido_equipo de los cruces correspondiente a la equiposClasificados que acabamos de construir
        ArrayList<Partido> partidosCuartos = bd.obtenerPartidosCuadro("Cuartos de final");
        bd.insertarPartidoEquipoCuadro(partidosCuartos.get(0).getId(), equiposClasificados.get(0).getId(), equiposClasificados.get(3).getId());
        bd.insertarPartidoEquipoCuadro(partidosCuartos.get(1).getId(), equiposClasificados.get(2).getId(), equiposClasificados.get(1).getId());
        bd.insertarPartidoEquipoCuadro(partidosCuartos.get(2).getId(), equiposClasificados.get(4).getId(), equiposClasificados.get(7).getId());
        bd.insertarPartidoEquipoCuadro(partidosCuartos.get(3).getId(), equiposClasificados.get(6).getId(), equiposClasificados.get(5).getId());
    }
    public static void procesarResultadosCuartos(MySqlDao bd){ //se encarga de evaluar quien ha ganado cada cruce de cuartos e insertarlos de dos en partido_equipo para estar lista la estructura en la base de datos para insertar los resultados
        int j;
        int diferenciaSets;
        PartidoEquipo local, visitante;
        int idEquipoGanador, idEquipoPerdedor, contSetsGanadosLocal, contSetsGanadosVisitante;
        ArrayList<PartidoEquipo> partidosEquipos = bd.obtenerPartidoEquipoCuadro("Cuartos de final");
        ArrayList<Integer> clasificadosSemifinales = new ArrayList<>();
        for(int i=0; i<partidosEquipos.size();i=i+2){
            local = partidosEquipos.get(i);
            visitante = partidosEquipos.get(i+1);

            contSetsGanadosLocal = 0;
            contSetsGanadosVisitante = 0;
            if (local.getJuegosS1() > visitante.getJuegosS1())
                contSetsGanadosLocal++;
            else
                contSetsGanadosVisitante++;
            if (local.getJuegosS2() > visitante.getJuegosS2())
                contSetsGanadosLocal++;
            else
                contSetsGanadosVisitante++;

            if (local.getJuegosS3() > visitante.getJuegosS3())  //en este caso puede haber 0 0 en este set y por esto es diferente
                contSetsGanadosLocal++;
            else if (local.getJuegosS3() < visitante.getJuegosS3())
                contSetsGanadosVisitante++;

            if (contSetsGanadosLocal>contSetsGanadosVisitante)
                clasificadosSemifinales.add(local.getIdEquipo());
            else
                clasificadosSemifinales.add(visitante.getIdEquipo());
        }

        ArrayList<Partido> partidosSemis = bd.obtenerPartidosCuadro("Semifinales");
        bd.insertarPartidoEquipoCuadro(partidosSemis.get(0).getId(), clasificadosSemifinales.get(0), clasificadosSemifinales.get(1));
        bd.insertarPartidoEquipoCuadro(partidosSemis.get(1).getId(), clasificadosSemifinales.get(2), clasificadosSemifinales.get(3));
    }
    public static void procesarResultadosSemifinales(MySqlDao bd){ //se encarga de evaluar quien ha ganado cada cruce de cuartos e insertarlos de dos en partido_equipo para estar lista la estructura en la base de datos para insertar los resultados
        int j;
        int diferenciaSets;
        PartidoEquipo local, visitante;
        int idEquipoGanador, idEquipoPerdedor, contSetsGanadosLocal, contSetsGanadosVisitante;
        ArrayList<PartidoEquipo> partidosEquipos = bd.obtenerPartidoEquipoCuadro("Semifinales");
        ArrayList<Integer> clasificadosFinal = new ArrayList<>();
        for(int i = 0; i<partidosEquipos.size(); i = i + 2) {
            local = partidosEquipos.get(i);
            visitante = partidosEquipos.get(i + 1);

            contSetsGanadosLocal = 0;
            contSetsGanadosVisitante = 0;
            if (local.getJuegosS1() > visitante.getJuegosS1())
                contSetsGanadosLocal++;
            else
                contSetsGanadosVisitante++;
            if (local.getJuegosS2() > visitante.getJuegosS2())
                contSetsGanadosLocal++;
            else
                contSetsGanadosVisitante++;

            if (local.getJuegosS3() > visitante.getJuegosS3())  //en este caso puede haber 0 0 en este set y por esto es diferente
                contSetsGanadosLocal++;
            else if (local.getJuegosS3() < visitante.getJuegosS3())
                contSetsGanadosVisitante++;

            if (contSetsGanadosLocal > contSetsGanadosVisitante)
                clasificadosFinal.add(local.getIdEquipo());
            else
                clasificadosFinal.add(visitante.getIdEquipo());
        }
        ArrayList<Partido> partidos= bd.obtenerPartidosCuadro("Final");
        bd.insertarPartidoEquipoCuadro(partidos.get(0).getId(), clasificadosFinal.get(0), clasificadosFinal.get(1));

    }
}