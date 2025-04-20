package Controller;

import Model.Move;
import Model.WaffleModel;
import View.WaffleGameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class WaffleController {
    private WaffleModel model;
    private WaffleGameView view;

    public WaffleController() {
        this.model = new WaffleModel();
    }

    public void setView(WaffleGameView view) {
        this.view = view;
        updateView();
    }

    public void newGame() {
        model.initGame();
        updateView();
    }

    public void makeMove(int x, int y) {
        if (!model.isGameOver()) {
            boolean moveMade = model.makeMove(x, y);
            if (moveMade) {
                updateView();

                // Check if AI should play after player's move
                if (model.getCurrentPlayer() == 2 && model.isAIPlaying()) {
                    makeAIMoveWithDelay();
                }
            }
        }
    }

    private void makeAIMoveWithDelay() {
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Move aiMove = model.getAIMove();
                if (aiMove != null) {
                    model.makeMove(aiMove.getX(), aiMove.getY());
                    updateView();
                }
            }
        };
        Timer timer = new Timer(2000, taskPerformer);
        timer.setRepeats(false);
        timer.start();
    }

    public void undoMove() {
        if (model.undoMove()) {
            updateView();
        }
    }

    public void redoMove() {
        if (model.redoMove()) {
            updateView();
        }
    }

    public boolean canUndo() {
        return model.getCurrentMoveIndex() >= 0;
    }

    public boolean canRedo() {
        return model.getCurrentMoveIndex() < model.getMoveHistory().size() - 1;
    }

    public void getHint() {
        Move hint = model.getHint();
        if (hint != null) {
            view.showHint(hint.getX(), hint.getY());
        } else {
            view.showInfoMessage("Aucun indice disponible.");
        }
    }

    public void setAIPlaying(boolean aiPlaying) {
        model.setAIPlaying(aiPlaying);

        // Si l'IA est activée et c'est le tour du joueur 2, faire jouer l'IA
        if (aiPlaying && model.getCurrentPlayer() == 2) {
            makeAIMoveWithDelay();
        }
    }

    public void setAILevel(int level) {
        model.setAILevel(level);
    }

    public void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Ajouter l'extension .waf si elle n'est pas présente
            if (!file.getName().toLowerCase().endsWith(".waf")) {
                file = new File(file.getAbsolutePath() + ".waf");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(model);
                view.showInfoMessage("Partie sauvegardée avec succès !");
            } catch (IOException e) {
                view.showErrorMessage("Erreur lors de la sauvegarde de la partie : " + e.getMessage());
            }
        }
    }

    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                model = (WaffleModel) ois.readObject();
                updateView();
                view.showInfoMessage("Partie chargée avec succès !");
            } catch (IOException | ClassNotFoundException e) {
                view.showErrorMessage("Erreur lors du chargement de la partie : " + e.getMessage());
            }
        }
    }

    private void updateView() {
        if (view != null) {
            view.updateView(model);
        }
    }
}