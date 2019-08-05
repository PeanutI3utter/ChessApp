package GameCore.Movement.MacroMovements;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Activities.Game;
import GameCore.Figure.Figure;
import GameCore.Movement.MovementDescriber.MovementCategory;
import GameCore.Movement.atomicMovement.FigureMovement;
import GameCore.Movement.atomicMovement.Movements;
import GameCore.Utils.CustomIterator;

public class MultiMove extends SpecialMove {
    private LinkedList<FigureMovement> movements;

    public MultiMove() {
        movements = new LinkedList<>();
    }

    public void addMovement(FigureMovement movement) {
        if (highlight == null)
            highlight = movement.getMoveTo();
        movements.add(movement);
    }

    public void addMovement(Figure movingFigure, Point moveTo) {
        if (highlight == null)
            highlight = moveTo;
        movements.add(new FigureMovement(movingFigure, moveTo));
    }

    public void addMovement(Figure movingFigure, int offset, MovementCategory dj) {
        FigureMovement newMovement = new FigureMovement(movingFigure, offset, dj);
        if (highlight == null)
            highlight = newMovement.getMoveTo();
        movements.add(newMovement);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public boolean hasMoves() {
        return movements.size() > 0;
    }

    @Override
    public FigureMovement popMove() {
        return movements.remove(0);
    }

    @Override
    public void processMoves(Game game) {
        while (hasMoves()) {
            Movements movement = popMove();
            int x = movement.getMovingFig().getX();
            int y = movement.getMovingFig().getY();
            String moved = movement.getMovingFig().hasMoved() ? "1" : "0";
            movement.processMove(game);
            game.getRecorder().record(movement, x, y, moved);
        }
    }

    @Override
    public List<Point> getMoveToFields() {
        ArrayList<Point> out = new ArrayList<>();
        for (Movements movement : movements)
            out.add(movement.getMoveTo());
        return out;
    }

    @Override
    public CustomIterator<Movements> getIterator() {
        return new CustomIterator<>(movements);
    }

    @Override
    public void specialMove(Game game) {

    }

    @Override
    public void modifyGame(Game game) {

    }

}
