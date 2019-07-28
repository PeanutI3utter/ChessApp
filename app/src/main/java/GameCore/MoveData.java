package GameCore;

import android.graphics.Point;

import java.util.ArrayList;

import GameCore.Movement.SpecialMove;
import GameCore.Utils.Util;


/**
 * class that contains movement data of a figure
 */
public class MoveData {
    private ArrayList<Point> availableMoves;
    private ArrayList<Point> attackbleFields;
    private ArrayList<SpecialMove> specialMoves;

    public MoveData() {
        availableMoves = new ArrayList<>();
        attackbleFields = new ArrayList<>();
        specialMoves = new ArrayList<>();
    }

    /**
     * contains all points that are attackable
     *
     * @return all attackable points
     */
    public ArrayList<Point> getAttackbleFields() {
        return attackbleFields;
    }

    /**
     * set all attackablePoints
     *
     * @param attackbleFields
     */
    public void setAttackbleFields(ArrayList<Point> attackbleFields) {
        this.attackbleFields = attackbleFields;
    }

    /**
     * @return all fields that can be moved to
     */
    public ArrayList<Point> getAvailableMoves() {
        return availableMoves;
    }

    public void setAvailableMoves(ArrayList<Point> availableMoves) {
        this.availableMoves = availableMoves;
    }

    public ArrayList<SpecialMove> getSpecialMoves() {
        return specialMoves;
    }

    public ArrayList<Point> getSpecialFields() {
        ArrayList<Point> out = new ArrayList<>();
        for (int i = 0; i < specialMoves.size(); i++)
            out.add(specialMoves.get(i).getHighlightPoint());
        return out;
    }

    public SpecialMove getSpecialMove(Point field) {
        for (int i = 0; i < specialMoves.size(); i++) {
            SpecialMove specialMove = specialMoves.get(i);
            if (specialMove.getHighlightPoint().equals(field))
                return specialMove;
        }
        return null;
    }

    public boolean hasMoves(){
        return attackbleFields.size() > 0 || availableMoves.size() > 0 || specialMoves.size() > 0;
    }

    /**
     * adds a field to the set of fields considered to be movable to
     *
     * @param point field coordinates as Points
     */
    public void addMoveable(Point point) {
        availableMoves.add(point);
    }

    /**
     * adds a field to the set of fields considered to be attackble
     *
     * @param point field coordinates as Points
     */
    public void addAttackable(Point point) {
        attackbleFields.add(point);
    }

    /**
     * adds a special move
     *
     * @param sm special move
     */
    public void addSpecial(SpecialMove sm) {
        if (sm == null)
            return;
        specialMoves.add(sm);
    }

    /**
     * clears all sets of movable and attackable fields
     */
    public void reset() {
        availableMoves.clear();
        attackbleFields.clear();
        specialMoves.clear();
    }

    /**
     * creates the intersection of two MoveData objects
     *
     * @param md other MoveData object
     * @return intersection
     */
    public MoveData intersection(MoveData md) {
        MoveData out = new MoveData();
        ArrayList<Point> av1 = out.getAvailableMoves();
        ArrayList<Point> at1 = out.getAttackbleFields();
        ArrayList<Point> av2 = out.getAvailableMoves();
        ArrayList<Point> at2 = out.getAttackbleFields();

        for (int i = 0; i < av1.size(); i++) {
            Point p = av1.get(i);
            if (av2.contains(p))
                out.addMoveable(p);
        }
        for (int i = 0; i < at1.size(); i++) {
            Point p = at1.get(i);
            if (at2.contains(p))
                out.addAttackable(p);
        }
        return out;
    }

    public void intersection(ArrayList<Point> list_of_av) {
        if (list_of_av == null)
            return;
        if (list_of_av.isEmpty()) {
            availableMoves.clear();
            attackbleFields.clear();
            return;
        }
        for (int i = 0; i < availableMoves.size(); i++) {
            Point p = availableMoves.get(i);
            if (!list_of_av.contains(p)) {
                availableMoves.remove(p);
                i--;
            }
        }
        for (int i = 0; i < attackbleFields.size(); i++) {
            Point p = attackbleFields.get(i);
            if (!list_of_av.contains(p)) {
                attackbleFields.remove(p);
                i--;
            }
        }
    }

    /**
     * unifies two MoveData objects
     *
     * @param o1
     * @param o2
     * @return Unification of o1 and o2
     */
    public MoveData unify(MoveData o1, MoveData o2) {
        MoveData out = new MoveData();
        Util.addWithoutDuplicates(out.getAvailableMoves(), o1.getAvailableMoves());
        Util.addWithoutDuplicates(out.getAvailableMoves(), o2.getAvailableMoves());
        Util.addWithoutDuplicates(out.getAttackbleFields(), o1.getAttackbleFields());
        Util.addWithoutDuplicates(out.getAttackbleFields(), o2.getAttackbleFields());
        return out;
    }

    /**
     * unifies two MoveData objects collector and source and unification to collector
     *
     * @param collector MoveData that unification should be saved to
     * @param source    other MoveData object
     */
    public void unifyWithoutCopy(MoveData collector, MoveData source) {
        Util.addWithoutDuplicates(collector.getAvailableMoves(), source.getAvailableMoves());
        Util.addWithoutDuplicates(collector.getAttackbleFields(), source.getAttackbleFields());
    }

    /**
     * unification of unlimited MoveData objects
     *
     * @param mds MoveData objects
     * @return Unification of all
     */
    public MoveData union(MoveData... mds) {
        MoveData out = new MoveData();
        for (MoveData md : mds) {
            unifyWithoutCopy(out, md);
        }
        return out;
    }

    /**
     * returns true if no movable fields nor attackable fields are left
     *
     * @return
     */
    public boolean isEmpty() {
        return availableMoves.isEmpty() & attackbleFields.isEmpty();
    }

    /**
     * substracts points from available fields
     *
     * @param points
     */
    public void subtract(ArrayList<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            availableMoves.remove(point);
            attackbleFields.remove(point);
        }
    }

}
