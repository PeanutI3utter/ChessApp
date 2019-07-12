package GameCore.Figure;

import com.example.chess.R;

import GameCore.Player;

public class Knight extends Figure{
    public Knight(Player owner) {
        super(owner);
        image = owner.player1() ? R.drawable.horsewhite : R.drawable.horseblack;

    }

    @Override
    public Integer[] availableMoves(Figure[][] field) {
        return new Integer[0];
    }
}