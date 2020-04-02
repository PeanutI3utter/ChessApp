package GameCore.Movement.MovementDescriber;

import java.util.ArrayList;

import Activities.Game;
import GameCore.Figure.Figure;
import GameCore.Movement.MacroMovements.SpecialMove;


public interface SpecialMoveEval {

    /**
     * checks if a special move is available
     */
    ArrayList<SpecialMove> checkSpecialMove(Game game, Figure figure);
}
