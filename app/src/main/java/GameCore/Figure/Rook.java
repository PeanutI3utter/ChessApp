package GameCore.Figure;

import com.example.chess.R;

import GameCore.MoveData;
import GameCore.Player;

import static GameCore.Direction.DOWN;
import static GameCore.Direction.LEFT;
import static GameCore.Direction.RIGHT;
import static GameCore.Direction.UP;

public class Rook extends Figure {
    public Rook(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.rookwhite : R.drawable.rookblack;
    }

    @Override
    public MoveData availableMoves(Figure[][] field) {
        MoveData md = new MoveData();
        horizontalMove(field, md.getAvailableMoves(), md.getAttackbleFields(), 8, LEFT, RIGHT);
        verticalMove(field, md.getAvailableMoves(), md.getAttackbleFields(), 8, UP, DOWN);
        return md;
    }
}
