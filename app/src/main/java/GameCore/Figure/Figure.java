package GameCore.Figure;

import GameCore.Player;

public abstract class Figure {
    Player owner;
    int image;

    public Figure(Player owner){
        this.owner = owner;
    }

    public int getImage(){
        return image;
    }

    abstract public Integer[] availableMoves(Figure[][] field);
}
