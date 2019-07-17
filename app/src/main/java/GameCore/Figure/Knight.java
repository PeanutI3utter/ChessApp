package GameCore.Figure;

import android.graphics.Point;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.MoveData;
import GameCore.Player;

public class Knight extends Figure{
    private int[][] moves = {{1, -2}, {-1, -2}, {1, 2}, {-1, 2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
    public Knight(Player owner, int x, int y) {
        super(owner, x, y);
        image = owner.player1() ? R.drawable.horsewhite : R.drawable.horseblack;

    }

    @Override
    public void availableMoves(Figure[][] field) {
        MoveData md = new MoveData();
        ArrayList<Point> av = md.getAvailableMoves();
        ArrayList<Point> at = md.getAttackbleFields();
        for (int[] move : moves) {
            int ex = pos.x + move[0];
            int why = pos.y + move[1];
            if (ex < 8 & ex >= 0 & why < 8 & why >= 0) {
                Figure fig = field[ex][why];
                if (!(fig instanceof PlaceHolder)) {
                    if (fig.getOwner() != getOwner()) {
                        at.add(new Point(ex, why));
                    }
                    break;
                }
                av.add(new Point(ex, why));
            }
        }
        setMd(md);
    }
}
