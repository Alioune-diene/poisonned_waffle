
import Controller.WaffleController;
import View.WaffleGameView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Utiliser le look and feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Créer le contrôleur
        WaffleController controller = new WaffleController();

        // Créer la vue et l'associer au contrôleur
        SwingUtilities.invokeLater(() -> {
            WaffleGameView view = new WaffleGameView(controller);
            controller.setView(view);
        });
    }
}