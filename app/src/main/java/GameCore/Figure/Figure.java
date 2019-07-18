package GameCore.Figure;

import android.graphics.Point;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

import GameCore.Direction;
import GameCore.MoveData;
import GameCore.Player;

public abstract class Figure {
    protected Point pos;
    int image;
    private boolean moveable;
    private Player owner;
    private MoveData md;


    public Figure(Player owner, int x, int y){
        this.owner = owner;
        pos = new Point(x, y);
    }

    public Figure(Player owner, Point pos) {
        this.owner = owner;
        this.pos = pos;
    }

    public MoveData getMd(Figure[][] field) {
        if (md == null) {
            availableMoves(field);
        }
        return md;
    }

    public void setMd(MoveData md) {
        this.md = md;
    }

    public int getImage(){
        return image;
    }

    public int getY() {
        return pos.y;
    }

    public void setY(int y) {
        pos.y = y;
    }

    public int getX() {
        return pos.x;
    }

    public void setX(int x) {
        pos.x = x;
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    abstract public void availableMoves(Figure[][] field);

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
            for (int i = 1; pos.x + i * dir < 8 & pos.x + i * dir >= 0; i++) {
                int ex = pos.x + i * dir;
                Figure fig = field[ex][pos.y];
                if (!(fig instanceof PlaceHolder)) {
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(ex, pos.y));
                    break;
                }
                available.add(new Point(ex, pos.y));
            }
        }
    }


    /*marks all the horizontal moves*/
    public void verticalMove(Figure[][] field, ArrayList<Point> available, ArrayList<Point> attackable, int range, Direction... directions) {
        for (Direction d : directions) {
            if (d != Direction.UP & d != Direction.DOWN)
                continue;
            int dir = d.getY();
            for (int i = 1; pos.y + i * dir >= 0 && pos.y + i * dir < 8; i++) {
                int why = pos.y + i * dir;
                Figure fig = field[pos.x][why];
                if (!(fig instanceof PlaceHolder)) {
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(pos.x, why));
                    break;
                }
                available.add(new Point(pos.x, why));
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
            while (pos.x + i * xDir < 8 & pos.x + i * xDir >= 0 & pos.y + j * yDir < 8 & pos.y + j * yDir >= 0) {
                int why = pos.y + j * yDir;
                int ex = pos.x + i * xDir;
                Figure fig = field[ex][why];
                if (!(fig instanceof PlaceHolder)) {
                    if (fig.getOwner() != getOwner())
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
