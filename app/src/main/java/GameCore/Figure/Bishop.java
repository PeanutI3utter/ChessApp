package GameCore.Figure;

import com.example.chess.R;

import GameCore.Player;

public class Bishop extends Figure{
    public Bishop(Player owner) {
        super(owner);
        image = owner.player1() ? R.drawable.bishopwhite : R.drawable.bishopblack;
    }

    @Override
    public Integer[] availableMoves(Figure[][] field) {
        return new Integer[0];
    }
}
