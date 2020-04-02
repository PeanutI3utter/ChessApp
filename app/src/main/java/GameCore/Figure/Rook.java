package GameCore.Figure;

import com.example.chess.R;

import Activities.Game;
import GameCore.MoveData;
import GameCore.Movement.MoveEval.PotentialMove;
import GameCore.Movement.MovementDescriber.Direction;
import GameCore.PlayerTypes.Player;

import static GameCore.Movement.MovementDescriber.Direction.DOWN;
import static GameCore.Movement.MovementDescriber.Direction.LEFT;
import static GameCore.Movement.MovementDescriber.Direction.RIGHT;
import static GameCore.Movement.MovementDescriber.Direction.UP;


public class Rook extends Figure {
    Direction[] directions = {UP, LEFT, RIGHT, DOWN};

    public Rook(){
        super();
    }

    public Rook(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.rookwhite : R.drawable.rookblack;
        for (Direction d : directions) {
            standardMoves.add(new PotentialMove(d, 8, 0, true, true));
        }
    }

    @Override
    public void updateMoveData() {
        MoveData md = getMd();
        md.reset();
        game.getMoveEvaluator().evalMoves(this);
        if (isRestricted()) {
            md.intersection(getRestrictions());
        }
        if (getOwner().isThreatened()) {
            md.intersection(getOwner().getKing().getRestrictions());
        }
    }

}
