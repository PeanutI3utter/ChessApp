package GameCore.Movement;

import android.graphics.Point;

import GameCore.Field;
import GameCore.Figure.Figure;

public abstract class SpecialMove {
    protected Figure[] figuresInvolved;
    protected Figure mainFigure;
    protected Point highlightPoint;

    abstract public void move(Field field);

    public Point getHighlightPoint() {
        return highlightPoint;
    }

    public void setHighlightPoint(Point highlightPoint) {
        this.highlightPoint = highlightPoint;
    }

    public void setFiguresInvolved(Figure... figuresInvolved) {
        this.figuresInvolved = figuresInvolved;
        mainFigure = figuresInvolved[0];
    }
}
