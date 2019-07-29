package GameCore.Figure;

import com.example.chess.R;

import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.MoveEval.PotentialMove;
import GameCore.Movement.MovementDescriber.Jump;
import GameCore.Movement.MovementDescriber.MovementCategory;
import GameCore.Player;

import static GameCore.Movement.MovementDescriber.Jump.DDL;
import static GameCore.Movement.MovementDescriber.Jump.DDR;
import static GameCore.Movement.MovementDescriber.Jump.DLL;
import static GameCore.Movement.MovementDescriber.Jump.DRR;
import static GameCore.Movement.MovementDescriber.Jump.ULL;
import static GameCore.Movement.MovementDescriber.Jump.URR;
import static GameCore.Movement.MovementDescriber.Jump.UUL;
import static GameCore.Movement.MovementDescriber.Jump.UUR;

public class Knight extends Figure{

    public Knight(){
        super();
    }

    public Knight(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.horsewhite : R.drawable.horseblack;
        Jump[] moves = {UUL, UUR, ULL, URR, DLL, DRR, DDL, DDR};
        for (MovementCategory jump : moves) {
            standardMoves.add(new PotentialMove(jump, 1, 0, true, true));
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
