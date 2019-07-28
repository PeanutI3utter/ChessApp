package GameCore.Figure;

import com.example.chess.R;

import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.Jump;
import GameCore.Player;

import static GameCore.Movement.Jump.DDL;
import static GameCore.Movement.Jump.DDR;
import static GameCore.Movement.Jump.DLL;
import static GameCore.Movement.Jump.DRR;
import static GameCore.Movement.Jump.ULL;
import static GameCore.Movement.Jump.URR;
import static GameCore.Movement.Jump.UUL;
import static GameCore.Movement.Jump.UUR;

public class Knight extends Figure{
    private Jump[] moves = {UUL, UUR, ULL, URR, DLL, DRR, DDL, DDR};

    public Knight(){
        super();
    }

    public Knight(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.horsewhite : R.drawable.horseblack;

    }

    @Override
    public void updateMoveData() {
        MoveData md = getMd();
        md.reset();
        jumpMoves(moves);
        if (isRestricted()) {
            md.intersection(getRestrictions());
        }
        if (getOwner().isThreatened()) {
            md.intersection(getOwner().getKing().getRestrictions());
        }
    }

}
