package GameCore;

import android.graphics.Point;

import java.util.ArrayList;

import GameCore.Movement.MacroMovements.Attack;
import GameCore.Movement.MacroMovements.Move;
import GameCore.Movement.MacroMovements.SingleMove;
import GameCore.Movement.MacroMovements.SpecialMove;
import GameCore.Utils.Util;


/**
 * class that contains movement data of a figure
 */
public class MoveData {
    private ArrayList<SingleMove> availableMoves;
    private ArrayList<Attack> attackbleFields;
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
    public ArrayList<Attack> getAttacks() {
        return attackbleFields;
    }

    /**
     * set all attackablePoints
     *
     * @param attackbleFields
     */
    public void setAttackbleFields(ArrayList<Attack> attackbleFields) {
        this.attackbleFields = attackbleFields;
    }

    /**
     * @return all fields that can be moved to
     */
    public ArrayList<SingleMove> getAvailableMoves() {
        return availableMoves;
    }

    public void setAvailableMoves(ArrayList<SingleMove> availableMoves) {
        this.availableMoves = availableMoves;
    }

    public ArrayList<SpecialMove> getSpecialMoves() {
        return specialMoves;
    }

    public ArrayList<Point> getSpecialFields() {
        ArrayList<Point> out = new ArrayList<>();
        for (int i = 0; i < specialMoves.size(); i++)
            out.add(specialMoves.get(i).getHighlight());
        return out;
    }

    public SpecialMove getSpecialMove(Point field) {
        for (int i = 0; i < specialMoves.size(); i++) {
            SpecialMove specialMove = specialMoves.get(i);
            if (specialMove.getHighlight().equals(field))
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
     * @param move add move to available moves
     */
    public void addMoveable(SingleMove move) {
        availableMoves.add(move);
    }

    /**
     * adds a field to the set of fields considered to be attackble
     *
     * @param attack add attack to available attacks
     */
    public void addAttackable(Attack attack) {
        attackbleFields.add(attack);
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
        ArrayList<SingleMove> av1 = out.getAvailableMoves();
        ArrayList<Attack> at1 = out.getAttacks();
        ArrayList<SingleMove> av2 = out.getAvailableMoves();
        ArrayList<Attack> at2 = out.getAttacks();

        for (int i = 0; i < av1.size(); i++) {
            SingleMove move = av1.get(i);
            if (av2.contains(move))
                out.addMoveable(move);
        }
        for (int i = 0; i < at1.size(); i++) {
            Attack attack = at1.get(i);
            if (at2.contains(attack))
                out.addAttackable(attack);
        }
        return out;
    }

    /**
     * creates the intersection of one MoveData object and a list of points
     *
     * @param list_of_av
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public void intersection(ArrayList<Point> list_of_av) {
        if (list_of_av == null)
            return;
        if (list_of_av.isEmpty()) {
            availableMoves.clear();
            attackbleFields.clear();
            return;
        }
        for (int i = 0; i < availableMoves.size(); i++) {
            Move move = availableMoves.get(i);
            if (!list_of_av.contains(move)) {
                availableMoves.remove(move);
                i--;
            }
        }
        for (int i = 0; i < attackbleFields.size(); i++) {
            Attack attack = attackbleFields.get(i);
            if (!list_of_av.contains(attack)) {
                attackbleFields.remove(attack);
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
        Util.addWithoutDuplicates(out.getAttacks(), o1.getAttacks());
        Util.addWithoutDuplicates(out.getAttacks(), o2.getAttacks());
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
        Util.addWithoutDuplicates(collector.getAttacks(), source.getAttacks());
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
        Util.subtract(availableMoves, points);
        Util.subtract(attackbleFields, points);
    }

}
