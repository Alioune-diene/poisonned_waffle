package Model;

import java.io.Serializable;

public class Move implements Serializable {
    private int x;
    private int y;
    private int player;

    public Move(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "Player " + player + " played at (" + x + ", " + y + ")";
    }
}