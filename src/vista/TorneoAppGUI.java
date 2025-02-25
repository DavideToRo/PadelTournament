package vista;

import controlador.MySqlDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TorneoAppGUI extends JFrame {
    private MySqlDao bd;
    private JPanel cardPanel;
    private CardLayout cardLayout;



    public TorneoAppGUI(MySqlDao bd) {
        this.bd = bd;

        Image image = Toolkit.getDefaultToolkit().getImage("C:/Users/david/Desktop/apptorneo/PadelTournament/lib/icono.png");
        setIconImage(image);


        setTitle("PadelTournament");
        setSize(1024, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mostramos la app centrada en el monitor
        setLocationRelativeTo(null);

        // CardLayout para cambiar entre formularios
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        //creamos un panel básico de bienvenida con texto muy visible

        JPanel bienvenidaPanel = new JPanel(new BorderLayout());
        JLabel bienvenidaLabel = new JLabel("Bienvenidos a PadelTournament");
        bienvenidaLabel.setFont(new Font("Arial", Font.BOLD, 50));
        bienvenidaLabel.setHorizontalAlignment(JLabel.CENTER);
        bienvenidaPanel.add(bienvenidaLabel, BorderLayout.CENTER);


        // Agrega el panel de tarjetas de distintas gestiones al centro
        cardPanel.add(bienvenidaPanel, "Bienvenida");

        add(cardPanel, BorderLayout.CENTER);

        //barra de menus para las opciones: gestión de equipos, de jugadores, de pistas y torneo
        JMenuBar menuBar = new JMenuBar();
        JMenu opcionesMenu = generaOpcionesMenu();
        menuBar.add(opcionesMenu);

        setJMenuBar(menuBar);

        setVisible(true);
    }

    private JMenu generaOpcionesMenu() {
        JMenu opcionesMenu = new JMenu("Opciones");
        JMenuItem gestionEquiposItem = new JMenuItem("Gestión de Equipos");
        JMenuItem gestionJugadoresItem = new JMenuItem("Gestión de Jugadores");
        JMenuItem gestionPistasItem = new JMenuItem("Gestión de Pistas");
        JMenuItem gestionPartidosItem = new JMenuItem("Gestión de Partidos");

        gestionEquiposItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EquiposPanel equiposPanel = new EquiposPanel(bd);
                cardPanel.add(equiposPanel, "Equipos");
                cardLayout.show(cardPanel, "Equipos");
            }
        });
        gestionJugadoresItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JugadoresPanel jugadoresPanel = new JugadoresPanel(bd);
                cardPanel.add(jugadoresPanel, "Jugadores");
                cardLayout.show(cardPanel, "Jugadores");
            }
        });
        gestionPistasItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PistasPanel pistasPanel = new PistasPanel(bd);
                cardPanel.add(pistasPanel, "Pistas");
                cardLayout.show(cardPanel, "Pistas");
            }
        });
        gestionPartidosItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PartidosPanel partidosPanel = new PartidosPanel(bd);
                cardPanel.add(partidosPanel, "Partidos");
                cardLayout.show(cardPanel, "Partidos");
            }
        });

        opcionesMenu.add(gestionEquiposItem);
        opcionesMenu.add(gestionJugadoresItem);
        opcionesMenu.add(gestionPistasItem);
        opcionesMenu.add(gestionPartidosItem);
        return opcionesMenu;
    }

}
