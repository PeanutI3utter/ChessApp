package GameCore.Figure;


import GameCore.Game;
import GameCore.Player;

public class GhostPawn extends Figure {
    Pawn master;

    public GhostPawn(Player owner, Integer x, Integer y, Pawn master, Game game) {
        super(owner, x, y, game);
        this.master = master;
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void updateMoveData() {
    }


    @Override
    public void delete() {
        if (game.getField().getFigure(pos.x, pos.y) == this) {
            super.delete();
        } else {
            getOwner().getFigures().remove(this);
        }
    }

    @Override
    public void onAttack() {
        master.delete();
        delete();
    }
}
