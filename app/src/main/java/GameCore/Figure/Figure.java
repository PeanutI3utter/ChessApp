package GameCore.Figure;

import android.graphics.Point;
import android.util.Pair;

import java.util.ArrayList;

import GameCore.Direction;
import GameCore.MoveData;
import GameCore.Player;

public abstract class Figure {
    Player owner;
    int image;
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

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    abstract public MoveData availableMoves(Figure[][] field);

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


    // marks all vertical moves
    public void horizontalMove(Figure[][] field, ArrayList<Point> available, ArrayList<Point> attackable, int range, Direction... directions) {
        for (Direction d : directions) {
            if (d != Direction.RIGHT && d != Direction.LEFT)
                continue;
            int dir = d.getX();
            for (int i = 1; x + i * dir < 8 & x + i * dir >= 0; i++) {
                int ex = x + i * dir;
                Figure fig = field[ex][y];
                if (!(fig instanceof PlaceHolder)) {
                    if (fig.owner != owner)
                        attackable.add(new Point(ex, y));
                    break;
                }
                available.add(new Point(ex, y));
            }
        }
    }


    /*marks all the horizontal moves*/
    public void verticalMove(Figure[][] field, ArrayList<Point> available, ArrayList<Point> attackable, int range, Direction... directions) {
        for (Direction d : directions) {
            if (d != Direction.UP & d != Direction.DOWN)
                continue;
            int dir = d.getY();
            for (int i = 1; y + i * dir >= 0 && y + i * dir < 8; i++) {
                int why = y + i * dir;
                Figure fig = field[x][why];
                if (!(fig instanceof PlaceHolder)) {
                    if (fig.owner != owner)
                        attackable.add(new Point(x, why));
                    break;
                }
                available.add(new Point(x, why));
            }
        }
    }


    public void diagonalMove(Figure[][] field, ArrayList<Point> available, ArrayList<Point> attackable, int range, Direction... directions) {
        for (Direction d : directions) {
            if (d != Direction.UPLEFT & d != Direction.UPRIGHT & d != Direction.DOWNLEFT & d != Direction.DOWNRIGHT)
                continue;
            int xDir = d.getX();
            int yDir = d.getY();
            int i = 1;
            int j = 1;
            while (x + i * xDir < 8 & x + i * xDir >= 0 & y + j * yDir < 8 & y + j * yDir >= 0) {
                int why = y + j * yDir;
                int ex = x + i * xDir;
                Figure fig = field[ex][why];
                if (!(fig instanceof PlaceHolder)) {
                    if (fig.owner != owner)
                        attackable.add(new Point(ex, why));
                    break;
                }
                available.add(new Point(ex, why));
                i++;
                j++;
            }
        }
    }

}
