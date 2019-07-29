package GameCore.Movement.atomicMovement;

import android.graphics.Point;

import GameCore.Figure.Figure;
import GameCore.Game;
import GameCore.Movement.MovementDescriber.MovementCategory;

/**
 * plain micromovement
 */
public class FigureMovement extends Movements {
    MovementCategory directionOrJump; // type of movement (for offset movement)
    int offSet;
    private Figure movingFig; // figure to be moved
    private Point moveTo; // moving coordinate
    private boolean movementByOffset; // true if movement is described by offset movement

    public FigureMovement(Figure movingFig, Point moveTo) {
        this.movingFig = movingFig;
        this.moveTo = moveTo;
        movementByOffset = false;
    }

    public FigureMovement(Figure movingFig, int offSet, MovementCategory directionOrJump) {
        this.movingFig = movingFig;
        this.offSet = offSet;
        this.directionOrJump = directionOrJump;
        moveTo = new Point(movingFig.getX() + directionOrJump.getX(), movingFig.getY() + directionOrJump.getY());
        movementByOffset = true;
    }

    public FigureMovement(Figure movingFig, int x, int y) {
        this.movingFig = movingFig;
        moveTo = new Point(x, y);
    }

    public Figure getMovingFig() {
        return movingFig;
    }

    public Point getMoveTo() {
        return moveTo;
    }

    @Override
    public void processMove(Game game) {
        if (movementByOffset)
            movingFig.moveViaOffset(offSet, directionOrJump);
        else
            movingFig.move(moveTo);
    }

    @Override
    public Point getGroundCover() {
        return moveTo;
    }
}
