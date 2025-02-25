package controlador;

import modelo.*;

import java.sql.*;
import java.util.ArrayList;

public class MySqlDao {
    private Connection conexion;

    public MySqlDao() {
        String url = "jdbc:mysql://localhost/";
        String usuario = "root";
        String password = "";

        try {
            conexion = DriverManager.getConnection(url, usuario, password);
            inicializarBaseDeDatos(); //llamamos al metodo despues de iniciar la conexion
        } catch (SQLException sqlE) {
            System.out.println("Error al intentar conectar con la base de datos");
        }
    }

    private void inicializarBaseDeDatos() {
        // Sentencias SQL para crear la base de datos y las tablas si no existen
        String[] sentenciasSQL = {
                "CREATE DATABASE IF NOT EXISTS torneo",
                "USE torneo",
                "CREATE TABLE IF NOT EXISTS equipos (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "nombre VARCHAR(255)," +
                        "puntos INT," +
                        "diferencia_sets INT," +
                        "diferencia_juegos INT," +
                        "posicion_sorteo INT" +
                        ")",
                "CREATE TABLE IF NOT EXISTS jugadores (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "dni VARCHAR(255)," +
                        "nombre VARCHAR(255)," +
                        "id_equipo INT," +
                        "FOREIGN KEY (id_equipo) REFERENCES equipos(id)" +
                        ")",
                "CREATE TABLE IF NOT EXISTS pista (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "nombre VARCHAR(255)" +
                        ")",
                "CREATE TABLE IF NOT EXISTS partido (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "dia INT," +
                        "mes INT," +
                        "anyo INT," +
                        "hora INT," +
                        "id_pista INT," +
                        "denominacion VARCHAR(255)," +
                        "FOREIGN KEY (id_pista) REFERENCES pista(id)" +
                        ")",
                "CREATE TABLE IF NOT EXISTS partido_equipo (" +
                        "id_partido INT," +
                        "id_equipo INT," +
                        "juegosS1 INT," +
                        "juegosS2 INT," +
                        "juegosS3 INT," +
                        "PRIMARY KEY (id_partido, id_equipo)," +
                        "FOREIGN KEY (id_partido) REFERENCES partido(id)," +
                        "FOREIGN KEY (id_equipo) REFERENCES equipos(id)" +
                        ")"
        };

        try {
            Statement stmt = conexion.createStatement();
            // Ejecutar cada sentencia SQL para crear la base de datos y las tablas
            for (String sql : sentenciasSQL) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }

    public ArrayList<Equipo> obtenerEquipos() {
        int id;
        String nombre;
        int puntos, diferenciaSets, diferenciaJuegos, posicionSorteo;
        String sql = "select * from equipos order by id;";
        ArrayList<Equipo> equipos = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("id");
                nombre = rs.getString("nombre");
                puntos = rs.getInt("puntos");
                diferenciaSets = rs.getInt("diferencia_sets");
                diferenciaJuegos = rs.getInt("diferencia_juegos");
                posicionSorteo = rs.getInt("posicion_sorteo");

                Equipo equipo = new Equipo(id, nombre, puntos, diferenciaSets, diferenciaJuegos, posicionSorteo);
                equipos.add(equipo);
            }
            rs.close();
            stmt.close();
            return equipos;
        } catch (SQLException e) {
            System.out.println("se produjo un error al consultar los equipos");
            return null;
        }
    }

    public boolean insertarEquipo(Equipo equipo) {
        PreparedStatement ps;
        String sql = "insert into equipos values (NULL,?, 0,0,0,0)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, equipo.getNombre());
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1)
                return true;
            else
                return false;
        } catch (SQLException sqlE) {
            System.out.println("error al insertar el equipo en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public boolean modificarEquipo(Equipo equipo) {
        PreparedStatement ps;
        String sql = "update equipos set nombre=? where id=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, equipo.getNombre());
            ps.setInt(2, equipo.getId());
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1)
                return true;
            else
                return false;
        } catch (SQLException sqlE) {
            System.out.println("error al modificar el equipo en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public boolean borrarEquipo(int id) {
        PreparedStatement ps;
        String sql = "delete from equipos where id=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1)
                return true;
            else
                return false;
        } catch (SQLException sqlE) {
            System.out.println("error al borrar el equipo en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public ArrayList<Jugador> obtenerJugadores() {
        int id;
        String dni;
        String nombre;
        int idEquipo;
        String sql = "select * from jugadores order by id;";
        ArrayList<Jugador> jugadores = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("id");
                dni = rs.getString("dni");
                nombre = rs.getString("nombre");
                idEquipo = rs.getInt("id_equipo");

                Jugador jugador = new Jugador(id, dni, nombre, idEquipo);
                jugadores.add(jugador);
            }
            rs.close();
            stmt.close();
            return jugadores;
        } catch (SQLException e) {
            System.out.println("se produjo un error al consultar los jugadores");
            return null;
        }

    }

    public boolean insertarJugador(Jugador jugador) {
        PreparedStatement ps;
        String sql = "insert into jugadores values (?,?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, jugador.getDni());
            ps.setString(2, jugador.getNombre());
            ps.setInt(3, jugador.getIdEquipo());
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 1)
                return true;
            else
                return false;

        } catch (SQLException sqlE) {
            System.out.println("error al insertar los datos del jugador en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public boolean modificarJugador(Jugador jugador) {
        PreparedStatement ps;
        String sql = "update jugadores set dni=?, nombre=?, id_equipo=? where id=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, jugador.getDni());
            ps.setString(2, jugador.getNombre());
            ps.setInt(3, jugador.getIdEquipo());
            ps.setInt(4, jugador.getId());
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1)
                return true;
            else
                return false;
        } catch (SQLException sqlE) {
            System.out.println("error al modificar el jugador en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public boolean borrarJugador(int id) {
        PreparedStatement ps;
        String sql = "delete from jugadores where id=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1)
                return true;
            else
                return false;
        } catch (SQLException sqlE) {
            System.out.println("error al borrar el jugador en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public ArrayList<Pista> obtenerPistas() {
        int id;
        String nombre;
        String sql = "select * from pista;";
        ArrayList<Pista> pistas = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("id");
                nombre = rs.getString("nombre");
                Pista pista = new Pista(id, nombre);
                pistas.add(pista);
            }
            rs.close();
            stmt.close();
            return pistas;
        } catch (SQLException e) {
            System.out.println("Se ha producido un error al consultar las pistas");
            return null;
        }
    }

    public boolean insertarPista(Pista pista) {
        PreparedStatement ps;
        String sql = "insert into pista values (NULL, ?)";

        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, pista.getNombre());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1)
                return true;
            else
                return false;

        } catch (SQLException sqlE) {
            System.out.println("Error al insertar una modelo.Pista en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    //modificar pista
    public boolean modificarPista(Pista pista) {
        PreparedStatement ps;
        String sql = "update pista set nombre=? where id=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, pista.getNombre());
            ps.setInt(2, pista.getId());
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1)
                return true;
            else
                return false;
        } catch (SQLException sqlE) {
            System.out.println("error al modificar la pista en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    //borrar pista
    public boolean borrarPista(int id) {
        PreparedStatement ps;
        String sql = "delete from pista where id=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1)
                return true;
            else
                return false;
        } catch (SQLException sqlE) {
            System.out.println("error al borrar la pista en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public boolean actualizarEquipoJugador(String dniJugador, int idEquipo) {
        PreparedStatement ps;
        String sql = "update  jugadores  set id_equipo = ? where dni=?;";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, dniJugador);
            ps.setInt(2, idEquipo);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 1)
                return true;
            else
                return false;

        } catch (SQLException sqlE) {
            System.out.println("error al actualizar el equipo al jugador");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }


    public ArrayList<Partido> obtenerPartidosRegular() {
        int id, dia, mes, anyo, hora;
        int id_pista;
        String denominacion;
        String dni;
        String nombre;
        int idEquipos;
        String sql = "select * from partido where denominacion like 'Grupo%' ;";
        ArrayList<Partido> partidos = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                id = rs.getInt("id");
                dia = rs.getInt("dia");
                mes = rs.getInt("mes");
                anyo = rs.getInt("anyo");
                hora = rs.getInt("hora");
                id_pista = rs.getInt("id_pista");
                denominacion = rs.getString("denominacion");

                Partido partido = new Partido(id, dia, mes, anyo, hora, id_pista, denominacion);

                partidos.add(partido);

            }
            rs.close();
            stmt.close();
            return partidos;
        } catch (SQLException e) {
            System.out.println("Se ha producido un error al consultar los partidos existentes");
            System.out.println(e.getMessage());
            return null;

        }

    }

    public ArrayList<PuntuacionEquipoPartido> obtenerPuntuacionEquiposPartido(int idPartido) {
        int juegosS1, juegosS2, juegosS3, idEquipo;
        String nombreEquipo;
        String sql = "SELECT equipos.id, equipos.nombre, partido_equipo.juegosS1, partido_equipo.juegosS2, partido_equipo.juegosS3 from equipos, partido_equipo, partido where partido.id=partido_equipo.id_partido and partido_equipo.id_equipo=equipos.id and partido.id=" + idPartido + ";";
        ArrayList<PuntuacionEquipoPartido> puntuacionesEquipos = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                juegosS1 = rs.getInt("partido_equipo.juegosS1");
                juegosS2 = rs.getInt("partido_equipo.juegosS2");
                juegosS3 = rs.getInt("partido_equipo.juegosS3");

                idEquipo = rs.getInt("equipos.id");
                nombreEquipo = rs.getString("equipos.nombre");
                PuntuacionEquipoPartido puntuacionEquipo = new PuntuacionEquipoPartido(idPartido, idEquipo, nombreEquipo, juegosS1, juegosS2, juegosS3);
                puntuacionesEquipos.add(puntuacionEquipo);
            }
            rs.close();
            stmt.close();
            return puntuacionesEquipos;
        } catch (SQLException e) {
            System.out.println("se produjo un error al consultar los equipos y sus puntuaciones del partido con id: " + idPartido);
            return null;
        }
    }

    public ArrayList<Equipo> obtenerEquiposPorPosicionSorteo() {
        int id;
        String nombre;
        int puntos, diferenciaSets, diferenciaJuegos, posicionSorteo;
        String sql = "select * from equipos order by posicion_sorteo;";
        ArrayList<Equipo> equipos = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("id");
                nombre = rs.getString("nombre");
                puntos = rs.getInt("puntos");
                diferenciaSets = rs.getInt("diferencia_sets");
                diferenciaJuegos = rs.getInt("diferencia_juegos");
                posicionSorteo = rs.getInt("posicion_sorteo");

                Equipo equipo = new Equipo(id, nombre, puntos, diferenciaSets, diferenciaJuegos, posicionSorteo);
                equipos.add(equipo);
            }
            rs.close();
            stmt.close();
            return equipos;
        } catch (SQLException e) {
            System.out.println("se produjo un error al consultar los equipos");
            System.out.println(e.getMessage());
            ;
            return null;
        }

    }

    public boolean actualizarPosicionSorteoEnEquipo(int idEquipo, int posicionSorteo) {
        PreparedStatement ps;
        String sql = "update  equipos  set posicion_sorteo = ? where id=?;";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, posicionSorteo);
            ps.setInt(2, idEquipo);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 1)
                return true;
            else
                return false;

        } catch (SQLException sqlE) {
            System.out.println("error al actualizar la posición del equipo en el Sorteo");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }


    public boolean insertarEstructuraCompletaPartidoRegular(Partido partido, int idEquipo1, int idEquipo2) {
        //recibe todos los datos generícos de una partido así como de los dos equipos que se enfrentan
        //empezamos insertando los datos en la tabla partido
        int idPartidoRecienInsertado = 0;
        boolean insercionCorrecta;
        PreparedStatement ps;
        String sql = "insert into partido values (NULL, ?,?,?,?,?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            // ps.setDate(1, partido.getInicio());
            ps.setInt(1, partido.getDia());
            ps.setInt(2, partido.getMes());
            ps.setInt(3, partido.getAnyo());
            ps.setInt(4, partido.getHora());
            ps.setInt(5, partido.getIdPista());
            ps.setString(6, partido.getDenominacion());
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 1)
                insercionCorrecta = true;
            else
                insercionCorrecta = false;

        } catch (SQLException sqlE) {
            System.out.println("error al insertar los datos del partido de fase regular en la base de datos");
            System.out.println(sqlE.getMessage());
            insercionCorrecta = false;
        }
        if (insercionCorrecta) {  //si ha ido bien la inserción del partido ahora inserto a los equipos en partido_equipo pero antes necesitamos conocer que id le ha dado la base de datos al id de la tabla partido

            sql = "select id from partido order by id desc;";

            try {
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                idPartidoRecienInsertado = rs.getInt("id");
                System.out.println("Voy a insertar los equipos con el id de partido: " + idPartidoRecienInsertado);

            } catch (SQLException e) {
                System.out.println("se produjo un error al recuperar el id del partido recien insertado");
                System.out.println(e.getMessage());
            }


            try {
                sql = "insert into partido_equipo value (?,?, NULL, NULL, NULL)";
                ps = conexion.prepareStatement(sql);
                ps.setInt(1, idPartidoRecienInsertado);
                ps.setInt(2, idEquipo1);

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas == 1)
                    insercionCorrecta = true;
                else
                    insercionCorrecta = false;

            } catch (SQLException sqlE) {
                System.out.println("error al insertar los datos del primer equipo en la tabla partido_equipo");
                System.out.println(sqlE.getMessage());
                insercionCorrecta = false;
            }
            if (insercionCorrecta) {
                try {
                    sql = "insert into partido_equipo value (?,?, NULL, NULL, NULL)";
                    ps = conexion.prepareStatement(sql);
                    ps.setInt(1, idPartidoRecienInsertado);
                    ps.setInt(2, idEquipo2);

                    int filasAfectadas = ps.executeUpdate();
                    if (filasAfectadas == 1)
                        insercionCorrecta = true;
                    else
                        insercionCorrecta = false;

                } catch (SQLException sqlE) {
                    System.out.println("error al insertar los datos del primer equipo en la tabla partido_equipo");
                    System.out.println(sqlE.getMessage());
                    insercionCorrecta = false;
                }
            }
        }
        return insercionCorrecta;
    }

    public boolean insertarPartidoCuadro(Partido partido) {
        //recibe todos los datos generícos de una partido, pero esta vez desconocemos a los contrincantes
        //porque dependen de como queden los enfrentamiento anteriores
        PreparedStatement ps;
        String sql = "insert into partido  values (NULL, ?,?,?,?,?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            // ps.setDate(1, partido.getInicio());
            ps.setInt(1, partido.getDia());
            ps.setInt(2, partido.getMes());
            ps.setInt(3, partido.getAnyo());
            ps.setInt(4, partido.getHora());
            ps.setInt(5, partido.getIdPista());
            ps.setString(6, partido.getDenominacion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 1)
                return true;
            else
                return false;

        } catch (SQLException sqlE) {
            System.out.println("error al insertar los datos del partido de fase de Cuadro");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public boolean insertarEquiposPartidoCuadro(int idPartido, int idEquipo1, int idEquipo2) {
        //recibe todos los datos generícos de una partido así como de los dos equipos que se enfrentan
        //empezamos insertando los datos en la tabla partido
        boolean insercionCorrecta = true;
        PreparedStatement ps;
        String sql;

        if (insercionCorrecta) {  //si ha ido bien la inserción del partido ahora inserto a los equipos en partido_equipo
            try {
                sql = "insert into partido_equipo value (?,?, NULL, NULL, NULL)";
                ps = conexion.prepareStatement(sql);
                ps.setInt(1, idPartido);
                ps.setInt(2, idEquipo1);

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas == 1)
                    insercionCorrecta = true;
                else
                    insercionCorrecta = false;

            } catch (SQLException sqlE) {
                System.out.println("error al insertar los datos del primer equipo en la tabla partido_equipo");
                System.out.println(sqlE.getMessage());
                insercionCorrecta = false;
            }
            if (insercionCorrecta) {
                try {
                    sql = "insert into partido_equipo value (?,?, NULL, NULL, NULL)";
                    ps = conexion.prepareStatement(sql);
                    ps.setInt(1, idPartido);
                    ps.setInt(2, idEquipo2);

                    int filasAfectadas = ps.executeUpdate();
                    if (filasAfectadas == 1)
                        insercionCorrecta = true;
                    else
                        insercionCorrecta = false;

                } catch (SQLException sqlE) {
                    System.out.println("error al insertar los datos del primer equipo en la tabla partido_equipo");
                    System.out.println(sqlE.getMessage());
                    insercionCorrecta = false;
                }
            }
        }
        return insercionCorrecta;
    }

    public boolean actualizarPuntuacionEquipoPartido(PartidoEquipo partidoEquipo) {
        PreparedStatement ps;
        String sql = "update  partido_equipo  set juegosS1=?, juegosS2=?, juegosS3=? where id_partido=? and id_equipo=?;";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, partidoEquipo.getJuegosS1());
            ps.setInt(2, partidoEquipo.getJuegosS2());
            ps.setInt(3, partidoEquipo.getJuegosS3());
            ps.setInt(4, partidoEquipo.getIdPartido());
            ps.setInt(5, partidoEquipo.getIdEquipo());
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 1)
                return true;
            else
                return false;

        } catch (SQLException sqlE) {
            System.out.println("error al actualizar la info del partido_equipo");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }

    public ArrayList<PartidoEquipo> obtenerPartidoEquipoRegular() {
        int idPartido, idEquipo, juegosS1, juegosS2, juegosS3;
        String sql = "select * from partido_equipo order by id_partido;";
        ArrayList<PartidoEquipo> partidoEquipos = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                idPartido = rs.getInt("id_partido");
                idEquipo = rs.getInt("id_equipo");
                juegosS1 = rs.getInt("juegosS1");
                juegosS2 = rs.getInt("juegosS2");
                juegosS3 = rs.getInt("juegosS3");


                PartidoEquipo partidoEquipo = new PartidoEquipo(idPartido, idEquipo, juegosS1, juegosS2, juegosS3);

                partidoEquipos.add(partidoEquipo);

            }
            rs.close();
            stmt.close();
            return partidoEquipos;
        } catch (SQLException e) {
            System.out.println("Se ha producido un error al consultar la puntuación de los partidos de la fase regular");
            System.out.println(e.getMessage());
            return null;

        }

    }

    public ArrayList<Partido> obtenerPartidosCuadro(String ronda) {
        int id, dia, mes, anyo, hora;
        int id_pista;
        String denominacion;

        String sql = "select * from partido where denominacion='" + ronda + "';  ";
        ArrayList<Partido> partidos = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                id = rs.getInt("id");
                dia = rs.getInt("dia");
                mes = rs.getInt("mes");
                anyo = rs.getInt("anyo");
                hora = rs.getInt("hora");
                id_pista = rs.getInt("id_pista");
                denominacion = rs.getString("denominacion");

                Partido partido = new Partido(id, dia, mes, anyo, hora, id_pista, denominacion);

                partidos.add(partido);

            }
            rs.close();
            stmt.close();
            return partidos;
        } catch (SQLException e) {
            System.out.println("Se ha producido un error al consultar los partidos de Cuartos");
            System.out.println(e.getMessage());
            return null;

        }

    }

    public boolean insertarPartidoEquipoCuadro(int idPartido, int idEquipo1, int idEquipo2) {
        //recibe el idPartido de cuartos y los id de los dos equipos que se enfretarán
        String sql;
        PreparedStatement ps;
        boolean insercionCorrecta = false;
        try {
            sql = "insert into partido_equipo value (?,?, NULL, NULL, NULL)";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPartido);
            ps.setInt(2, idEquipo1);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 1)
                insercionCorrecta = true;
            else
                insercionCorrecta = false;

        } catch (SQLException sqlE) {
            System.out.println("error al insertar los datos del primer equipo en la tabla partido_equipo correspondiente a Cuartos De Final");
            System.out.println(sqlE.getMessage());
            insercionCorrecta = false;
        }
        if (insercionCorrecta) {
            try {
                sql = "insert into partido_equipo value (?,?, NULL, NULL, NULL)";
                ps = conexion.prepareStatement(sql);
                ps.setInt(1, idPartido);
                ps.setInt(2, idEquipo2);

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas == 1)
                    insercionCorrecta = true;
                else
                    insercionCorrecta = false;

            } catch (SQLException sqlE) {
                System.out.println("error al insertar los datos del segundo equipo en la tabla partido_equipo de cruce de cuartos");
                System.out.println(sqlE.getMessage());
                insercionCorrecta = false;
            }
        }
        return insercionCorrecta;
    }

    public ArrayList<PartidoEquipo> obtenerPartidoEquipoCuadro(String ronda) {
        int idPartido, idEquipo, juegosS1, juegosS2, juegosS3;
        String sql = "SELECT pe.id_partido, pe.id_equipo, pe.juegosS1, pe.juegosS2, pe.juegosS3 from partido_equipo pe, partido p where pe.id_partido=p.id and p.denominacion='" + ronda + "' order by p.id;";
        ArrayList<PartidoEquipo> partidoEquipos = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                idPartido = rs.getInt("pe.id_partido");
                idEquipo = rs.getInt("pe.id_equipo");
                juegosS1 = rs.getInt("pe.juegosS1");
                juegosS2 = rs.getInt("pe.juegosS2");
                juegosS3 = rs.getInt("pe.juegosS3");


                PartidoEquipo partidoEquipo = new PartidoEquipo(idPartido, idEquipo, juegosS1, juegosS2, juegosS3);

                partidoEquipos.add(partidoEquipo);

            }
            rs.close();
            stmt.close();
            return partidoEquipos;
        } catch (SQLException e) {
            System.out.println("Se ha producido un error al consultar la puntuación de los partidos de la fase de cuartos");
            System.out.println(e.getMessage());
            return null;

        }

    }

    //borrar torneo: partidos y partidosEquipos
    public boolean borrarTorneo() {
        PreparedStatement ps;
        String sql = "delete from partido_equipo;";
        try {
            ps = conexion.prepareStatement(sql);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas >= 0) {
                sql = "delete from partido;";
                ps = conexion.prepareStatement(sql);
                filasAfectadas = ps.executeUpdate();
                if (filasAfectadas >= 0)
                    return true;
                else
                    return false;
            } else
                return false;
        } catch (SQLException sqlE) {
            System.out.println("error al borrar el torneo en la base de datos");
            System.out.println(sqlE.getMessage());
            return false;
        }
    }
}
