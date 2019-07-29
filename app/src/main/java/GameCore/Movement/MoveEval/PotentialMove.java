package GameCore.Movement.MoveEval;


import GameCore.Movement.MovementDescriber.MovementCategory;

/**
 * class that describes all the potential moves a figure can execute
 */
public class PotentialMove {
    private MovementCategory mc; // direction and/or jumps
    private int range; // range of move
    private int jumpOvers; // how many jumpovers can figure do?
    private boolean move; // is this move a plain move
    private boolean attack; // is this move a attack (can be both simultaneously)

    public PotentialMove(MovementCategory mc, int range, int jumpOvers, boolean move, boolean attack) {
        this.mc = mc;
        this.range = range;
        this.move = move;
        this.attack = attack;
        this.jumpOvers = jumpOvers;
    }

    public MovementCategory getMc() {
        return mc;
    }

    public int getRange() {
        return range;
    }

    public boolean isAttack() {
        return attack;
    }

    public boolean isMove() {
        return move;
    }

    public int getJumpOvers() {
        return jumpOvers;
    }
}
