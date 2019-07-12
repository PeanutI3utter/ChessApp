package GameCore.Figure;

import com.example.chess.R;

import GameCore.Player;

public class Pawn extends Figure {

    public Pawn(Player owner){
        super(owner);
        image = owner.player1() ? R.drawable.pawnwhite : R.drawable.pawnblack;
    }

    @Override
    public Integer[] availableMoves(Figure[][] field) {
        return new Integer[0];
    }


}
