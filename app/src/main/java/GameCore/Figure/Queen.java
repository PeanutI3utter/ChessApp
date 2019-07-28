package GameCore.Figure;

import com.example.chess.R;

import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.Direction;
import GameCore.Player;

import static GameCore.Movement.Direction.DOWN;
import static GameCore.Movement.Direction.DOWNLEFT;
import static GameCore.Movement.Direction.DOWNRIGHT;
import static GameCore.Movement.Direction.LEFT;
import static GameCore.Movement.Direction.RIGHT;
import static GameCore.Movement.Direction.UP;
import static GameCore.Movement.Direction.UPLEFT;
import static GameCore.Movement.Direction.UPRIGHT;

public class Queen extends Figure {
    Direction[] directions = {UP, LEFT, RIGHT, DOWN, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT};


    public Queen(){
        super();
    }


    public Queen(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.queenwhite : R.drawable.queenblack;
    }

    @Override
    public void updateMoveData() {
        MoveData data = getMd();
        data.reset();
        lineMoves(8, directions);
        if (isRestricted()) {
            data.intersection(getRestrictions());
        }
        if (getOwner().isThreatened()) {
            data.intersection(getOwner().getKing().getRestrictions());
        }
    }

}
