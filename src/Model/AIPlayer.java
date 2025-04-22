package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer implements Serializable {
    private final WaffleModel model;
    private final Random random;

    public AIPlayer(WaffleModel model) {
        this.model = model;
        this.random = new Random();
    }

    public Move makeMove(int level) {
        return switch (level) {
            case 1 -> makeRandomMove();
            case 2 -> makeIntermediateMove();
            case 3 -> makeAdvancedMove();
            default -> makeRandomMove();
        };
    }

    private Move makeRandomMove() {
        List<Move> validMoves = getValidMoves();
        if (validMoves.isEmpty()) {
            return null;
        }

        return validMoves.get(random.nextInt(validMoves.size()));
    }

    private Move makeIntermediateMove() {
        List<Move> validMoves = getValidMoves();

        // Éviter la case empoisonnée (0,0) si possible
        for (Move move : validMoves) {
            if (move.getX() == 0 && move.getY() == 0) {
                // Éviter la case empoisonnée
                continue;
            }

            // Vérifier si ce coup est gagnant
            if (isWinningMove(move)) {
                return move;
            }
        }

        // Si aucun coup gagnant n'a été trouvé, jouer aléatoirement parmi les coups non perdants
        List<Move> nonLosingMoves = new ArrayList<>();
        for (Move move : validMoves) {
            if (move.getX() != 0 || move.getY() != 0) {
                nonLosingMoves.add(move);
            }
        }

        if (!nonLosingMoves.isEmpty()) {
            return nonLosingMoves.get(random.nextInt(nonLosingMoves.size()));
        }

        // S'il n'y a plus que la case empoisonnée, on est obligé de la jouer
        return validMoves.get(0);
    }

    private Move makeAdvancedMove() {
        // Pour les petites tailles de gaufre, on peut calculer le coup optimal
        if (model.getWidth() <= 6 && model.getHeight() <= 6) {
            return findOptimalMove();
        } else {
            // Sinon, utiliser la stratégie intermédiaire
            return makeIntermediateMove();
        }
    }

    private Move findOptimalMove() {
        // Implémentation de l'algorithme minimax pour trouver le coup optimal
        // Pour simplifier, nous utiliserons une heuristique basée sur la parité

        List<Move> validMoves = getValidMoves();

        // Si une seule case reste (la case empoisonnée), on est obligé de la jouer
        if (validMoves.size() == 1) {
            return validMoves.get(0);
        }

        // Si on peut jouer le coup (1,0) ou (0,1), c'est souvent un bon choix
        for (Move move : validMoves) {
            if ((move.getX() == 1 && move.getY() == 0) || (move.getX() == 0 && move.getY() == 1)) {
                return move;
            }
        }

        // Sinon, essayer de laisser une position symétrique à l'adversaire
        for (Move move : validMoves) {
            if (move.getX() != 0 && move.getY() != 0 && move.getX() == move.getY()) {
                return move;
            }
        }

        // Si aucune stratégie spécifique n'est applicable, revenir à la stratégie intermédiaire
        return makeIntermediateMove();
    }

    private boolean isWinningMove(Move move) {
        // Vérifier si ce coup laisse uniquement la case empoisonnée à l'adversaire
        boolean[][] waffle = model.getWaffle();
        int width = model.getWidth();
        int height = model.getHeight();

        // Compter les cases restantes si on joue ce coup
        int remainingCells = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (waffle[y][x] && (y < move.getY() || x < move.getX())) {
                    remainingCells++;
                }
            }
        }

        // Si seule la case (0,0) reste, c'est un coup gagnant
        return remainingCells == 1 && waffle[0][0];
    }

    private List<Move> getValidMoves() {
        List<Move> validMoves = new ArrayList<>();
        boolean[][] waffle = model.getWaffle();
        int width = model.getWidth();
        int height = model.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (waffle[y][x]) {
                    validMoves.add(new Move(x, y, 2));  // L'IA est toujours le joueur 2
                }
            }
        }

        return validMoves;
    }
}