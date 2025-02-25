package vista;

import controlador.MySqlDao;
import modelo.Equipo;
import modelo.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class JugadoresPanel extends JPanel {
    private MySqlDao bd;
    private DefaultListModel<Jugador> listModel;
    private JList<Jugador> list;
    private JTextField dniField;
    private JTextField nombreField;
    private JComboBox<Equipo> equiposComboBox;
    private JButton agregarButton;
    private JButton borrarButton;
    private JButton modificarButton;
    private JButton cancelarButton;
    private ArrayList<Equipo> equipos;
    private int seleccionado;

    public JugadoresPanel(MySqlDao bd) {
        this.bd = bd;
        setLayout(new BorderLayout());

        // Componentes para el formulario para meter el formulario de campos arriba y el de botones abajo
        JPanel formularioPanel = new JPanel();
        formularioPanel.setLayout(new BoxLayout(formularioPanel, BoxLayout.Y_AXIS));

        dniField = new JTextField(20);
        nombreField = new JTextField(20);
        equiposComboBox = new JComboBox<>();
        //necesitamos que el combo muestre únicamente el nombre
        equiposComboBox.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                Equipo equipo = (Equipo) value;
                //null?
                if (equipo == null) {
                    return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
                }else{
                    return super.getListCellRendererComponent(list, equipo.getNombre(), index, isSelected, cellHasFocus);
                }
            }
        });

        // Botones
        agregarButton = new JButton("Agregar Jugador");
        borrarButton = new JButton("Borrar Jugador");
        modificarButton = new JButton("Modificar Jugador");
        cancelarButton = new JButton("Cancelar");

        // Desactivar botones hasta que se seleccione un jugador de la lista
        borrarButton.setEnabled(false);
        modificarButton.setEnabled(false);
        cancelarButton.setEnabled(false);

        // Agregar funcionalidad a los botones
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarJugador();
            }
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarJugador();
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarJugador();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarSeleccion();
            }
        });

        // Componentes para mostrar la lista de jugadores
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        //queremos un tipo de letra con el ancho fijo
        list.setFont(new Font("Monospaced", Font.PLAIN, 12));
        //necesitamos que la lista muestre el texto personalizado de cada jugador
        list.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                                                              int index, boolean isSelected, boolean cellHasFocus) {
                Jugador jugador = (Jugador) value;
                String text = jugador.toString();
                //necesitamos el nombre del equipo entre paréntesis si tiene equipo
                if (jugador.getIdEquipo() != 0) {
                    Equipo equipo = obtenerEquipoPorId(jugador.getIdEquipo());
                    if (equipo != null)
                        text += " (" + equipo.getNombre() + ")";
                }
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
                }
            }
        );

        // Cargar jugadores y equipos al iniciar
        cargarLista();
        // Cargar equipos al iniciar
        cargarEquipos();

        // Evento de clic en la lista de jugadores
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pincharFila(evt);
            }
        });

        // Generar un panel para los campos de texto y el combo box
        JPanel camposPanel = new JPanel();
        camposPanel.add(new JLabel("DNI:"));
        camposPanel.add(dniField);
        camposPanel.add(new JLabel("Nombre:"));
        camposPanel.add(nombreField);
        camposPanel.add(new JLabel("Equipo:"));
        camposPanel.add(equiposComboBox);

        // Generar un panel para los botones que ocupe toda la fila
        JPanel botonesPanel = new JPanel();
        botonesPanel.add(agregarButton);
        botonesPanel.add(borrarButton);
        botonesPanel.add(modificarButton);
        botonesPanel.add(cancelarButton);

        formularioPanel.add(camposPanel);
        formularioPanel.add(botonesPanel);

        // Indicar un margen de 10px
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agregar componentes al panel de jugadores
        add(formularioPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

    }

    private void cargarLista() {
        ArrayList<Jugador> jugadores = bd.obtenerJugadores();
        for (Jugador jugador : jugadores) {
            listModel.addElement(jugador);
        }
    }

    private void cargarEquipos() {
        equipos = bd.obtenerEquipos();
        equiposComboBox.removeAllItems();
        for (Equipo equipo : equipos) {
            equiposComboBox.addItem(equipo);
        }
    }

    private void agregarJugador() {
        String dni = dniField.getText();
        String nombre = nombreField.getText();
        Equipo equipoSeleccionado = (Equipo) equiposComboBox.getSelectedItem();

        // Validar que los campos no estén vacíos y que se haya seleccionado un equipo
        if (dni.isEmpty() || nombre.isEmpty() || equipoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos y seleccione un equipo.");
            return;
        }

        Jugador jugador = new Jugador(dni, nombre, equipoSeleccionado.getId());

        if (bd.insertarJugador(jugador)) {
            JOptionPane.showMessageDialog(this, "El Jugador se ha creado correctamente");
            listModel.clear();
            cargarListaJugadores(bd);
        } else {
            JOptionPane.showMessageDialog(this, "No se ha podido crear el Jugador");
        }

        // Limpiar los campos después de agregar
        dniField.setText("");
        nombreField.setText("");
    }

    private void borrarJugador() {

        if (bd.borrarJugador(seleccionado)) {
            JOptionPane.showMessageDialog(this, "El Jugador se ha borrado correctamente");
            // Limpiar el formulario
            dniField.setText("");
            nombreField.setText("");
            equiposComboBox.setSelectedItem(null);
            borrarButton.setEnabled(false);
            modificarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            agregarButton.setEnabled(true);
            listModel.clear();
            cargarListaJugadores(bd);
        } else {
            JOptionPane.showMessageDialog(this, "No se ha podido borrar el Jugador");
        }
    }

    private void modificarJugador() {
        String dni = dniField.getText();
        String nombre = nombreField.getText();
        Equipo equipoSeleccionado = (Equipo) equiposComboBox.getSelectedItem();

        // Validar que los campos no estén vacíos y que se haya seleccionado un equipo
        if (dni.isEmpty() || nombre.isEmpty() || equipoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos y seleccione un equipo.");
            return;
        }

        Jugador jugador = new Jugador(seleccionado, dni, nombre, equipoSeleccionado.getId());

        if (bd.modificarJugador(jugador)) {
            JOptionPane.showMessageDialog(this, "El Jugador se ha modificado correctamente");
            // Limpiar el formulario
            dniField.setText("");
            nombreField.setText("");
            equiposComboBox.setSelectedItem(null);
            borrarButton.setEnabled(false);
            modificarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            agregarButton.setEnabled(true);
            listModel.clear();
            cargarListaJugadores(bd);
        } else {
            JOptionPane.showMessageDialog(this, "No se ha podido modificar el Jugador");
        }
    }

    private void cancelarSeleccion() {
        // Limpiar el formulario
        dniField.setText("");
        nombreField.setText("");
        equiposComboBox.setSelectedItem(null);
        borrarButton.setEnabled(false);
        modificarButton.setEnabled(false);
        cancelarButton.setEnabled(false);
        agregarButton.setEnabled(true);
    }

    private void pincharFila(java.awt.event.MouseEvent evt) {
        int index = list.locationToIndex(evt.getPoint());
        if (index >= 0) {
            Jugador jugador = listModel.getElementAt(index);
            // Cargar los datos del jugador en el formulario
            dniField.setText(jugador.getDni());
            nombreField.setText(jugador.getNombre());
            // Seleccionar el equipo del jugador en el combo box
            equiposComboBox.setSelectedItem(obtenerEquipoPorId(jugador.getIdEquipo()));
            seleccionado = jugador.getId();
            // Habilitar los botones
            modificarButton.setEnabled(true);
            borrarButton.setEnabled(true);
            cancelarButton.setEnabled(true);
            agregarButton.setEnabled(false);
        }
    }

    private Equipo obtenerEquipoPorId(int idEquipo) {
        for (Equipo equipo : equipos) {
            if (equipo.getId() == idEquipo) {
                return equipo;
            }
        }
        return null;
    }

    private void cargarListaJugadores(MySqlDao bd) {
        ArrayList<Jugador> jugadores = bd.obtenerJugadores();
        for (Jugador jugador : jugadores) {
            listModel.addElement(jugador);
        }
    }
}
