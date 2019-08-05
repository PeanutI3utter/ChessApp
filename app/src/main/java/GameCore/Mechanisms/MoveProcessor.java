package GameCore.Mechanisms;

import Activities.Game;
import GameCore.Movement.MacroMovements.Move;
import GameCore.Movement.MacroMovements.SpecialMove;

/**
 * processor class that handles moves
 */
public class MoveProcessor {
    Game game;

    public MoveProcessor(Game game) {
        this.game = game;
    }

    public void processMove(Move move) {
        move.processMoves(game);
        if (move.modifiesGame()) {
            move.modifyGame(game);
        }
        if (move instanceof SpecialMove)
            ((SpecialMove) move).specialMove(game);
        if (move.opensSelector())
            //TODO create class for listening to selection
            game.openSelector();
        else
            game.nextTurn();
    }
}
