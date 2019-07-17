package GameCore.Figure;

import android.graphics.Point;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Direction;
import GameCore.MoveData;
import GameCore.Player;

public class Pawn extends Figure {

    public Pawn(Player owner, int x, int y){
        super(owner, x, y);
        image = owner.player1() ? R.drawable.pawnwhite : R.drawable.pawnblack;
    }

    @Override
    public void availableMoves(Figure[][] field) {
        MoveData md = new MoveData();
        Direction d = getOwner().player1() ? Direction.UP : Direction.DOWN;
        availableFields(field, md.getAvailableMoves(), md.getAttackbleFields(), d);
        setMd(md);
    }

    public void availableFields(Figure[][] field, ArrayList<Point> available, ArrayList<Point> attackable, Direction d) {
        int x = getX();
        int y = getY();
        int why = y + d.getY();
        if (why < 8 & why >= 0) {
            if (field[x][why] instanceof PlaceHolder)
                available.add(new Point(x, why));
            if (x + 1 < 8) {
                Figure fig = field[x + 1][why];
                if (!(fig instanceof PlaceHolder))
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(x + 1, why));
            }
            if (x - 1 >= 0) {
                Figure fig = field[x - 1][why];
                if (!(fig instanceof PlaceHolder))
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(x - 1, why));
            }
        }
    }


}
