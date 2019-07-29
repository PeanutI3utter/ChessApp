package GameCore.Movement.MacroMovements;

import android.graphics.Point;

import GameCore.Game;
import GameCore.Movement.atomicMovement.Movements;
import GameCore.Utils.CustomIterator;


/**
 * abstract class of MacroMove
 */
public abstract class Move {
    protected Point highlight; // highlighting point
    protected boolean openSelector; // boolean value if move opens selector
    protected boolean modifyGame; // boolean value if move modifies game attributes


    abstract public boolean hasMoves(); // true if this move has micromovements left

    abstract public Movements popMove(); // gets next micromovement

    abstract public void processMoves(Game game); // process all micromoves

    public Point getHighlight() {
        return highlight;
    }

    public void setHighlight(Point highlight) {
        this.highlight = highlight;
    }

    public boolean opensSelector() {
        return openSelector;
    }

    public void setOpenSelector(boolean openSelector) {
        this.openSelector = openSelector;
    }

    public boolean modifiesGame() {
        return modifyGame;
    }

    /**
     * @return customIterator for micromovements
     */
    abstract public CustomIterator<Movements> getIterator();

    /**
     * custom equals so Move object can be compared with Point object
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            CustomIterator<Movements> customIterator = getIterator();
            while (customIterator.hasNext())
                if (customIterator.getNext().getGroundCover().equals(obj))
                    return true;
            return false;
        }
        return super.equals(obj);
    }


    @SuppressWarnings("EmptyMethod")
    abstract public void modifyGame(Game game);
}
