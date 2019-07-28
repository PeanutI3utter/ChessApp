package GameCore.Figure;

import com.example.chess.R;

import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.Direction;
import GameCore.Player;

import static GameCore.Movement.Direction.DOWNLEFT;
import static GameCore.Movement.Direction.DOWNRIGHT;
import static GameCore.Movement.Direction.UPLEFT;
import static GameCore.Movement.Direction.UPRIGHT;


public class Bishop extends Figure{
    Direction[] directions = {UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT};

    public Bishop(){
        super();
    }
    public Bishop(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.bishopwhite : R.drawable.bishopblack;
    }


    @Override
    public void updateMoveData() {
        MoveData md = getMd();
        md.reset();
        lineMoves(8, directions);
        if (isRestricted()) {
            md.intersection(getRestrictions());
        }
        if (getOwner().isThreatened()) {
            md.intersection(getOwner().getKing().getRestrictions());
        }
    }

}
