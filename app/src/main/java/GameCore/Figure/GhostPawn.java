package GameCore.Figure;


import GameCore.Game;
import GameCore.Player;

public class GhostPawn extends Figure {
    Pawn master;

    public GhostPawn(Player owner, Integer x, Integer y, Pawn master, Game game) {
        super(owner, x, y, game);
        this.master = master;
    }

    @Override
    public void updateMoveData() {
        return;
    }


    @Override
    public void onAttack() {
        master.delete();
        delete();
    }
}
