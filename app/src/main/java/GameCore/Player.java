package GameCore;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Figure.Figure;
import GameCore.Figure.King;

public abstract class Player {
    private boolean player1;
    private boolean threatened;
    private King King;
    private int playerColor;
    private ArrayList<Figure> figures;
    private boolean moves;
    private String name;

    public Player(String name, boolean player1) {
        figures = new ArrayList<>();
        setPlayer1(player1);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * gets the king figure of this player object
     *
     * @return king object
     */
    public GameCore.Figure.King getKing() {
        return King;
    }

    public void setKing(King king) {
        King = king;
    }

    public boolean isPlayer1() {
        return player1;
    }

    /**
     * sets this player to player1 who has the first move
     * @param player1
     */
    public void setPlayer1(boolean player1) {
        if (player1) {
            playerColor = R.drawable.whitecircle;
        } else {
            playerColor = R.drawable.blackcircle;
        }
        this.player1 = player1;
    }

    /**
     * add figure to players figure list
     *
     * @param f figure to be added to players figure list
     */
    public void addFigure(Figure f) {
        figures.add(f);
    }

    public void removeFigure(Figure f) {
        figures.remove(f);
    }

    public int getPlayerColor() {
        return playerColor;
    }

    /**
     * returns true if opponent has check on this player
     * @return
     */
    public boolean isThreatened() {
        return threatened;
    }

    public boolean canMove(){
        return moves;
    }

    public void setThreatened(boolean threatened) {
        this.threatened = threatened;
    }

    public boolean player1(){
        return player1;
    }

    public void update(Field field) {
        for (Figure f : figures) {
            f.updateMoveData();
            if(f.getMd().hasMoves())
                moves = true;
        }
    }

    public void onNextTurn(){
        for (Figure f : figures) {
            f.onNextTurn();
        }
    }

    public ArrayList<Figure> getFigures() {
        return figures;
    }

    public void reset() {
        setThreatened(false);
        for (Figure f : figures) {
            f.setRestricted(false);
            f.reset();
        }
        moves = false;
    }
}
