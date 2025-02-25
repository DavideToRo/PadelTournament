package vista;

import controlador.MySqlDao;
import modelo.Pista;
import modelo.Pista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PistasPanel extends JPanel {
    private MySqlDao bd;
    private DefaultListModel<Pista> listModel;
    private JList<Pista> list;
    private JTextField nombreField;
    private int seleccionado = -1;

    private JButton agregarButton;
    private JButton borrarButton;
    private JButton modificarButton;
    private JButton cancelarButton;

    public PistasPanel(MySqlDao bd) {
        this.bd = bd;
        setLayout(new BorderLayout());

        // Componentes para el formulario
        JPanel formularioPanel = new JPanel();
        formularioPanel.setLayout(new BoxLayout(formularioPanel, BoxLayout.Y_AXIS));
        nombreField = new JTextField(20);
        agregarButton = new JButton("Agregar Pista");
        borrarButton = new JButton("Borrar Pista");
        modificarButton = new JButton("Modificar Pista");
        cancelarButton = new JButton("Cancelar");

        //desactivamos los botones de modificar y borrar hasta que se seleccione un pista de la lista
        borrarButton.setEnabled(false);
        modificarButton.setEnabled(false);
        cancelarButton.setEnabled(false);

        //agregamos funcionalidad al botón de agregar
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarPista();
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
                modificarPista();
            }
        });

        //agregamos funcionalidad al botón de borrar
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarPista();
            }
        });

        // Componentes para mostrar la lista de pistas
        listModel = new DefaultListModel<Pista>();
        list = new JList<Pista>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        //queremos un tipo de letra con el ancho fijo
        list.setFont(new Font("Monospaced", Font.PLAIN, 12));

        cargarLista();

        //si hacemos doble click en un elemento de la lista, se cargara en el formulario para poder modificarlo o eliminarlo
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                pincharFila(evt);
            }
        });

        //generamos un panel para la label y el texto
        JPanel camposPanel = new JPanel();
        camposPanel.add(new JLabel("Nombre del Pista:"));
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

    private void agregarPista() {
        String nombrePista = nombreField.getText();
        //comprobar que el nombre no esté vacío
        if (nombrePista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del Pista no puede estar vacío");
            return;
        }
        //en caso de que no esté vacío, crear un objeto Pista y agregarlo a la BD
        Pista pista = new Pista(0, nombrePista);
        if (bd.insertarPista(pista)) {
            JOptionPane.showMessageDialog(this, "El Pista se ha creado correctamente");
            listModel.clear();
            cargarLista();
        }
        else {
            JOptionPane.showMessageDialog(this, "No se ha podido crear el Pista");
        }
        nombreField.setText("");  // Limpiar el campo después de agregar
    }

    private void borrarPista() {
        //comprobar que el nombre no esté vacío
        String nombrePista = nombreField.getText();
        if (nombrePista.isEmpty()) {
            JOptionPane.showMessageDialog(PistasPanel.this, "El nombre del Pista no puede estar vacío");
            return;
        }
        //en caso de que no esté vacío, crear un objeto Pista y agregarlo a la BD
        Pista pista = new Pista(seleccionado, nombrePista);
        if (bd.borrarPista(pista.getId())) {
            JOptionPane.showMessageDialog(PistasPanel.this, "El Pista se ha borrado correctamente");
            //limpiamos el formulario
            nombreField.setText("");
            //desactivamos los botones de modificar y borrar hasta que se seleccione un pista de la lista
            borrarButton.setEnabled(false);
            modificarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            //activamos el botón de agregar
            agregarButton.setEnabled(true);
            //actualizamos la lista
            cargarLista();
        }
        else {
            JOptionPane.showMessageDialog(PistasPanel.this, "No se ha podido borrar el Pista");
        }
    }

    private void modificarPista() {
        //comprobar que el nombre no esté vacío
        String nombrePista = nombreField.getText();
        if (nombrePista.isEmpty()) {
            JOptionPane.showMessageDialog(PistasPanel.this, "El nombre del Pista no puede estar vacío");
            return;
        }
        //en caso de que no esté vacío, crear un objeto Pista y agregarlo a la BD
        Pista pista = new Pista(seleccionado, nombrePista);
        if (bd.modificarPista(pista)) {
            JOptionPane.showMessageDialog(PistasPanel.this, "El Pista se ha modificado correctamente");
            //limpiamos el formulario
            nombreField.setText("");
            //desactivamos los botones de modificar y borrar hasta que se seleccione un pista de la lista
            borrarButton.setEnabled(false);
            modificarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            //activamos el botón de agregar
            agregarButton.setEnabled(true);
            //actualizamos la lista
            cargarLista();
        }
        else {
            JOptionPane.showMessageDialog(PistasPanel.this, "No se ha podido modificar el Pista");
        }

    }

    private void cancelarSeleccion() {
        //limpiamos el formulario
        nombreField.setText("");
        //desactivamos los botones de modificar y borrar hasta que se seleccione un pista de la lista
        borrarButton.setEnabled(false);
        modificarButton.setEnabled(false);
        cancelarButton.setEnabled(false);
        //activamos el botón de agregar
        agregarButton.setEnabled(true);
    }

    private void pincharFila(MouseEvent evt) {
        int index = list.locationToIndex(evt.getPoint());
        if (index >= 0) {
            //cargo el nombre del pista en el formulario y nos quedamos con la id para poder modificarlo o eliminarlo
            Pista pista = listModel.getElementAt(index);
            String nombrePista = pista.getNombre();
            nombreField.setText(nombrePista);
            seleccionado = pista.getId();

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
        //rellenar la lista de pistas con la id para poder modificarlos y eliminarlos
        ArrayList<Pista> pistas = bd.obtenerPistas();
        for (Pista pista : pistas) {
            listModel.addElement(pista);
        }
    }
}
