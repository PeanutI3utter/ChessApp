package GameCore.Figure;


import GameCore.Field;
import GameCore.Game;
import GameCore.Player;

public class GhostPawn extends Figure {
    Pawn master;

    public GhostPawn(Player owner, int x, int y, Pawn master, Game game) {
        super(owner, x, y, game);
        this.master = master;
    }

    @Override
    public void updateMoveData(Field field) {
        return;
    }

    @Override
    public void delete(Field field) {
        master.delete(field);
    }

    public void delete(int i, Field field) {
        field.setField(null, this.getX(), this.getY());
    }
}
