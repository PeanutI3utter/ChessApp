package GameCore.Figure;

import android.graphics.Point;

import com.example.chess.R;

import java.util.ArrayList;
import java.util.List;

import Activities.Game;
import GameCore.Field;
import GameCore.MoveData;
import GameCore.Movement.MacroMovements.Attack;
import GameCore.Movement.MacroMovements.Move;
import GameCore.Movement.MacroMovements.SingleMove;
import GameCore.Movement.MacroMovements.SpecialMove;
import GameCore.Movement.MoveEval.PotentialMove;
import GameCore.Movement.MovementDescriber.Direction;
import GameCore.Movement.MovementDescriber.MovementCategory;
import GameCore.Movement.MovementDescriber.SpecialMoveEval;
import GameCore.PlayerTypes.Player;

public class Pawn extends Ghostable {
    private Ghost clone;
    private Direction direction;
    private Direction[] attacks;
    private SpecialMoveEval pawnSpecialMove1 = ((game, figure) -> {
        Field field = game.getField();
        Pawn pawn = (Pawn) figure;
        int pawnYf = pawn.getY() + pawn.getDirection().getY();
        ArrayList<SpecialMove> specialMoves = new ArrayList<>();
        if (!figure.hasMoved()) {
            int x = figure.getX();
            int y = figure.getY() + direction.getY() * 2;
            if (x < 0 | x > 7 | y < 0 | y > 7)
                return null;
            if(field.getFigure(x, figure.getY() + direction.getY()) != null && !(field.getFigure(x, figure.getY() + direction.getY()) instanceof  GhostPawn))
                return null;
            Point fig = new Point(x, y);
            if (figure.isRestricted()) {
                if (!figure.getRestrictions().contains(fig))
                    return null;
            }
            if (figure.getOwner().isThreatened()) {
                if (!figure.getOwner().getKing().getRestrictions().contains(new Point(getX(), getY() + getDirection().getY() * 2)))
                    return null;
            }
            if (field.getFigure(fig) == null) {
                SpecialMove sm = new SpecialMove() {
                    @Override
                    public void specialMove(Game game) {
                        Pawn mainFig = (Pawn) getInvolvedFigures().get(0);
                        GhostPawn ghost = new GhostPawn(mainFig.getOwner(), mainFig.getX(), mainFig.getY() - mainFig.getDirection().getY(), mainFig, game);
                        game.getField().setField(ghost, ghost.getX(), ghost.getY());
                        game.figureCreated(ghost);
                    }


                    @Override
                    public List<Point> getMoveToFields() {
                        return null;
                    }

                    @Override
                    public void modifyGame(Game game) {

                    }
                };
                sm.addMovement(figure, fig);
                specialMoves.add(sm);
                return specialMoves;
            }
        }
        return null;
    });
    private SpecialMoveEval pawnSpecialMove2 = (((game1, figure) -> {
        Field field = game.getField();
        Pawn pawn = (Pawn) figure;
        int pawnYf = pawn.getY() + pawn.getDirection().getY();
        ArrayList<SpecialMove> specialMoves = new ArrayList<>();
        for (Attack attack : getMd().getAttacks()) {
            for (Point point : attack.getMoveToFields())
                if (point.y == 7 || point.y == 0) {
                    SpecialMove specialMove = new SpecialMove() {

                        @Override
                        public List<Point> getMoveToFields() {
                            return null;
                        }

                        @Override
                        public void modifyGame(Game game) {

                        }

                        @Override
                        public void specialMove(Game game) {

                        }
                    };
                    specialMove.addMovement(attack.getIterator().getNext());
                    specialMove.setOpenSelector(true);
                    specialMoves.add(specialMove);
                }
        }
        for (Move move : getMd().getAvailableMoves()) {
            for (Point point : move.getMoveToFields())
                if (point.y == 7 || point.y == 0) {
                    SpecialMove specialMove = new SpecialMove() {

                        @Override
                        public List<Point> getMoveToFields() {
                            return null;
                        }

                        @Override
                        public void modifyGame(Game game) {

                        }

                        @Override
                        public void specialMove(Game game) {

                        }
                    };
                    specialMove.addMovement(move.getIterator().getNext());
                    specialMove.setOpenSelector(true);
                    specialMoves.add(specialMove);
                }
        }
        if (specialMoves.size() > 0)
            return specialMoves;
        return null;
    }));

