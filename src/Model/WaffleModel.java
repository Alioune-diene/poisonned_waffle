package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaffleModel implements Serializable {
    // Constantes
    public static final int DEFAULT_WIDTH = 6;
    public static final int DEFAULT_HEIGHT = 8;

    // États du jeu
    private boolean[][] waffle;  // true = case présente, false = case mangée
    private int width;
    private int height;
    private int currentPlayer;  // 1 = joueur 1, 2 = joueur 2
    private boolean gameOver;
    private int winner;  // 0 = pas de gagnant, 1 = joueur 1, 2 = joueur 2

    // Pour annuler/rejouer
    private List<Move> moveHistory;
    private int currentMoveIndex;

    // Pour l'IA
    private AIPlayer aiPlayer;
    private boolean isAIPlaying;
    private int aiLevel;  // 1 = aléatoire, 2 = intermédiaire, 3 = avancé

    // Pour les hints (conseils)
    private int[] hintsRemaining = new int[2];
    private static final int MAX_HINTS = 3;


    public WaffleModel() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public WaffleModel(int width, int height) {
        this.width = width;
        this.height = height;

        // Initialiser le jeu
        initGame();

        // Créer l'IA
        aiPlayer = new AIPlayer(this);
    }

    public void initGame() {
        // Initialiser la gaufre
        waffle = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                waffle[y][x] = true;  // Toutes les cases sont initialement présentes
            }
        }

        // Initialiser l'état du jeu
        currentPlayer = 1;  // Le joueur 1 commence
        gameOver = false;
        winner = 0;

        // Initialiser l'historique des coups
        moveHistory = new ArrayList<>();
        currentMoveIndex = -1;

        // Initialiser les hints
        hintsRemaining[0] = MAX_HINTS;
        hintsRemaining[1] = MAX_HINTS;
    }

    public boolean isValidMove(int x, int y) {
        // Vérifier si la case est dans les limites
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }

        // Vérifier si la case est disponible
        return waffle[y][x];
    }

    public boolean makeMove(int x, int y) {
        if (gameOver || !isValidMove(x, y)) {
            return false;
        }

        // Créer un objet Move pour l'historique
        Move move = new Move(x, y, currentPlayer);

        // Appliquer le coup
        applyMove(move);

        // Ajouter le coup à l'historique et supprimer les coups après l'index actuel
        if (currentMoveIndex < moveHistory.size() - 1) {
            moveHistory = new ArrayList<>(moveHistory.subList(0, currentMoveIndex + 1));
        }
        moveHistory.add(move);
        currentMoveIndex++;

        // Vérifier si le jeu est terminé
        checkGameOver(x, y);

        // Changer de joueur si le jeu n'est pas terminé
        if (!gameOver) {
            switchPlayer();
        }

        return true;
    }

    private void applyMove(Move move) {
        int x = move.getX();
        int y = move.getY();

        // "Mordre" dans la gaufre : supprimer toutes les cases en bas à droite du coup joué
        for (int j = y; j < height; j++) {
            for (int i = x; i < width; i++) {
                waffle[j][i] = false;
            }
        }
    }

    private void checkGameOver(int x, int y) {
        // Le jeu est terminé si la case poisonnée (0,0) a été prise
        if (x == 0 && y == 0) {
            gameOver = true;
            winner = (currentPlayer == 1) ? 2 : 1;  // Le joueur qui a pris la case empoisonnée a perdu
        }
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;

        // Si c'est au tour de l'IA et que l'IA est activée
        if (currentPlayer == 2 && isAIPlaying) {
            // L'IA joue son coup
            Move aiMove = aiPlayer.makeMove(aiLevel);
            if (aiMove != null) {
                makeMove(aiMove.getX(), aiMove.getY());
            }
        }
    }

    public boolean undoMove() {
        if (currentMoveIndex < 0) {
            return false;  // Pas de coup à annuler
        }

        currentMoveIndex--;

        // Reconstruire l'état du jeu
        reconstructGameState();

        return true;
    }

    public boolean redoMove() {
        if (currentMoveIndex >= moveHistory.size() - 1) {
            return false;  // Pas de coup à rejouer
        }

        currentMoveIndex++;

        // Reconstruire l'état du jeu
        reconstructGameState();

        return true;
    }

    private void reconstructGameState() {
        // Réinitialiser la gaufre
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                waffle[y][x] = true;
            }
        }

        gameOver = false;
        winner = 0;

        // Rejouer tous les coups jusqu'à l'index actuel
        for (int i = 0; i <= currentMoveIndex; i++) {
            Move move = moveHistory.get(i);
            applyMove(move);

            // Vérifier si le dernier coup est la case empoisonnée
            if (i == currentMoveIndex && move.getX() == 0 && move.getY() == 0) {
                gameOver = true;
                winner = (move.getPlayer() == 1) ? 2 : 1;
            }
        }

         // Définir le joueur courant
        if (currentMoveIndex >= 0) {
            Move lastMove = moveHistory.get(currentMoveIndex);
            currentPlayer = (lastMove.getPlayer() == 1) ? 2 : 1;
        } else {
            currentPlayer = 1;  // Le joueur 1 commence par défaut
        }
    }

    public Move getHint() {
        if (gameOver) {
            return null;
        }
        int idx = currentPlayer - 1;       // 0 pour joueur 1, 1 pour joueur 2
        if (hintsRemaining[idx] <= 0) {
            return null;
        }
        hintsRemaining[idx]--;
        return aiPlayer.makeMove(3); // Utiliser l'IA de niveau avancé pour obtenir un bon coup
    }


    // Getters et setters

    public boolean[][] getWaffle() {
        return waffle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getWinner() {
        return winner;
    }

    public void setAIPlaying(boolean aiPlaying) {
        isAIPlaying = aiPlaying;
    }

    public void setAILevel(int level) {
        if (level >= 1 && level <= 3) {
            aiLevel = level;
        }
    }

    public int getHintsRemaining() {
        return hintsRemaining[currentPlayer - 1];
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public int getCurrentMoveIndex() {
        return currentMoveIndex;
    }
}