package GameCore;

public class PlayerQueue {
    Player next;
    Player nextAfter;

    public PlayerQueue(Player Start, Player second){
        next = Start;
        nextAfter = second;
    }

    public Player getNext(){
        Player ret = next;
        next = nextAfter;
        nextAfter = ret;
        return ret;
    }
}
