package GameCore.GameModes;

import GameCore.Game;

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
