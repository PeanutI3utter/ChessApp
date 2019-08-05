package GameCore.Figure;

import com.example.chess.R;

import Activities.Game;
import GameCore.MoveData;
import GameCore.Movement.MoveEval.PotentialMove;
import GameCore.Movement.MovementDescriber.Direction;
import GameCore.PlayerTypes.Player;

import static GameCore.Movement.MovementDescriber.Direction.DOWN;
import static GameCore.Movement.MovementDescriber.Direction.DOWNLEFT;
import static GameCore.Movement.MovementDescriber.Direction.DOWNRIGHT;
import static GameCore.Movement.MovementDescriber.Direction.LEFT;
import static GameCore.Movement.MovementDescriber.Direction.RIGHT;
import static GameCore.Movement.MovementDescriber.Direction.UP;
import static GameCore.Movement.MovementDescriber.Direction.UPLEFT;
import static GameCore.Movement.MovementDescriber.Direction.UPRIGHT;

public class Queen extends Figure {
    Direction[] directions = {UP, LEFT, RIGHT, DOWN, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT};


    public Queen(){
        super();
    }


    public Queen(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.queenwhite : R.drawable.queenblack;
        for (Direction direction : directions) {
            standardMoves.add(new PotentialMove(direction, 8, 0, true, true));
        }
    }

    @Override
    public void updateMoveData() {
        MoveData data = getMd();
        data.reset();
        game.getMoveEvaluator().evalMoves(this);
        if (isRestricted()) {
            data.intersection(getRestrictions());
        }
        if (getOwner().isThreatened()) {
            data.intersection(getOwner().getKing().getRestrictions());
        }
    }

}
