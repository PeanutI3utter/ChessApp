package GameCore.Figure;

import android.util.Pair;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Player;

public class Rook extends Figure {
    public Rook(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.rookwhite : R.drawable.rookblack;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> availableMoves(Figure[][] field) {
        ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();

        for (int i = 1; i < 8; i++) {
            moves.add(new Pair(x, y + i));
            moves.add(new Pair(x + i, y));
            moves.add(new Pair(x, y - i));
            moves.add(new Pair(x - i, y));
        }

        return valid(moves);
    }
}
