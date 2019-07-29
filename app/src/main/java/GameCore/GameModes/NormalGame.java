package GameCore.GameModes;

import GameCore.Game;

/**
 * normal game mode where a player wins if the other player is in check mate
 */
public class NormalGame extends Game {
    @Override
    public short checkWin() {
        if(currentPlayer.canMove())
            return 0;
        if(currentPlayer.isThreatened())
            return 1;
        return 2;
    }
}
