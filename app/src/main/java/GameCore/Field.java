package GameCore;

import android.graphics.Point;

import GameCore.Figure.Figure;


public class Field {
    private Figure[][] field;

    public Field() {
        this.field = new Figure[8][8];
    }

    /**
     * set figure to position x|y
     *
     * @param fig figure
     * @param x   x coordinate
     * @param y   y coordinate
     */
    public void setField(Figure fig, int x, int y) {
        if (x < 8 & y < 8)
            field[x][y] = fig;
    }

    /**
     * gets figure at position x|y
     *
     * @param x
     * @param y
     * @return
     */
    public Figure getFigure(int x, int y) {
        if (x >= 0 & x < field.length & y >= 0 & y < field.length)
            return field[x][y];
        return null;
    }

    /**
     * get figure via point object
     *
     * @param point
     * @return
     */
    public Figure getFigure(Point point) {
        return field[point.x][point.y];
    }
}
