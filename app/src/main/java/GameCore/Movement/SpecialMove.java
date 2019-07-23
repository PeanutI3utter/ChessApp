package GameCore.Movement;

import android.graphics.Point;

import GameCore.Field;
import GameCore.Figure.Figure;

public abstract class SpecialMove {
    protected Figure[] figuresInvolved;
    protected Figure mainFigure;
    protected Point highlightPoint;
    protected boolean anAttack = false;
    protected Point position;

    abstract public void move(Field field);

    public boolean isAnAttack() {
        return anAttack;
    }

    public void setAnAttack(boolean anAttack) {
        this.anAttack = anAttack;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setMainFigure(Figure mainFigure) {
        this.mainFigure = mainFigure;
    }

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
