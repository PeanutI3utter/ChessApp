package GameCore.Figure;

import com.example.chess.R;

import GameCore.MoveData;
import GameCore.Player;

import static GameCore.Direction.DOWN;
import static GameCore.Direction.DOWNLEFT;
import static GameCore.Direction.DOWNRIGHT;
import static GameCore.Direction.LEFT;
import static GameCore.Direction.RIGHT;
import static GameCore.Direction.UP;
import static GameCore.Direction.UPLEFT;
import static GameCore.Direction.UPRIGHT;

public class King extends Figure {

    public King(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.kingwhite : R.drawable.kingblack;
    }

    @Override
    public MoveData availableMoves(Figure[][] field) {
        MoveData md = new MoveData();
        horizontalMove(field, md.getAvailableMoves(), md.getAttackbleFields(), 1, UP, DOWN);
        verticalMove(field, md.getAvailableMoves(), md.getAttackbleFields(), 1, RIGHT, LEFT);
        diagonalMove(field, md.getAvailableMoves(), md.getAttackbleFields(), 1, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT);
        return md;
    }
}
