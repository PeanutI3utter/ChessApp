package GameCore.Figure;

import android.graphics.Point;

import com.example.chess.R;

import java.util.ArrayList;

import Activities.Game;
import GameCore.Field;
import GameCore.MoveData;
import GameCore.Movement.MacroMovements.MultiMove;
import GameCore.Movement.MacroMovements.SpecialMove;
import GameCore.Movement.MoveEval.PotentialMove;
import GameCore.Movement.MovementDescriber.Direction;
import GameCore.Movement.MovementDescriber.SpecialMoveEval;
import GameCore.PlayerTypes.Player;
import GameCore.Utils.Util;

import static GameCore.Movement.MovementDescriber.Direction.DOWN;
import static GameCore.Movement.MovementDescriber.Direction.DOWNLEFT;
import static GameCore.Movement.MovementDescriber.Direction.DOWNRIGHT;
import static GameCore.Movement.MovementDescriber.Direction.LEFT;
import static GameCore.Movement.MovementDescriber.Direction.RIGHT;
import static GameCore.Movement.MovementDescriber.Direction.UP;
import static GameCore.Movement.MovementDescriber.Direction.UPLEFT;
import static GameCore.Movement.MovementDescriber.Direction.UPRIGHT;

public class King extends Figure {
    private ArrayList<Figure> threatendBy;
    private ArrayList<Point> blackList;
    Direction[] directions = {UP, LEFT, RIGHT, DOWN, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT};
    private SpecialMoveEval kingSpecialMove = (game, figure) -> {
        Field field = game.getField();
        ArrayList<SpecialMove> specialMoves = new ArrayList<>();
        if (figure.getOwner().isThreatened())
            return null;
        King king = (King) figure;
        if (king.hasMoved())
            return null;
        int kingheight = king.getY();
        Figure rook1 = field.getFigure(7, kingheight);
        Figure rook2 = field.getFigure(0, kingheight);
        if (rook2 instanceof Rook) {
            boolean proceed = true;
            if (rook2.hasMoved())
                proceed = false;
            ArrayList<Point> blacklist = king.getBlackList();
            for (int i = 1; i < 4; i++) {
                if (field.getFigure(i, kingheight) != null)
                    proceed = false;
            }
            if (blacklist != null) {
                if (blacklist.contains(new Point(2, kingheight)))
                    proceed = false;
            }
            if (proceed) {
                MultiMove mm = new MultiMove();
                mm.addMovement(king, new Point(2, kingheight));
                mm.addMovement(rook2, new Point(3, kingheight));
                mm.setHighlight(new Point(2, kingheight));
                specialMoves.add(mm);
            }
        }
        if (rook1 instanceof Rook) {
            boolean proceed = true;
            if (rook1.hasMoved())
                proceed = false;
            ArrayList<Point> blacklist = king.getBlackList();
            for (int i = 5; i < 7; i++) {
                if (field.getFigure(i, kingheight) != null)
                    proceed = false;
            }
            if (blacklist != null) {
                if (blacklist.contains(new Point(6, kingheight)))
                    proceed = false;
            }
            if (proceed) {
                MultiMove mm = new MultiMove();
                mm.addMovement(king, new Point(6, kingheight));
                mm.addMovement(rook1, new Point(5, kingheight));
                mm.setHighlight(new Point(6, kingheight));
                specialMoves.add(mm);
            }
        }
        return specialMoves.size() > 0 ? specialMoves : null;
    };

    public King(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.kingwhite : R.drawable.kingblack;
        threatendBy = new ArrayList<>();
        blackList = new ArrayList<>();
        for (Direction direction : directions) {
            standardMoves.add(new PotentialMove(direction, 1, 0, true, true));
        }
        specialMovesEval.add(kingSpecialMove);
    }

    @Override
    public void updateMoveData() {
        MoveData md = getMd();
        md.reset();
        game.getMoveEvaluator().evalMoves(this);
        game.getMoveEvaluator().specialMoves(this);
        md.subtract(blackList);
    }

    @Override
    public void delete() {

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
            Util.intersectListTo1(getRestrictions(), restrictions);
        }
    }

    public void setRestrictions(Point p){
        if(p == null){
            super.setRestrictions(new ArrayList<>());
        } else if (getRestrictions() == null) {
            ArrayList<Point> newRestriction = new ArrayList<>();
            newRestriction.add(p);
            super.setRestrictions(newRestriction);
        } else {
            ArrayList<Point> al = new ArrayList<>();
            al.add(p);
            Util.intersectListTo1(getRestrictions(), al);
        }
    }

    public void addBlackList(Point point){
        Util.addWithoutDuplicates(blackList, point);
    }

    public ArrayList<Point> getBlackList() {
        return blackList;
    }

    public void addThreatendBy(Figure threatendBy) {
        Util.addWithoutDuplicates(this.threatendBy, threatendBy);
    }

    public ArrayList<Figure> getThreatendBy() {
        return threatendBy;
    }


}
