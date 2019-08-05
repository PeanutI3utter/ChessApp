package GameCore.Movement.MacroMovements;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import Activities.Game;
import GameCore.Figure.Figure;
import GameCore.Movement.MovementDescriber.MovementCategory;
import GameCore.Movement.atomicMovement.FigureMovement;
import GameCore.Movement.atomicMovement.Movements;
import GameCore.Utils.CustomIterator;

/**
 * Macro move with single micro movement
 */
public class SingleMove extends Move {
    private boolean hasMovesLeft; //refer to parent class
    private FigureMovement move;


    public SingleMove(FigureMovement figMove) {
        move = figMove;
        hasMovesLeft = true;
    }

    public SingleMove(Figure movingFigure, Point moveTo) {
        move = new FigureMovement(movingFigure, moveTo);
        hasMovesLeft = true;
        highlight = moveTo;
    }

    public SingleMove(Figure movingFigure, int offSet, MovementCategory dj) {
        move = new FigureMovement(movingFigure, offSet, dj);
        highlight = move.getMoveTo();
        hasMovesLeft = true;
    }

    @Override
    public boolean hasMoves() {
        return hasMovesLeft;
    }

    @Override
    public FigureMovement popMove() {
        hasMovesLeft = false;
        return move;
    }

    @Override
    public void processMoves(Game game) {
        Movements movement = popMove();
        int x = movement.getMovingFig().getX();
        int y = movement.getMovingFig().getY();
        String moved = movement.getMovingFig().hasMoved() ? "1" : "0";
        movement.processMove(game);
        game.getRecorder().record(movement, x, y, moved);
    }

    @Override
    public List<Point> getMoveToFields() {
        ArrayList<Point> out = new ArrayList<>();
        out.add(move.getMoveTo());
        return out;
    }

    @Override
    public CustomIterator<Movements> getIterator() {
        return new CustomIterator<>(move);
    }

    @Override
    public void modifyGame(Game game) {

    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
