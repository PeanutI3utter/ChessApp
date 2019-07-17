package GameCore;

import android.graphics.Point;

import java.util.ArrayList;


/**
 * class that contains movement data of a figure
 */
public class MoveData {
    private ArrayList<Point> availableMoves;
    private ArrayList<Point> attackbleFields;

    public MoveData() {
        availableMoves = new ArrayList<>();
        attackbleFields = new ArrayList<>();
    }

    public ArrayList<Point> getAttackbleFields() {
        return attackbleFields;
    }

    public void setAttackbleFields(ArrayList<Point> attackbleFields) {
        this.attackbleFields = attackbleFields;
    }

    public ArrayList<Point> getAvailableMoves() {
        return availableMoves;
    }

    public void setAvailableMoves(ArrayList<Point> availableMoves) {
        this.availableMoves = availableMoves;
    }
}
