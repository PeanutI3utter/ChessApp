package GameCore;

import GameCore.Figure.Figure;

public abstract class Player {
    private boolean player1;
    private boolean threatened;
    private Figure King;

    public Figure getKing() {
        return King;
    }

    public void setKing(Figure king) {
        King = king;
    }

    public boolean isPlayer1() {
        return player1;
    }

    public void setPlayer1(boolean player1) {
        this.player1 = player1;
    }

    public boolean isThreatened() {
        return threatened;
    }

    public void setThreatened(boolean threatened) {
        this.threatened = threatened;
    }

    public Player(boolean player1){
        this.player1 = player1;
    }

    public boolean player1(){
        return player1;
    }
}
