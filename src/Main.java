import controlador.MySqlDao;
import vista.TorneoAppGUI;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    UIManager.put("nimbusBase", new Color(50, 100, 250));
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        SwingUtilities.invokeLater(() -> {
            MySqlDao bd = new MySqlDao();
            TorneoAppGUI app = new TorneoAppGUI(bd);
            app.setVisible(true); // Aseguramos que la vnetana queda visible
        });
        //VistaTexto gestion = new VistaTexto(bd);
        //gestion.iniciarApp();
    }
}