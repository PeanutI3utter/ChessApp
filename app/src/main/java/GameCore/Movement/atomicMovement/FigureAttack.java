package GameCore.Movement.atomicMovement;

import android.graphics.Point;

import GameCore.Figure.Figure;
import GameCore.Game;

/**
 * micromovement: attack;; refer to FigureMovement
 */
public class FigureAttack extends Movements {
    private Figure attackingFigure;
    private Figure attackedFigure;
    private Point attackOn;

    public FigureAttack(Figure attackingFigure, Figure attackedFigure, Point attackOn) {
        this.attackingFigure = attackingFigure;
        this.attackedFigure = attackedFigure;
        this.attackOn = attackOn;
    }


    public Figure getAttackingFigure() {
        return attackingFigure;
    }

    public Figure getAttackedFigure() {
        return attackedFigure;
    }

    public Point getAttackOn() {
        return attackOn;
    }


    @Override
    public void processMove(Game game) {
        attackingFigure.attack(attackedFigure, attackOn);
    }

    @Override
    public Point getGroundCover() {
        return attackOn;
    }

    @Override
    public Point getMoveTo() {
        return getAttackOn();
    }

    @Override
    public Figure getMovingFig() {
        return getAttackedFigure();
    }
}
