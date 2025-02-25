package vista;

import controlador.MySqlDao;
import controlador.TorneoProcesado;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PartidosPanel extends JPanel {
    private MySqlDao bd;
    private DefaultListModel<PartidoListModel> listModel;
    private JList<PartidoListModel> list;
    private JFormattedTextField set1LocalField;
    private JFormattedTextField set2LocalField;
    private JFormattedTextField set3LocalField;
    private JFormattedTextField set1VisitanteField;
    private JFormattedTextField set2VisitanteField;
    private JFormattedTextField set3VisitanteField;
    private PartidoListModel seleccionado;

    private JButton crearTorneoButton;
    private JButton borrarTorneoButton;
    private JButton rellenarButton;
    private JButton faseActiva;
    private JButton puntuarButton;
    private JButton cancelarButton;
    private JButton procesarButton;
    private ArrayList<Equipo> equipos;
    private ArrayList<Pista> pistas;
    //combo para seleccionar la fase de los partidos
    private JComboBox<String> faseComboBox;
    private boolean faseCompleta;

    public PartidosPanel(MySqlDao bd) {
        this.bd = bd;
        setLayout(new BorderLayout());

        // Componentes para el formulario
        JPanel formularioPanel = new JPanel();
        formularioPanel.setLayout(new BoxLayout(formularioPanel, BoxLayout.Y_AXIS));
        set1LocalField = new JFormattedTextField();
        set2LocalField = new JFormattedTextField();
        set3LocalField = new JFormattedTextField();
        set1VisitanteField = new JFormattedTextField();
        set2VisitanteField = new JFormattedTextField();
        set3VisitanteField = new JFormattedTextField();

        crearTorneoButton = new JButton("Crear Torneo");
        borrarTorneoButton = new JButton("Borrar Torneo");
        puntuarButton = new JButton("Puntuar");
        cancelarButton = new JButton("Cancelar");
        rellenarButton = new JButton("Rellenar Aleatoriamente");
        procesarButton = new JButton("Procesar");

        faseComboBox = new JComboBox<>();
        faseComboBox.addItem("Regular");
        faseComboBox.addItem("Cuartos de final");
        faseComboBox.addItem("Semifinales");
        faseComboBox.addItem("Final");

        //agregamos funcionalidad al botón de crear
        crearTorneoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TorneoProcesado.crearEstructuraPartidos(bd);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                cargarLista();

            }
        });

        //agregamos funcionalidad al botón de cancelar
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarSeleccion();
            }
        });

        //agregamos funcionalidad al botón de modificar
        puntuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puntuarPartido();
            }
        });

        //agregamos funcionalidad al botón de borrar
        borrarTorneoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //preguntamos antes de borrar
                int respuesta = JOptionPane.showConfirmDialog(PartidosPanel.this, "¿Estás seguro de que quieres borrar el Partido?");
                if (respuesta != JOptionPane.YES_OPTION) {
                    return;
                }
                bd.borrarTorneo();
                cargarLista();
            }
        });

        rellenarButton.addActionListener(new ActionListener() {
            @Override


            //-------
            public void actionPerformed(ActionEvent e) {
                //rellenamos aleatoriamente la puntuacion de los partidos y los recogemos por iterador listModelRegular.elements().asIterator()
                for (int i = 0; i < listModel.size(); i++) {
                    PartidoListModel partido = listModel.getElementAt(i);
                    //hasta qeu sea valida
                    int set1Local, set2Local, set3Local, set1Visitante, set2Visitante, set3Visitante;
                    do {
                        //calculamos si 2 o 3 set´s
                        int numSets = (int) (Math.random() * 2) + 2;
                        if (numSets == 2) {
                            set3Local = 0;
                            set3Visitante = 0;
                        }else{
                            set3Local = (int) (Math.random() * 8);
                            set3Visitante = (int) (Math.random() * 8);
                        }

                        set1Local = (int) (Math.random() * 8);
                        set2Local = (int) (Math.random() * 8);
                        set1Visitante = (int) (Math.random() * 8);
                        set2Visitante = (int) (Math.random() * 8);

                        //repetimos hasta que sea válida la puntuación del partido
                    } while (validarPuntuacion(set1Local, set2Local, set3Local, set1Visitante, set2Visitante, set3Visitante));
                    PartidoEquipo partidoEquipoLocal = new PartidoEquipo(partido.getPartido().getId(), partido.getPuntuacionEquipoPartido1().getIdEquipo(), set1Local, set2Local, set3Local);
                    PartidoEquipo partidoEquipoVisitante = new PartidoEquipo(partido.getPartido().getId(), partido.getPuntuacionEquipoPartido2().getIdEquipo(), set1Visitante, set2Visitante, set3Visitante);
                    bd.actualizarPuntuacionEquipoPartido(partidoEquipoLocal);
                    bd.actualizarPuntuacionEquipoPartido(partidoEquipoVisitante);
                }

                cargarLista();
            }
        });

        procesarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (faseComboBox.getSelectedIndex()) {
                    case 0:
                        TorneoProcesado.procesarResultadosRegular(bd);
                        break;
                    case 1:
                        TorneoProcesado.procesarResultadosCuartos(bd);
                        break;
                    case 2:
                        TorneoProcesado.procesarResultadosSemifinales(bd);
                        break;
                    default:
                        //mostramos el ganador calculando el resultado final
                        PuntuacionEquipoPartido puntuacion1 = listModel.get(0).getPuntuacionEquipoPartido1();
                        PuntuacionEquipoPartido puntuacion2 = listModel.get(0).getPuntuacionEquipoPartido2();
                        int setsLocal = 0;
                        int setsVisitante = 0;
                        if (puntuacion1.getJuegosS1() > puntuacion2.getJuegosS1()) {
                            setsLocal++;
                        } else {
                            setsVisitante++;
                        }
                        if (puntuacion1.getJuegosS2() > puntuacion2.getJuegosS2()) {
                            setsLocal++;
                        } else {
                            setsVisitante++;
                        }
                        if (puntuacion1.getJuegosS3() != 0 && puntuacion2.getJuegosS3() != 0) {
                            if (puntuacion1.getJuegosS3() > puntuacion2.getJuegosS3()) {
                                setsLocal++;
                            } else {
                                setsVisitante++;
                            }
                        }
                        if (setsLocal > setsVisitante) {
                            JOptionPane.showMessageDialog(PartidosPanel.this, "El ganador es " + listModel.get(0).getPuntuacionEquipoPartido1().getNombre());
                        } else {
                            JOptionPane.showMessageDialog(PartidosPanel.this, "El ganador es " + listModel.get(0).getPuntuacionEquipoPartido2().getNombre());
                        }

                }
                cargarLista();
            }
        });
        pistas = bd.obtenerPistas();
        equipos = bd.obtenerEquipos();

        // Componentes para mostrar la lista de partidos
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        //queremos un tipo de letra con el ancho fijo
        list.setFont(new Font("Monospaced", Font.PLAIN, 12));
        list.setCellRenderer(new DefaultListCellRenderer() {
             @Override
             public Component getListCellRendererComponent(JList<?> list, Object value,
                                                           int index, boolean isSelected, boolean cellHasFocus) {
                 PartidoListModel fila = (PartidoListModel) value;
                 return super.getListCellRendererComponent(list, getTextoPartido(fila), index, isSelected, cellHasFocus);
             }
         }
        );

        //si hacemos doble click en un elemento de la lista, se cargara en el formulario para poder modificarlo o eliminarlo
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                pincharFila(evt);
            }
        });

        faseComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarLista();
            }
        });

        //generamos un panel para la label, los combo ce los dos equipos y las puntuaciones de los 3 set
        JPanel camposPanel = new JPanel();
        camposPanel.add(new JLabel("S1(L-V):"));
        camposPanel.add(set1LocalField);
        camposPanel.add(set1VisitanteField);
        camposPanel.add(new JLabel("S2(L-V):"));
        camposPanel.add(set2LocalField);
        camposPanel.add(set2VisitanteField);
        camposPanel.add(new JLabel("S3(L-V):"));
        camposPanel.add(set3LocalField);
        camposPanel.add(set3VisitanteField);
        camposPanel.add(puntuarButton);
        camposPanel.add(cancelarButton);
        //establecemos una anchura fija para los campos de texto
        set1LocalField.setColumns(1);
        set2LocalField.setColumns(1);
        set3LocalField.setColumns(1);
        set1VisitanteField.setColumns(1);
        set2VisitanteField.setColumns(1);
        set3VisitanteField.setColumns(1);

        // Generar un panel para los botones que ocupe toda la fila
        JPanel botonesPanel = new JPanel();
        botonesPanel.add(crearTorneoButton);
        botonesPanel.add(borrarTorneoButton);
        formularioPanel.add(faseComboBox);
        botonesPanel.add(rellenarButton);
        botonesPanel.add(procesarButton);

        formularioPanel.add(botonesPanel);
        formularioPanel.add(camposPanel);

        // Indicar un margen de 10px
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agregar componentes al panel de jugadores
        add(formularioPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        cargarLista();
    }

    private void puntuarPartido() {
        //obtenemos los valores de los campos
        int set1Local = Integer.parseInt(set1LocalField.getText());
        int set2Local = Integer.parseInt(set2LocalField.getText());
        int set3Local = Integer.parseInt(set3LocalField.getText());
        int set1Visitante = Integer.parseInt(set1VisitanteField.getText());
        int set2Visitante = Integer.parseInt(set2VisitanteField.getText());
        int set3Visitante = Integer.parseInt(set3VisitanteField.getText());
        if (validarPuntuacion(set1Local, set2Local, set3Local, set1Visitante, set2Visitante, set3Visitante)){
            JOptionPane.showMessageDialog(this, "La puntuación no es válida");
            return;
        }

        PartidoEquipo partidoEquipoLocal = new PartidoEquipo(seleccionado.getPartido().getId(), seleccionado.getPuntuacionEquipoPartido1().getIdEquipo(), set1Local, set2Local, set3Local);
        PartidoEquipo partidoEquipoVisitante = new PartidoEquipo(seleccionado.getPartido().getId(), seleccionado.getPuntuacionEquipoPartido2().getIdEquipo(), set1Visitante, set2Visitante, set3Visitante);

        if (bd.actualizarPuntuacionEquipoPartido(partidoEquipoLocal) && bd.actualizarPuntuacionEquipoPartido(partidoEquipoVisitante)) {
            JOptionPane.showMessageDialog(this, "El partido se ha puntuado correctamente");
            cancelarSeleccion();
        } else {
            JOptionPane.showMessageDialog(this, "No se ha podido puntuar el partido");
        }
    }

    private void estadoBotones() {

        if (seleccionado != null) {
            puntuarButton.setEnabled(true);
            cancelarButton.setEnabled(true);
            rellenarButton.setEnabled(false);
            procesarButton.setEnabled(false);
            crearTorneoButton.setEnabled(false);
            borrarTorneoButton.setEnabled(false);
            faseComboBox.setEnabled(false);
        } else {
            ArrayList<Partido> partidosFaseSiguiente;
            switch (faseComboBox.getSelectedIndex()) {
                case 0:
                    partidosFaseSiguiente = bd.obtenerPartidosCuadro("Cuartos de final");
                    break;
                case 1:
                    partidosFaseSiguiente = bd.obtenerPartidosCuadro("Semifinales");
                    break;
                case 2:
                    partidosFaseSiguiente = bd.obtenerPartidosCuadro("Final");
                    break;
                default:
                    partidosFaseSiguiente = null;

            }
            boolean faseSiguienteCompleta = true;
            if (partidosFaseSiguiente != null && !partidosFaseSiguiente.isEmpty()) {
                for (Partido partido : partidosFaseSiguiente) {
                    PartidoListModel partidoListModel;
                    ArrayList<PuntuacionEquipoPartido> puntuacion = bd.obtenerPuntuacionEquiposPartido(partido.getId());
                    if (puntuacion.size() < 2) {
                        faseSiguienteCompleta = false;
                        break;
                    }
                }
            }else{
                faseSiguienteCompleta = false;
            }
            puntuarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            rellenarButton.setEnabled(!faseSiguienteCompleta);
            procesarButton.setEnabled(faseCompleta && !faseSiguienteCompleta);
            crearTorneoButton.setEnabled(listModel.isEmpty());
            borrarTorneoButton.setEnabled(!listModel.isEmpty());
            faseComboBox.setEnabled(true);
            list.setEnabled(!faseSiguienteCompleta);
        }
    }

    private boolean validarPuntuacion(int set1Local, int set2Local, int set3Local, int set1Visitante, int set2Visitante, int set3Visitante) {
        // Comprobamos que los valores son correctos (no negativos)
        if ((set1Local < 0) || (set2Local < 0) || (set3Local < 0) ||
                (set1Visitante < 0) || (set2Visitante < 0) || (set3Visitante < 0)) {
            return true;
        }

        if (    // Evitar empates en los sets
                (set1Local == set1Visitante) ||
                        (set2Local == set2Visitante) ||
                        (set3Local == set3Visitante && set3Visitante != 0) ||

                        // Evitar más de 7 juegos ganados o menos de 6 en sets completos
                        (set1Local > 7 || set1Visitante > 7 || set1Local < 6 && set1Visitante < 6) ||
                        (set2Local > 7 || set2Visitante > 7 || set2Local < 6 && set2Visitante < 6) ||
                        (set3Local > 7 || set3Visitante > 7 || set3Local < 6 && set3Visitante < 6 && set3Visitante != 0) ||

                        // Asegurar que para ganar con 7 juegos, el otro jugador tenga 5 o 6 juegos
                        (set1Local == 7 && !(set1Visitante == 5 || set1Visitante == 6)) ||
                        (set2Local == 7 && !(set2Visitante == 5 || set2Visitante == 6)) ||
                        (set3Local == 7 && set3Visitante != 6 && set3Visitante != 0) ||
                        (set1Visitante == 7 && !(set1Local == 5 || set1Local == 6)) ||
                        (set2Visitante == 7 && !(set2Local == 5 || set2Local == 6)) ||
                        (set3Visitante == 7 && set3Local != 6 && set3Visitante != 0)
        ) {
            return true; // Devuelve true si alguna de las condiciones inválidas se cumple
        }

        // Comprobamos que no quede en empate contando los sets ganados por cada jugador
        int setsLocal = 0;
        int setsVisitante = 0;

        if (set1Local > set1Visitante) setsLocal++;
        else setsVisitante++;

        if (set2Local > set2Visitante) setsLocal++;
        else setsVisitante++;

        if (set3Local != 0 || set3Visitante != 0) { // Considerar el tercer set solo si se ha jugado
            if (set3Local > set3Visitante) setsLocal++;
            else setsVisitante++;
        }

        // Devuelve true si los sets ganados son iguales, indicando un resultado inválido
        return setsLocal == setsVisitante;
    }

    private void cancelarSeleccion() {
        // Limpiar el formulario
        set1LocalField.setText("");
        set2LocalField.setText("");
        set3LocalField.setText("");
        set1VisitanteField.setText("");
        set2VisitanteField.setText("");
        set3VisitanteField.setText("");
        seleccionado = null;
        list.clearSelection();
        cargarLista();
        estadoBotones();
    }

    private void pincharFila(MouseEvent evt) {
        if (!list.isEnabled()){
            return;
        }
        int index = list.locationToIndex(evt.getPoint());
        if (index >= 0) {
            PartidoListModel fila = listModel.getElementAt(index);
            if (fila.getPuntuacionEquipoPartido1() != null && fila.getPuntuacionEquipoPartido2() != null) {

                seleccionado = fila;

                //rellenamos los campos con los valores de la fila
                set1LocalField.setText(String.valueOf(fila.getPuntuacionEquipoPartido1().getJuegosS1()));
                set2LocalField.setText(String.valueOf(fila.getPuntuacionEquipoPartido1().getJuegosS2()));
                set3LocalField.setText(String.valueOf(fila.getPuntuacionEquipoPartido1().getJuegosS3()));
                set1VisitanteField.setText(String.valueOf(fila.getPuntuacionEquipoPartido2().getJuegosS1()));
                set2VisitanteField.setText(String.valueOf(fila.getPuntuacionEquipoPartido2().getJuegosS2()));
                set3VisitanteField.setText(String.valueOf(fila.getPuntuacionEquipoPartido2().getJuegosS3()));

                estadoBotones();
            }
        }
    }

    private void cargarLista() {
        faseCompleta = true;
        listModel.clear();
        //desactivamos los botones de crear torneo si este está creado
        ArrayList<Partido> partidos;
        switch (faseComboBox.getSelectedIndex()) {
            case 0:
                partidos = bd.obtenerPartidosRegular();
                break;
            case 1:
                partidos = bd.obtenerPartidosCuadro("Cuartos de final");
                break;
            case 2:
                partidos = bd.obtenerPartidosCuadro("Semifinales");
                break;
            default:
                partidos = bd.obtenerPartidosCuadro("Final");
                break;
        }
        if (partidos != null && !partidos.isEmpty()) {
            for (Partido partido : partidos) {
                PartidoListModel partidoListModel;
                ArrayList<PuntuacionEquipoPartido> puntuacion = bd.obtenerPuntuacionEquiposPartido(partido.getId());
                if (puntuacion.size() == 2) {
                    partidoListModel = new PartidoListModel(partido, puntuacion.get(0), puntuacion.get(1));

                    if (puntuacion.get(0).getJuegosS1() == puntuacion.get(1).getJuegosS1() && puntuacion.get(1).getJuegosS1() == 0) {
                        faseCompleta = false;
                    }
                }else {
                    partidoListModel = new PartidoListModel(partido, null, null);
                    faseCompleta = false;
                }
                listModel.addElement(partidoListModel);
            }
        }else{
            faseCompleta = false;
        }
        estadoBotones();
    }

    private String getTextoPartido(PartidoListModel fila) {
        //obtenemos el nombre del equipo local, visitante y partido
        String nombrePista = "";
        for (Pista pista : pistas) {
            if (pista.getId() == fila.getPartido().getIdPista()) {
                nombrePista = pista.getNombre();
            }
        }
        String nombreLocal = "No definido";
        String nombreVisitante = "No definido";
        String puntuacion = "(x-x, x-x, x-x)";
        if (fila.getPuntuacionEquipoPartido1() != null) {
            for (Equipo equipo : equipos) {
                if (equipo.getId() == fila.getPuntuacionEquipoPartido1().getIdEquipo()) {
                    nombreLocal = equipo.getNombre();
                }
                if (equipo.getId() == fila.getPuntuacionEquipoPartido2().getIdEquipo()) {
                    nombreVisitante = equipo.getNombre();
                }
            }

            puntuacion = String.format("(%d-%d, %d-%d", fila.getPuntuacionEquipoPartido1().getJuegosS1(), fila.getPuntuacionEquipoPartido2().getJuegosS1(), fila.getPuntuacionEquipoPartido1().getJuegosS2(), fila.getPuntuacionEquipoPartido2().getJuegosS2());
            if (!((fila.getPuntuacionEquipoPartido1().getJuegosS3() == 0) && (fila.getPuntuacionEquipoPartido2().getJuegosS3() == 0))) {
                puntuacion = puntuacion + ", " + fila.getPuntuacionEquipoPartido1().getJuegosS3() + "-" + fila.getPuntuacionEquipoPartido2().getJuegosS3();
            }
            puntuacion = puntuacion + ")";
        }else{
            rellenarButton.setEnabled(false);
        }
        return String.format("ID: %02d | Fecha: %02d/%02d/%04d %02d:00 | Pista: %-10s | %20s VS %-20s: %s ", fila.getPartido().getId(), fila.getPartido().getDia(), fila.getPartido().getMes(), fila.getPartido().getAnyo(), fila.getPartido().getHora(), nombrePista, nombreLocal, nombreVisitante, puntuacion);
    }

}
