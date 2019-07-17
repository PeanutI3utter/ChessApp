package GameCore.Figure;


import GameCore.MoveData;
import GameCore.Player;

public class PlaceHolder extends Figure {
    public PlaceHolder(Player owner, int x, int y) {
        super(owner, x, y);
    }

    @Override
    public MoveData availableMoves(Figure[][] field) {
        return null;
    }
}
