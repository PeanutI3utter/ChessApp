package GameCore.Figure;

import android.graphics.Point;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Field;
import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.SpecialMove;
import GameCore.Movement.SpecialMoveEval;
import GameCore.Player;
import GameCore.Utils.Util;

import static GameCore.Movement.Direction.DOWN;
import static GameCore.Movement.Direction.DOWNLEFT;
import static GameCore.Movement.Direction.DOWNRIGHT;
import static GameCore.Movement.Direction.LEFT;
import static GameCore.Movement.Direction.RIGHT;
import static GameCore.Movement.Direction.UP;
import static GameCore.Movement.Direction.UPLEFT;
import static GameCore.Movement.Direction.UPRIGHT;

public class King extends Figure {
    private ArrayList<Figure> threatendBy;
    private ArrayList<Point> blackList;
    private SpecialMoveEval kingSpecialMove = (field, figure) -> {
        if (figure.getOwner().isThreatened())
            return;
        King king = (King) figure;
        if (king.hasMoved())
            return;
        int kingheight = king.getY();
        Figure rook1 = field.getFigure(7, kingheight);
        Figure rook2 = field.getFigure(0, kingheight);
        if (rook2 instanceof Rook) {
            boolean proceed = true;
            if (rook2.hasMoved())
                proceed = false;
            ArrayList<Point> restrictions = king.getRestrictions();
            for (int i = 1; i < 4; i++) {
                if (field.getFigure(i, kingheight) != null)
                    proceed = false;
            }
            if (restrictions != null) {
                if (restrictions.contains(new Point(2, kingheight)))
                    proceed = false;
            }
            if (proceed) {
                SpecialMove sm = new SpecialMove() {
                    @Override
                    public void move(Field field) {
                        mainFigure.moveViaOffset(field, 2, LEFT);
                        figuresInvolved[1].moveViaOffset(field, 3, RIGHT);
                    }
                };
                sm.setFiguresInvolved(king, rook2);
                sm.setHighlightPoint(new Point(2, kingheight));
                king.getMd().addSpecial(sm);
            }
        }
        if (rook1 instanceof Rook) {
            boolean proceed = true;
            if (rook1.hasMoved())
                proceed = false;
            ArrayList<Point> restrictions = king.getRestrictions();
            for (int i = 5; i < 7; i++) {
                if (field.getFigure(i, kingheight) != null)
                    proceed = false;
            }
            if (restrictions != null) {
                if (restrictions.contains(new Point(6, kingheight)))
                    proceed = false;
            }
            if (proceed) {
                SpecialMove sm = new SpecialMove() {
                    @Override
                    public void move(Field field) {
                        mainFigure.moveViaOffset(field, 2, RIGHT);
                        figuresInvolved[1].moveViaOffset(field, 2, LEFT);
                    }
                };
                sm.setFiguresInvolved(king, rook1);
                sm.setHighlightPoint(new Point(6, kingheight));
                king.getMd().addSpecial(sm);
            }
        }

    };

    public King(Player owner, int x, int y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.kingwhite : R.drawable.kingblack;
        threatendBy = new ArrayList<>();
        blackList = new ArrayList<>();
    }

    @Override
    public void updateMoveData(Field field) {
        MoveData md = getMd();
        md.reset();
        lineMoves(field, 1, UP, LEFT, RIGHT, DOWN, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT);
        kingSpecialMove.checkSpecialMove(field, this);
        md.subtract(blackList);
    }

    @Override
    public void delete(Field field) {

    }

    @Override
    public void reset() {
        super.setRestrictions(null);
        getMd().reset();
        blackList.clear();
        threatendBy.clear();
    }



    @Override
    public void setRestrictions(ArrayList<Point> restrictions){
        if(getRestrictions() == null){
            super.setRestrictions(restrictions);
        }else{
            Util.intersectArrayListTo1(getRestrictions(), restrictions);
        }
    }

    public void setRestrictions(Point p){
        if(p == null){
            super.setRestrictions(new ArrayList<>());
        }else{
            ArrayList<Point> al = new ArrayList<>();
            al.add(p);
            Util.intersectArrayListTo1(getRestrictions(), al);
        }
    }

    public void addBlackList(Point point){
        blackList.add(point);
    }


    public void addThreatendBy(Figure threatendBy) {
        Util.addWithoutDuplicates(this.threatendBy, threatendBy);
    }

    public ArrayList<Figure> getThreatendBy() {
        return threatendBy;
    }


}
