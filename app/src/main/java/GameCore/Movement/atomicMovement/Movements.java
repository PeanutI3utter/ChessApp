package GameCore.Movement.atomicMovement;

import android.graphics.Point;

import GameCore.Figure.Figure;
import GameCore.Game;

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
