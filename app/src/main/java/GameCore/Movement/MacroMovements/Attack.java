package GameCore.Movement.MacroMovements;

import android.graphics.Point;

import GameCore.Figure.Figure;
import GameCore.Game;
import GameCore.Movement.atomicMovement.FigureAttack;
import GameCore.Movement.atomicMovement.Movements;
import GameCore.Utils.CustomIterator;

/**
 * attack macro move
 */
public class Attack extends Move {
    private FigureAttack attack; // micro attack move
    private boolean moves; // refre to parent class


    public Attack(FigureAttack attack) {
        this.attack = attack;
        moves = true;
        highlight = attack.getAttackOn();
    }

    public Attack(Figure attackingFigure, Figure attackedFigure, Point attackOn) {
        attack = new FigureAttack(attackingFigure, attackedFigure, attackOn);
        moves = true;
        highlight = attackOn;
    }

    @Override
    public boolean hasMoves() {
        return moves;
    }

    @Override
    public FigureAttack popMove() {
        moves = false;
        return attack;
    }

    @Override
    public CustomIterator<Movements> getIterator() {
        return new CustomIterator<>(attack);
    }

    @Override
    public void processMoves(Game game) {
        attack.processMove(game);
    }

    @Override
    public void modifyGame(Game game) {

    }
}
