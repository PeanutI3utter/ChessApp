package GameCore.Figure;

import com.example.chess.R;

import GameCore.Field;
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

    public Rook(Player owner, int x, int y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.rookwhite : R.drawable.rookblack;
    }

    @Override
    public void updateMoveData(Field field) {
        MoveData md = getMd();
        md.reset();
        lineMoves(field, 8, directions);
        if (isRestricted()) {
            md.intersection(getRestrictions());
        }
        if (getOwner().isThreatened()) {
            md.intersection(getOwner().getKing().getRestrictions());
        }
    }

}
