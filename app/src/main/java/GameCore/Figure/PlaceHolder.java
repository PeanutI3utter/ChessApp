package GameCore.Figure;


import android.util.Pair;

import java.util.ArrayList;

import GameCore.Player;

public class PlaceHolder extends Figure {
    public PlaceHolder(Player owner, int x, int y) {
        super(owner, x, y);
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> availableMoves(Figure[][] field) {
        return null;
    }
}
