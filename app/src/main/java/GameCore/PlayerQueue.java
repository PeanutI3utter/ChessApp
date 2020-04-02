package GameCore;

import GameCore.PlayerTypes.Player;

/**
 *
 */
public class PlayerQueue {
    private Player next;
    private Player nextAfter;

    public PlayerQueue(Player Start, Player second){
        next = Start;
        nextAfter = second;
    }

    /**
     * @return next player in turn
     */
    public Player next() {
        Player ret = next;
        next = nextAfter;
        nextAfter = ret;
        return ret;
    }

    public Player peak() {
        return next;
    }

    /*
        gets other player than the given one
     */
    public Player getOtherPlayer(Player player) {
        return player == next ? nextAfter : next;
    }
}
