package GameCore.Movement.MacroMovements;

import android.graphics.Point;

import java.util.LinkedList;

import GameCore.Figure.Figure;
import GameCore.Game;
import GameCore.Movement.MovementDescriber.MovementCategory;
import GameCore.Movement.atomicMovement.FigureMovement;
import GameCore.Movement.atomicMovement.Movements;
import GameCore.Utils.CustomIterator;

/**
 * special moves class
 */
public abstract class SpecialMove extends Move {
    protected LinkedList<Figure> involvedFigures;
    private LinkedList<Movements> movements;

    public SpecialMove() {
        movements = new LinkedList<Movements>();
        involvedFigures = new LinkedList<>();
    }

    /**
     * adds a micromovement to movement list
     *
     * @param movement
     */
    public void addMovement(Movements movement) {
        if (highlight == null)
            highlight = movement.getMoveTo();
        movements.add(movement);
        involvedFigures.add(movement.getMovingFig());
    }

    /**
     * @return list of micromovements
     */
    public LinkedList<Movements> getMovements() {
        return movements;
    }

    /**
     * adds a micromovement
     *
     * @param movingFigure moving figure
     * @param moveTo       moving coordinate
     */
    public void addMovement(Figure movingFigure, Point moveTo) {
        if (highlight == null)
            highlight = moveTo;
        movements.add(new FigureMovement(movingFigure, moveTo));
        involvedFigures.add(movingFigure);
    }

    /**
     * adds micromovement via offset and movement category
     *
     * @param movingFigure
     * @param offset
     * @param dj
     */
    public void addMovement(Figure movingFigure, int offset, MovementCategory dj) {
        if (highlight == null)
            highlight = new Point(movingFigure.getX() + dj.getX(), movingFigure.getY() + dj.getY());
        movements.add(new FigureMovement(movingFigure, offset, dj));
        involvedFigures.add(movingFigure);
    }

    /**
     * @return all figures involved in this special moves
     */
    public LinkedList<Figure> getInvolvedFigures() {
        return involvedFigures;
    }

    @Override
    public void processMoves(Game game) {
        while (hasMoves())
            popMove().processMove(game);
    }

    @Override
    public boolean hasMoves() {
        return movements.size() > 0;
    }

    @Override
    public Movements popMove() {
        assert movements.size() > 0;
        return movements.remove(0);
    }

    @Override
    public CustomIterator<Movements> getIterator() {
        return new CustomIterator<>(movements);
    }

    abstract public void specialMove(Game game);

}
