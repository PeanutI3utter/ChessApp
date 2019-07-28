package GameCore;

public abstract class Win {
    Game game;

    public Win(Game game){
        this.game = game;
    }

    /**
     *
     * @return 0 if game goes on, 1 if someone has one, 2 if draw
     */
    abstract public short checkWin();
}
