package GameCore.Figure;


import GameCore.Player;

public class PlaceHolder extends Figure {
    public PlaceHolder(Player owner) {
        super(owner);
    }

    @Override
    public Integer[] availableMoves(Figure[][] field) {
        return new Integer[0];
    }
}
