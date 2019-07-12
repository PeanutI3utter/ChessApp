package GameCore.Figure;

import GameCore.Player;

public abstract class Figure {
    protected Player owner;
    protected int image;
    private int x;
    private int y;


    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Figure(Player owner){
        this.owner = owner;
    }

    public int getImage(){
        return image;
    }

    abstract public Integer[] availableMoves(Figure[][] field);
}
