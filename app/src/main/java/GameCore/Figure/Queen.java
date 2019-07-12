package GameCore.Figure;

import com.example.chess.R;

import GameCore.Player;

public class Queen extends Figure {
    public Queen(Player owner) {
        super(owner);
        image = owner.player1() ? R.drawable.queenwhite : R.drawable.queenblack;
    }

    @Override
    public Integer[] availableMoves(Figure[][] field) {
        return new Integer[0];
    }
}
