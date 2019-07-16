package GameCore.Figure;

import android.util.Pair;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Player;

public class Knight extends Figure{
    public Knight(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.horsewhite : R.drawable.horseblack;

    }

    @Override
    public ArrayList<Pair<Integer, Integer>> availableMoves(Figure[][] field) {

        ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
        moves.add(new Pair(x + 1, y - 2));
        moves.add(new Pair(x - 1, y - 2));
        moves.add(new Pair(x + 1, y + 2));
        moves.add(new Pair(x - 1, y + 2));
        moves.add(new Pair(x + 2, y + 1));
        moves.add(new Pair(x + 2, y - 1));
        moves.add(new Pair(x - 2, y + 1));
        moves.add(new Pair(x - 2, y - 1));

        return valid(moves);
    }
}
