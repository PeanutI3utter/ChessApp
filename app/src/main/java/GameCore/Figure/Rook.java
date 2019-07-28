package GameCore.Figure;

import com.example.chess.R;

import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.Direction;
import GameCore.Player;

import static GameCore.Movement.Direction.DOWN;
import static GameCore.Movement.Direction.LEFT;
import static GameCore.Movement.Direction.RIGHT;
import static GameCore.Movement.Direction.UP;


public class Rook extends Figure {
    Direction[] directions = {UP, LEFT, RIGHT, DOWN};

    public Rook(){
        super();
    }

    public Rook(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.rookwhite : R.drawable.rookblack;
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
