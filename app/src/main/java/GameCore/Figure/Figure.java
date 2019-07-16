package GameCore.Figure;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import GameCore.Player;

public abstract class Figure {
    protected Player owner;
    protected int image;
    protected int x;
    protected int y;




    public Figure(Player owner, int x, int y){
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    public int getImage(){
        return image;
    }

    abstract public ArrayList<Pair<Integer, Integer>> availableMoves(Figure[][] field);

    protected ArrayList<Pair<Integer, Integer>> valid(ArrayList<Pair<Integer, Integer>> moves) {
        ArrayList<Pair<Integer, Integer>> validMoves = new ArrayList<>();
        if (!moves.isEmpty()) {
            for (Pair move : moves) {
                if (!((int) move.first < 0 || (int) move.first > 7 || (int) move.second < 0 || (int) move.second > 7 )) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }
}
