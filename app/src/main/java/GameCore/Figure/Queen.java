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

public class Queen extends Figure {
    public Queen(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.queenwhite : R.drawable.queenblack;
    }

    @Override
    public MoveData availableMoves(Figure[][] field) {
        MoveData data = new MoveData();
        horizontalMove(field, data.getAvailableMoves(), data.getAttackbleFields(), 8, RIGHT, LEFT);
        verticalMove(field, data.getAvailableMoves(), data.getAttackbleFields(), 8, UP, DOWN);
        diagonalMove(field, data.getAvailableMoves(), data.getAttackbleFields(), 9, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT);
        return data;
    }
}
