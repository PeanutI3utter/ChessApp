package GameCore.Figure;

import com.example.chess.R;

import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.MoveEval.PotentialMove;
import GameCore.Movement.MovementDescriber.Direction;
import GameCore.Player;

import static GameCore.Movement.MovementDescriber.Direction.DOWNLEFT;
import static GameCore.Movement.MovementDescriber.Direction.DOWNRIGHT;
import static GameCore.Movement.MovementDescriber.Direction.UPLEFT;
import static GameCore.Movement.MovementDescriber.Direction.UPRIGHT;


public class Bishop extends Figure{
    Direction[] directions = {UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT};

    public Bishop(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.bishopwhite : R.drawable.bishopblack;
        for (Direction direction : directions) {
            standardMoves.add(new PotentialMove(direction, 8, 0, true, true));
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
