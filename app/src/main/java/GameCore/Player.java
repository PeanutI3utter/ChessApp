package GameCore;

import java.util.Timer;

public abstract class Player {
    boolean player1;
    Timer timer;

    public Player(boolean player1){
        this.player1 = player1;
    }

    public boolean player1(){
        return player1;
    }
}
