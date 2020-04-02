package GameCore.Figure;

import Activities.Game;
import GameCore.PlayerTypes.Player;

public abstract class Ghost extends Figure {
    protected Ghostable master;

    public Ghost(Player owner, Integer x, Integer y, Ghostable master, Game game) {
        super(owner, x, y, game);
        this.master = master;
        selectable = false;
        master.setGhost(this);
    }

    public Figure getMaster() {
        return master;
    }

    public void setMaster(Figure master) {
        this.master = (Ghostable) master;
    }
}
