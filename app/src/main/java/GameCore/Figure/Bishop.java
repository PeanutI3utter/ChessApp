package GameCore.Figure;

import com.example.chess.R;

import GameCore.MoveData;
import GameCore.Player;

import static GameCore.Direction.DOWNLEFT;
import static GameCore.Direction.DOWNRIGHT;
import static GameCore.Direction.UPLEFT;
import static GameCore.Direction.UPRIGHT;

public class Bishop extends Figure{
    public Bishop(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.bishopwhite : R.drawable.bishopblack;
    }

    @Override
    public MoveData availableMoves(Figure[][] field) {
        MoveData md = new MoveData();
        diagonalMove(field, md.getAvailableMoves(), md.getAttackbleFields(), 8, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT);
        return md;
    }
}
