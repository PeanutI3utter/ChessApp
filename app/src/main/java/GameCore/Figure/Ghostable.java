package GameCore.Figure;

import Activities.Game;
import GameCore.PlayerTypes.Player;

public abstract class Ghostable extends Figure {
    public Ghostable(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
    }

    abstract public void setGhost(Ghost ghost);
}
