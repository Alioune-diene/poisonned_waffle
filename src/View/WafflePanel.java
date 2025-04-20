package View;

import Controller.WaffleController;
import Model.WaffleModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class WafflePanel extends JPanel {
    private final WaffleController controller;
    private boolean[][] waffle;
    private int width;
    private int height;

    // Images pour les cases de la gaufre
    private Image waffleImage;
    private Image waffleImageTopLeft;
    private Image waffleImageTopMid;
    private Image waffleImageTopRight;
    private Image waffleImageLeftMid;
    private Image waffleImageRightMid;
    private Image waffleImageBottomLeft;
    private Image waffleImageBottomMid;
    private Image waffleImageBottomRight;
    private Image poisonImage;
    private Image hintImage;

    // Pour l'animation de l'indice
    private int hintX = -1;
    private int hintY = -1;
    private Timer hintTimer;

    public WafflePanel(WaffleController controller) {
        this.controller = controller;

        // Initialiser la gaufre
        waffle = null;
        width = 0;
        height = 0;

        // Charger les images
        loadImages();

        // Ajouter un écouteur de souris
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (waffle != null) {
                    // Convertir les coordonnées de la souris en indices de la grille
                    int cellWidth = getWidth() / width;
                    int cellHeight = getHeight() / height;

                    int x = e.getX() / cellWidth;
                    int y = e.getY() / cellHeight;

                    // Vérifier que les coordonnées sont dans les limites
                    if (x >= 0 && x < width && y >= 0 && y < height) {
                        controller.makeMove(x, y);
                    }
                }
            }
        });

        // Timer pour l'animation de l'indice
        hintTimer = new Timer(1500, e -> {
            hintX = -1;
            hintY = -1;
            repaint();
            hintTimer.stop();
        });
        hintTimer.setRepeats(false);
    }

    private void loadImages() {
        // Charger l'image de la gaufre
        try {
            waffleImage = new ImageIcon(getClass().getResource("/resources/images/waffle.png")).getImage();
        } catch (Exception e) {
            // Si l'image ne peut pas être chargée, créer une image par défaut
            waffleImage = createDefaultWaffleImage();
        }

        try {
            waffleImageTopLeft = new ImageIcon(getClass().getResource("/resources/images/waffle_top_left1.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            waffleImageTopMid = new ImageIcon(getClass().getResource("/resources/images/waffle_top_mid1.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            waffleImageTopRight = new ImageIcon(getClass().getResource("/resources/images/waffle_top_right1.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            waffleImageLeftMid = new ImageIcon(getClass().getResource("/resources/images/waffle_left_mid1.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            waffleImageRightMid = new ImageIcon(getClass().getResource("/resources/images/waffle_right_mid1.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            waffleImageBottomLeft = new ImageIcon(getClass().getResource("/resources/images/waffle_bottom_left1.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            waffleImageBottomMid = new ImageIcon(getClass().getResource("/resources/images/waffle_bottom_mid1.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            waffleImageBottomRight = new ImageIcon(getClass().getResource("/resources/images/waffle_bottom_right1.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Charger l'image de la case empoisonnée
        try {
            poisonImage = new ImageIcon(getClass().getResource("/resources/images/poison1.png")).getImage();
        } catch (Exception e) {
            // Si l'image ne peut pas être chargée, créer une image par défaut
            poisonImage = createDefaultPoisonImage();
        }

        // Charger l'image de l'indice
        try {
            hintImage = new ImageIcon(getClass().getResource("/resources/images/hint1.png")).getImage();
        } catch (Exception e) {
            // Si l'image ne peut pas être chargée, créer une image par défaut
            hintImage = createDefaultHintImage();
        }
    }

    private Image createDefaultWaffleImage() {
        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        // Fond beige clair
        g.setColor(new Color(245, 222, 179));
        g.fillRect(0, 0, 50, 50);

        // Quadrillage brun
        g.setColor(new Color(139, 69, 19));
        g.drawRect(0, 0, 49, 49);
        g.drawLine(0, 25, 49, 25);
        g.drawLine(25, 0, 25, 49);

        g.dispose();
        return img;
    }

    private Image createDefaultPoisonImage() {
        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        // Fond rouge
        g.setColor(Color.RED);
        g.fillRect(0, 0, 50, 50);

        // Symbole de poison
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("☠", 15, 35);

        g.dispose();
        return img;
    }

    private Image createDefaultHintImage() {
        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        // Fond jaune translucide
        g.setColor(new Color(255, 255, 0, 128));
        g.fillRect(0, 0, 50, 50);

        // Bordure jaune foncé
        g.setColor(new Color(218, 165, 32));
        g.setStroke(new BasicStroke(3));
        g.drawRect(0, 0, 49, 49);

        g.dispose();
        return img;
    }

    public void updateWaffle(WaffleModel model) {
        if (model != null) {
            this.waffle = model.getWaffle();
            this.width = model.getWidth();
            this.height = model.getHeight();
            repaint();
        }
    }

    public void showHint(int x, int y) {
        hintX = x;
        hintY = y;
        repaint();
        hintTimer.restart();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (waffle == null || width == 0 || height == 0) {
            return;
        }

        int cellWidth = getWidth() / width;
        int cellHeight = getHeight() / height;

        // Dessiner les cases de la gaufre
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!waffle[y][x]) continue;

                Image img;
                // Coin supérieur gauche (empoisonné)
                if (x == 0 && y == 0) {
                    img = poisonImage;

                    // Coin supérieur droit
                } else if (x == width - 1 && y == 0) {
                    img = waffleImageTopRight;

                    // Coin inférieur gauche
                } else if (x == 0 && y == height - 1) {
                    img = waffleImageBottomLeft;

                    // Coin inférieur droit
                } else if (x == width - 1 && y == height - 1) {
                    img = waffleImageBottomRight;

                    // Bord haut (hors coins)
                } else if (y == 0) {
                    img = waffleImageTopMid;

                    // Bord bas (hors coins)
                } else if (y == height - 1) {
                    img = waffleImageBottomMid;

                    // Bord gauche (hors coins)
                } else if (x == 0) {
                    img = waffleImageLeftMid;

                    // Bord droit (hors coins)
                } else if (x == width - 1) {
                    img = waffleImageRightMid;

                    // Case “intérieure” classique
                } else {
                    img = waffleImage;
                }

                // Affichage de la case
                g.drawImage(img,
                        x * cellWidth, y * cellHeight,
                        cellWidth, cellHeight,
                        this);

                // Dessiner l’indice si nécessaire
                if (hintX == x && hintY == y) {
                    g.drawImage(hintImage,
                            x * cellWidth, y * cellHeight,
                            cellWidth, cellHeight,
                            this);
                }
            }
        }
    }

}