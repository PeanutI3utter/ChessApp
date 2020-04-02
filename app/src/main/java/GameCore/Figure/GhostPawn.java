package GameCore.Figure;


import Activities.Game;
import GameCore.PlayerTypes.Player;

public class GhostPawn extends Ghost {

    public GhostPawn(Player owner, Integer x, Integer y, Ghostable master, Game game) {
        super(owner, x, y, master, game);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void updateMoveData() {

    }

    @Override
    public void onNextTurn() {
        delete();
    }

    @Override
    public void delete() {
        if (game.getField().getFigure(pos.x, pos.y) == this) {
            super.delete();
        } else {
            getOwner().getFigures().remove(this);
            game.getRecorder().onDelete(this);
        }
    }

    public void deleteWithoutRecord() {
        if (game.getField().getFigure(pos.x, pos.y) == this) {
            super.deleteWithoutRecord();
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
