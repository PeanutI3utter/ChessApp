package GameCore.Figure;

import android.util.Pair;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Player;

public class Pawn extends Figure {

    public Pawn(Player owner, int x, int y){
        super(owner, x, y);
        image = owner.player1() ? R.drawable.pawnwhite : R.drawable.pawnblack;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> availableMoves(Figure[][] field) {
        ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
        if (owner.player1()) {
            moves.add(new Pair(x - 1, y));
        } else {
            moves.add(new Pair(x + 1, y ));
        }
        return valid(moves);
    }


}
