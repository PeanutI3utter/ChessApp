package GameCore.Movement;

public enum Jump implements Movement {
    //U: up, D: down, L: left, R: right
    UUL(-1, -2), UUR(1, -2), ULL(-2, -1), URR(2, -1), DLL(-2, 1), DRR(2, 1), DDL(-1, 2), DDR(1, 2);

    private int x;
    private int y;

    Jump(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
