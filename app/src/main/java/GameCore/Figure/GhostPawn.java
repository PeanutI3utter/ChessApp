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

    /**
     * deletes itself from field and resets
     *
     * @param field
     */
    public void delete(Field field) {
        super.delete(field);
        master.deleteClone();
    }

    @Override
    public void onAttack(Field field) {
        master.delete(field);
        delete(field);
    }
}
