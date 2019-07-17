package GameCore;

public class PlayerQueue {
    private Player next;
    private Player nextAfter;

    public PlayerQueue(Player Start, Player second){
        next = Start;
        nextAfter = second;
    }

    public Player next() {
        Player ret = next;
        next = nextAfter;
        nextAfter = ret;
        return ret;
    }
}
