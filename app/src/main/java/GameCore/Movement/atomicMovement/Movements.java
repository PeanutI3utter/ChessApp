package GameCore.Movement.atomicMovement;

import android.graphics.Point;

import Activities.Game;
import GameCore.Figure.Figure;

/**
 * class describing micromovements
 */
public abstract class Movements {
    abstract public void processMove(Game game);

    /**
     * @return point that gets covered by this move
     */
    abstract public Point getGroundCover();

    abstract public Point getMoveTo();

    abstract public Figure getMovingFig();
}