    public Pawn(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.pawnwhite : R.drawable.pawnblack;
        direction = getOwner().player1() ? Direction.UP : Direction.DOWN;
        Direction[] atUp = {Direction.UPLEFT, Direction.UPRIGHT};
        Direction[] atDown = {Direction.DOWNLEFT, Direction.DOWNRIGHT};
        attacks = direction == Direction.UP ? atUp : atDown;
        standardMoves.add(new PotentialMove(direction, 1, 0, true, false));
        for (Direction attackDirection : attacks) {
            standardMoves.add((new PotentialMove(attackDirection, 1, 0, false, true)));
        }
        specialMovesEval.add(pawnSpecialMove1);
        specialMovesEval.add(pawnSpecialMove2);
    }

    /**
     * overwritten move method because of ghost pawn
     *
     * @param x
     * @param y
     */
    @Override
    public void move(int x, int y) {
        super.move(x, y);

    }

    /**
     * overwritten move method because of ghost pawn
     *
     * @param position
     */
    @Override
    public void move(Point position) {
        if (clone != null)
            clone.delete();
        super.move(position);
    }

    /**
     * overwritten move method because of ghost pawn
     *
     * @param amount
     * @param direction
     */
    @Override
    public void moveViaOffset(int amount, MovementCategory direction) {
        if (clone != null)
            clone.delete();
        super.moveViaOffset(amount, direction);
    }

    /**
     * overwritten attack method because of ghost pawn
     *
     *
     * @param attackedFigure
     * @param position
     */
    @Override
    public void attack(Figure attackedFigure, Point position) {
        if (clone != null)
            clone.delete();
        super.attack(attackedFigure, position);
    }

    @Override
    public void onAttack() {
        if (clone != null)
            clone.delete();
        super.delete();
    }

    @Override
    public void onNextTurn() {
        deleteClone();
    }

    public GhostPawn getClone() {
        return (GhostPawn) clone;
    }

    public void exchange(Figure figure){
        if(figure == this)
            return;
        delete();
        getOwner().addFigure(figure);
        field.setField(figure, getX(), getY());
    }

    public Figure select(){
        return null;
    }

    /**
     * sets the clone for the pawn
     *
     * @param clone
     */
    public void setClone(GhostPawn clone) {
        this.clone = clone;
    }

    /**
     * deletes the clone of this pawn
     */
    public void deleteClone() {
        if(clone != null) {
            clone.delete();
            clone = null;
        }
    }

    /**
     * @return direction which this pawn is headed in
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * refert to figure.updateMoveData()
     */
    @Override
    public void updateMoveData() {
        MoveData md = getMd();
        md.reset();
        availableFields();
        game.getMoveEvaluator().specialMoves(this);
        if (isRestricted()) {
            md.intersection(getRestrictions());
        }
        if (getOwner().isThreatened()) {
            md.intersection(getOwner().getKing().getRestrictions());
        }
    }

    /**
     * gets all available fields for this pawn
     */
    public void availableFields() {
        int x = getX();
        int y = getY();
        int why = y + getDirection().getY();
        King enemyKing = getGame().getQueue().getOtherPlayer(getOwner()).getKing();
        if (why < 8 & why >= 0) {
            if (field.getFigure(x, why) == null || field.getFigure(x, why) instanceof  GhostPawn)
                this.getMd().getAvailableMoves().add(new SingleMove(this, new Point(x, why)));

            if (x + 1 < 8) {
                Point point = new Point(x + 1, why);
                Figure fig = field.getFigure(x + 1, why);
                if (!(fig == null))
                    if (fig.getOwner() != getOwner()) {
                        if (fig == enemyKing) {
                            enemyKing.getOwner().setThreatened(true);
                            enemyKing.addThreatendBy(this);
                            enemyKing.setRestrictions(this.getPos());
                        }
                        getMd().getAttacks().add(new Attack(this, fig, point));
                    }
                if (enemyKing.isInVicinity(x + 1, why)) {
                    enemyKing.addBlackList(point);
                }
            }

            if (x - 1 >= 0) {
                Point point = new Point(x - 1, why);
                Figure fig = field.getFigure(x - 1, why);
                if (!(fig == null))
                    if (fig.getOwner() != getOwner()) {
                        if (fig == enemyKing) {
                            enemyKing.getOwner().setThreatened(true);
                            enemyKing.addThreatendBy(this);
                            enemyKing.setRestrictions(this.getPos());
                        }
                        getMd().getAttacks().add(new Attack(this, fig, point));
                    }
                if (enemyKing.isInVicinity(x - 1, why)) {
                    enemyKing.addBlackList(point);
                }
            }
        }
    }


    /**
     * special delete method for pawn
     */
    @Override
    public void delete() {
        if (clone != null)
            clone.delete();
        super.delete();
    }

    @Override
    public void setGhost(Ghost ghost) {
        this.clone = ghost;
    }
}
