package GameCore.Figure;

import com.example.chess.R;

import GameCore.Player;

public class Rook extends Figure {
    public Rook(Player owner) {
        super(owner);
        image = owner.player1() ? R.drawable.rookwhite : R.drawable.rookblack;
    }

    @Override
    public Integer[] availableMoves(Figure[][] field) {
        return new Integer[0];
    }
}
