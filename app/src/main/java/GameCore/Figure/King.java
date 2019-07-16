package GameCore.Figure;

import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Player;

public class King extends Figure {

    public King(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.kingwhite : R.drawable.kingblack;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> availableMoves(Figure[][] field) {
        ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
        moves.add(new Pair(x, y + 1));
        moves.add(new Pair(x, y - 1));
        moves.add(new Pair(x + 1, y));
        moves.add(new Pair(x - 1, y));
        moves.add(new Pair(x + 1, y - 1));
        moves.add(new Pair(x + 1, y + 1));
        moves.add(new Pair(x - 1, y + 1));
        moves.add(new Pair(x - 1, y - 1));

        return valid(moves);
    }
}
