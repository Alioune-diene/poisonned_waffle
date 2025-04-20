package View;

import Controller.WaffleController;
import Model.WaffleModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class WaffleGameView extends JFrame {
    private final WaffleController controller;
    private final WafflePanel wafflePanel;
    private JLabel statusLabel;
    private JLabel playerLabel;
    private JPanel hintsPanel;
    private JLabel[] heartLabels;

    // Menu items
    private JMenuItem newGameMenuItem;
    private JMenuItem saveGameMenuItem;
    private JMenuItem loadGameMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem hintsMenuItem;

    // Buttons
    private JButton newGameButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton hintButton;

    // Images
    private ImageIcon fullHeartIcon;
    private ImageIcon emptyHeartIcon;

    public WaffleGameView(WaffleController controller) {
        this.controller = controller;

        // Charger les icônes
        loadIcons();

        // Configurer la fenêtre
        setTitle("La Gaufre Empoisonnée");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        // Créer la barre de menu
        createMenuBar();

        // Créer le panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Créer le panneau de gaufre
        wafflePanel = new WafflePanel(controller);
        mainPanel.add(wafflePanel, BorderLayout.CENTER);

        // Créer le panneau d'informations
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.EAST);

        // Créer la barre d'état
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        // Créer la barre d'outils
        JToolBar toolBar = createToolBar();
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // Ajouter le panneau principal à la fenêtre
        add(mainPanel);

        setVisible(true);
    }

    private void loadIcons() {
        fullHeartIcon = new ImageIcon(getClass().getResource("/resources/images/full_heart.png"));
        if (fullHeartIcon.getIconWidth() == -1) {
            // Si l'image n'a pas pu être chargée, utiliser une image par défaut
            fullHeartIcon = new ImageIcon(new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB));
            Graphics g = fullHeartIcon.getImage().getGraphics();
            g.setColor(Color.RED);
            g.fillOval(0, 0, 20, 20);
            g.dispose();
        }

        emptyHeartIcon = new ImageIcon(getClass().getResource("/resources/images/empty_heart.png"));
        if (emptyHeartIcon.getIconWidth() == -1) {
            // Si l'image n'a pas pu être chargée, utiliser une image par défaut
            emptyHeartIcon = new ImageIcon(new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB));
            Graphics g = emptyHeartIcon.getImage().getGraphics();
            g.setColor(Color.GRAY);
            g.fillOval(0, 0, 20, 20);
            g.dispose();
        }
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Jeu
        JMenu gameMenu = new JMenu("Jeu");
        gameMenu.setMnemonic(KeyEvent.VK_J);

        newGameMenuItem = new JMenuItem("Nouvelle partie", KeyEvent.VK_N);
        newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newGameMenuItem.addActionListener(e -> controller.newGame());

        saveGameMenuItem = new JMenuItem("Sauvegarder", KeyEvent.VK_S);
        saveGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveGameMenuItem.addActionListener(e -> controller.saveGame());

        loadGameMenuItem = new JMenuItem("Charger", KeyEvent.VK_C);
        loadGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        loadGameMenuItem.addActionListener(e -> controller.loadGame());

        exitMenuItem = new JMenuItem("Quitter", KeyEvent.VK_Q);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exitMenuItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newGameMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(saveGameMenuItem);
        gameMenu.add(loadGameMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);

        // Menu Édition
        JMenu editMenu = new JMenu("Édition");
        editMenu.setMnemonic(KeyEvent.VK_E);

        undoMenuItem = new JMenuItem("Annuler", KeyEvent.VK_Z);
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undoMenuItem.addActionListener(e -> controller.undoMove());

        redoMenuItem = new JMenuItem("Refaire", KeyEvent.VK_Y);
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        redoMenuItem.addActionListener(e -> controller.redoMove());

        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);

        // Menu Aide
        JMenu helpMenu = new JMenu("Aide");
        helpMenu.setMnemonic(KeyEvent.VK_A);

        hintsMenuItem = new JMenuItem("Indice", KeyEvent.VK_I);
        hintsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        hintsMenuItem.addActionListener(e -> controller.getHint());

        JMenuItem rulesMenuItem = new JMenuItem("Règles du jeu", KeyEvent.VK_R);
        rulesMenuItem.addActionListener(e -> showRules());

        JMenuItem aboutMenuItem = new JMenuItem("À propos", KeyEvent.VK_P);
        aboutMenuItem.addActionListener(e -> showAbout());

        helpMenu.add(hintsMenuItem);
        helpMenu.addSeparator();
        helpMenu.add(rulesMenuItem);
        helpMenu.add(aboutMenuItem);

        menuBar.add(gameMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        newGameButton = new JButton("Nouvelle partie");
        newGameButton.addActionListener(e -> controller.newGame());

        undoButton = new JButton("Annuler");
        undoButton.addActionListener(e -> controller.undoMove());

        redoButton = new JButton("Refaire");
        redoButton.addActionListener(e -> controller.redoMove());

        hintButton = new JButton("Indice");
        hintButton.addActionListener(e -> controller.getHint());

        toolBar.add(newGameButton);
        toolBar.addSeparator();
        toolBar.add(undoButton);
        toolBar.add(redoButton);
        toolBar.addSeparator();
        toolBar.add(hintButton);

        return toolBar;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Affichage du joueur courant
        playerLabel = new JLabel("Joueur 1");
        playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panneau pour les indices restants (cœurs)
        hintsPanel = new JPanel();
        hintsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        hintsPanel.setBorder(BorderFactory.createTitledBorder("Indices restants"));

        heartLabels = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            heartLabels[i] = new JLabel(fullHeartIcon);
            hintsPanel.add(heartLabels[i]);
        }

        // Options de jeu
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        JPanel aiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox aiCheckBox = new JCheckBox("Jouer contre l'ordinateur");
        aiCheckBox.addActionListener(e -> controller.setAIPlaying(aiCheckBox.isSelected()));
        aiPanel.add(aiCheckBox);

        JPanel levelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        levelPanel.add(new JLabel("Niveau : "));

        JComboBox<String> levelComboBox = new JComboBox<>(new String[] {"Facile", "Intermédiaire", "Difficile"});
        levelComboBox.addActionListener(e -> controller.setAILevel(levelComboBox.getSelectedIndex() + 1));
        levelPanel.add(levelComboBox);

        optionsPanel.add(aiPanel);
        optionsPanel.add(levelPanel);

        // Ajouter tous les composants au panneau d'informations
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(playerLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(hintsPanel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(optionsPanel);
        infoPanel.add(Box.createVerticalGlue());

        return infoPanel;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        statusLabel = new JLabel("Nouvelle partie. Joueur 1, à vous de jouer !");
        statusPanel.add(statusLabel, BorderLayout.WEST);

        return statusPanel;
    }

    public void updateView(WaffleModel model) {
        // Mettre à jour le panneau de gaufre
        wafflePanel.updateWaffle(model);

        // Mettre à jour le joueur courant
        int currentPlayer = model.getCurrentPlayer();
        playerLabel.setText("Joueur " + currentPlayer);

        // Mettre à jour les indices restants
        int hintsRemaining = model.getHintsRemaining();
        for (int i = 0; i < heartLabels.length; i++) {
            heartLabels[i].setIcon(i < hintsRemaining ? fullHeartIcon : emptyHeartIcon);
        }

        // Mettre à jour la barre d'état
        if (model.isGameOver()) {
            statusLabel.setText("Partie terminée. Joueur " + model.getWinner() + " a gagné !");
        } else {
            statusLabel.setText("Joueur " + currentPlayer + ", à vous de jouer !");
        }

        // Activer/désactiver les boutons d'annulation/rétablissement
        undoButton.setEnabled(controller.canUndo());
        redoButton.setEnabled(controller.canRedo());
        undoMenuItem.setEnabled(controller.canUndo());
        redoMenuItem.setEnabled(controller.canRedo());

        // Activer/désactiver le bouton d'indice
        hintButton.setEnabled(hintsRemaining > 0 && !model.isGameOver());
        hintsMenuItem.setEnabled(hintsRemaining > 0 && !model.isGameOver());

        // Mettre à jour l'interface
        repaint();
    }

    public void showHint(int x, int y) {
        wafflePanel.showHint(x, y);
    }

    private void showRules() {
        String rules = "Règles du jeu de la gaufre empoisonnée :\n\n" +
                "- Le jeu se joue à deux joueurs sur une grille rectangulaire.\n" +
                "- La case en haut à gauche (0,0) est empoisonnée.\n" +
                "- À chaque tour, un joueur doit mordre dans la gaufre en choisissant une case.\n" +
                "- Toutes les cases situées en bas et à droite de la case choisie disparaissent.\n" +
                "- Le joueur qui est forcé de prendre la case empoisonnée perd la partie.\n" +
                "- Vous ne pouvez pas passer votre tour.";

        JOptionPane.showMessageDialog(this, rules, "Règles du jeu", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        String about = "La Gaufre Empoisonnée\n\n" +
                "Version 1.0\n" +
                "Développé avec Java et Swing par ABD\n\n" +
                "© 2025 - Tous droits réservés";

        JOptionPane.showMessageDialog(this, about, "À propos", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}