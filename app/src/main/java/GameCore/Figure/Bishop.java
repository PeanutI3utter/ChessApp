package GameCore.Figure;

import android.util.Pair;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Player;

public class Bishop extends Figure{
    public Bishop(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.bishopwhite : R.drawable.bishopblack;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> availableMoves(Figure[][] field) {
        ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            moves.add(new Pair(x + i, y + i));
            moves.add(new Pair(x - i, y -i ));
            moves.add(new Pair(x + i, y - i));
            moves.add(new Pair(x - i, y + i));
        }
        return valid(moves);
    }
}
