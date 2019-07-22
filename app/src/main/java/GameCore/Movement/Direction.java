package GameCore.Movement;

/**
 * Directions a figure can move to
 */
public enum Direction implements Movement {
    UP(0, -1, false), DOWN(0, 1, false), RIGHT(1, 0, false), LEFT(-1, 0, false), UPLEFT(-1, -1, true), UPRIGHT(1, -1, true), DOWNLEFT(-1, 1, true), DOWNRIGHT(1, 1, true);

    private int x;
    private int y;
    private boolean diag;

    Direction(int x, int y, boolean diag) {
        this.x = x;
        this.y = y;
        this.diag = diag;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDiag() {
        return diag;
    }
}
