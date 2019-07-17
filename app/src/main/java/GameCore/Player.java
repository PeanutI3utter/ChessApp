package GameCore;

import android.graphics.Color;

import GameCore.Figure.Figure;

public abstract class Player {
    private boolean player1;
    private boolean threatened;
    private Figure King;
    private int playerColor;

    public Player(boolean player1) {
        setPlayer1(player1);
    }

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
        if (player1) {
            playerColor = Color.WHITE;
        } else {
            playerColor = Color.BLACK;
        }
        this.player1 = player1;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public boolean isThreatened() {
        return threatened;
    }

    public void setThreatened(boolean threatened) {
        this.threatened = threatened;
    }

    public boolean player1(){
        return player1;
    }
}
