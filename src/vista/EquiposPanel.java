package vista;

import controlador.MySqlDao;
import modelo.Equipo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class EquiposPanel extends JPanel {
    private MySqlDao bd;
    private DefaultListModel<Equipo> listModel;
    private JList<Equipo> list;
    private JTextField nombreField;
    private int seleccionado = -1;

    private JButton agregarButton;
    private JButton borrarButton;
    private JButton modificarButton;
    private JButton cancelarButton;

    public EquiposPanel(MySqlDao bd) {
        this.bd = bd;
        setLayout(new BorderLayout());

        // Componentes para el formulario
        JPanel formularioPanel = new JPanel();
        formularioPanel.setLayout(new BoxLayout(formularioPanel, BoxLayout.Y_AXIS));
        nombreField = new JTextField(20);
        agregarButton = new JButton("Agregar Equipo");
        borrarButton = new JButton("Borrar Equipo");
        modificarButton = new JButton("Modificar Equipo");
        cancelarButton = new JButton("Cancelar");

        //desactivamos los botones de modificar y borrar hasta que se seleccione un equipo de la lista
        borrarButton.setEnabled(false);
        modificarButton.setEnabled(false);
        cancelarButton.setEnabled(false);

        //agregamos funcionalidad al botón de agregar
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarEquipo();
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
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarEquipo();
            }
        });

        //agregamos funcionalidad al botón de borrar
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarEquipo();
            }
        });

        // Componentes para mostrar la lista de equipos
        listModel = new DefaultListModel<Equipo>();
        list = new JList<Equipo>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        //queremos un tipo de letra con el ancho fijo
        list.setFont(new Font("Monospaced", Font.PLAIN, 12));

        cargarLista();

        //si hacemos doble click en un elemento de la lista, se cargara en el formulario para poder modificarlo o eliminarlo
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pincharFila(evt);
            }
        });

        //generamos un panel para la label y el texto
        JPanel camposPanel = new JPanel();
        camposPanel.add(new JLabel("Nombre del Equipo:"));
        camposPanel.add(nombreField);

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

    private void agregarEquipo() {
        String nombreEquipo = nombreField.getText();
        //comprobar que el nombre no esté vacío
        if (nombreEquipo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del Equipo no puede estar vacío");
            return;
        }
        //en caso de que no esté vacío, crear un objeto Equipo y agregarlo a la BD
        Equipo equipo = new Equipo(0, nombreEquipo, 0, 0, 0, 0);
        if (bd.insertarEquipo(equipo)) {
            JOptionPane.showMessageDialog(this, "El Equipo se ha creado correctamente");
            listModel.clear();
            cargarLista();
        }
        else {
            JOptionPane.showMessageDialog(this, "No se ha podido crear el Equipo");
        }
        nombreField.setText("");  // Limpiar el campo después de agregar
    }

    private void borrarEquipo() {
        //comprobar que el nombre no esté vacío
        String nombreEquipo = nombreField.getText();
        if (nombreEquipo.isEmpty()) {
            JOptionPane.showMessageDialog(EquiposPanel.this, "El nombre del Equipo no puede estar vacío");
            return;
        }
        //en caso de que no esté vacío, crear un objeto Equipo y agregarlo a la BD
        Equipo equipo = new Equipo(seleccionado, nombreEquipo, 0, 0, 0, 0);
        if (bd.borrarEquipo(equipo.getId())) {
            JOptionPane.showMessageDialog(EquiposPanel.this, "El Equipo se ha borrado correctamente");
            //limpiamos el formulario
            nombreField.setText("");
            //desactivamos los botones de modificar y borrar hasta que se seleccione un equipo de la lista
            borrarButton.setEnabled(false);
            modificarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            //activamos el botón de agregar
            agregarButton.setEnabled(true);
            //actualizamos la lista
            cargarLista();
        }
        else {
            JOptionPane.showMessageDialog(EquiposPanel.this, "No se ha podido borrar el Equipo");
        }
    }

    private void modificarEquipo() {
        //comprobar que el nombre no esté vacío
        String nombreEquipo = nombreField.getText();
        if (nombreEquipo.isEmpty()) {
            JOptionPane.showMessageDialog(EquiposPanel.this, "El nombre del Equipo no puede estar vacío");
            return;
        }
        //en caso de que no esté vacío, crear un objeto Equipo y agregarlo a la BD
        Equipo equipo = new Equipo(seleccionado, nombreEquipo, 0, 0, 0, 0);
        if (bd.modificarEquipo(equipo)) {
            JOptionPane.showMessageDialog(EquiposPanel.this, "El Equipo se ha modificado correctamente");
            //limpiamos el formulario
            nombreField.setText("");
            //desactivamos los botones de modificar y borrar hasta que se seleccione un equipo de la lista
            borrarButton.setEnabled(false);
            modificarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            //activamos el botón de agregar
            agregarButton.setEnabled(true);
            //actualizamos la lista
            cargarLista();
        }
        else {
            JOptionPane.showMessageDialog(EquiposPanel.this, "No se ha podido modificar el Equipo");
        }

    }

    private void cancelarSeleccion() {
        //limpiamos el formulario
        nombreField.setText("");
        //desactivamos los botones de modificar y borrar hasta que se seleccione un equipo de la lista
        borrarButton.setEnabled(false);
        modificarButton.setEnabled(false);
        cancelarButton.setEnabled(false);
        //activamos el botón de agregar
        agregarButton.setEnabled(true);
    }

    private void pincharFila(MouseEvent evt) {
        int index = list.locationToIndex(evt.getPoint());
        if (index >= 0) {
            //cargo el nombre del equipo en el formulario y nos quedamos con la id para poder modificarlo o eliminarlo
            Equipo equipo = listModel.getElementAt(index);
            String nombreEquipo = equipo.getNombre();
            nombreField.setText(nombreEquipo);
            seleccionado = equipo.getId();

            //habilitamos los botones
            modificarButton.setEnabled(true);
            borrarButton.setEnabled(true);
            cancelarButton.setEnabled(true);
            //desactivamos el botón de agregar
            agregarButton.setEnabled(false);
        }
    }

    private void cargarLista() {
        listModel.clear();
        //rellenar la lista de equipos con la id para poder modificarlos y eliminarlos
        ArrayList<Equipo> equipos = bd.obtenerEquipos();
        for (Equipo equipo : equipos) {
            listModel.addElement(equipo);
        }
    }
}
