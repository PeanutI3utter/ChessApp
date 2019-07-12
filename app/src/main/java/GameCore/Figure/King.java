package GameCore.Figure;

import com.example.chess.R;

import GameCore.Player;

public class King extends Figure {

    public King(Player owner) {
        super(owner);
        image = owner.player1() ? R.drawable.kingwhite : R.drawable.kingblack;
    }

    @Override
    public Integer[] availableMoves(Figure[][] field) {
        return new Integer[0];
    }
}
