package GameCore.Figure;

import android.graphics.Point;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Field;
import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.Direction;
import GameCore.Movement.SpecialMove;
import GameCore.Movement.SpecialMoveEval;
import GameCore.Player;

public class Pawn extends Figure {
    private GhostPawn clone;
    private Direction direction;
    private SpecialMoveEval pawnSpecialMove = ((field, figure) -> {
        if (!figure.hasMoved()) {
            int x = figure.getX();
            int y = figure.getY() + direction.getY() * 2;
            if (x < 0 | x > 7 | y < 0 | y > 7)
                return;
            Point fig = new Point(x, y);
            if (figure.isRestricted()) {
                if (!figure.getRestrictions().contains(fig))
                    return;
            }
            if (figure.getOwner().isThreatened()) {
                if (!figure.getOwner().getKing().getRestrictions().contains(new Point(getX(), getY() + getDirection().getY() * 2)))
                    ;
                return;
            }
            if (field.getFigure(fig) == null) {
                SpecialMove specialMove = new SpecialMove() {
                    @Override
                    public void move(Field field) {
                        Pawn selectedFig = (Pawn) mainFigure;
                        int xPawn = selectedFig.getX();
                        int yPawn = selectedFig.getY();
                        int yDir = selectedFig.getDirection().getY();
                        GhostPawn ghostPawn = new GhostPawn(selectedFig.getOwner(), xPawn, yPawn + yDir, selectedFig, selectedFig.getGame());
                        field.setField(ghostPawn, ghostPawn.getX(), ghostPawn.getY());
                        selectedFig.setClone(ghostPawn);
                        mainFigure.moveViaOffset(field, 2, selectedFig.getDirection());
                        mainFigure.setMoved(true);
                    }
                };
                specialMove.setFiguresInvolved(figure);
                specialMove.setHighlightPoint(new Point(x, y));
                figure.getMd().addSpecial(specialMove);
            }
        }
    });

    public Pawn(Player owner, int x, int y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.pawnwhite : R.drawable.pawnblack;
        direction = getOwner().player1() ? Direction.UP : Direction.DOWN;
    }

    /**
     * overwritten move method because of ghost pawn
     *
     * @param field
     * @param x
     * @param y
     */
    @Override
    public void move(Field field, int x, int y) {
        if (clone != null)
            deleteClone();
        super.move(field, x, y);
    }

    /**
     * overwritten move method because of ghost pawn
     *
     * @param field
     * @param position
     */
    @Override
    public void move(Field field, Point position) {
        if (clone != null)
            deleteClone();
        super.move(field, position);
    }

    /**
     * overwritten move method because of ghost pawn
     *
     * @param field
     * @param amount
     * @param direction
     */
    @Override
    public void moveViaOffset(Field field, int amount, Direction direction) {
        if (clone != null)
            deleteClone();
        super.moveViaOffset(field, amount, direction);
    }

    /**
     * overwritten attack method because of ghost pawn
     *
     * @param field
     * @param attackedFigure
     * @param position
     */
    @Override
    public void attack(Field field, Figure attackedFigure, Point position) {
        if (clone != null)
            deleteClone();
        super.attack(field, attackedFigure, position);
    }

    public GhostPawn getClone() {
        return clone;
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
        clone = null;
    }

    /**
     * @return direction which this pawn is headed in
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * refert to figure.updateMoveData()
     * @param field current playing field
     */
    @Override
    public void updateMoveData(Field field) {
        MoveData md = getMd();
        md.reset();
        availableFields(field, md.getAvailableMoves(), md.getAttackbleFields(), direction);
        pawnSpecialMove.checkSpecialMove(field, this);
        if (isRestricted()) {
            md.intersection(getRestrictions());
        }
        if (getOwner().isThreatened()) {
            md.intersection(getOwner().getKing().getRestrictions());
        }
    }

    /**
     * gets all available fields for this pawn
     * @param field
     * @param available
     * @param attackable
     * @param d
     */
    public void availableFields(Field field, ArrayList<Point> available, ArrayList<Point> attackable, Direction d) {
        int x = getX();
        int y = getY();
        int why = y + d.getY();
        King enemyKing = getGame().getQueue().getOtherPlayer(getOwner()).getKing();
        if (why < 8 & why >= 0) {
            if (field.getFigure(x, why) == null)
                available.add(new Point(x, why));

            if (x + 1 < 8) {
                Point point = new Point(x + 1, why);
                Figure fig = field.getFigure(x + 1, why);
                if (!(fig == null))
                    if (fig.getOwner() != getOwner())
                        attackable.add(point);
                if (enemyKing.isInVicinity(x + 1, why)) {
                    enemyKing.addBlackList(point);
                }
            }

            if (x - 1 >= 0) {
                Point point = new Point(x - 1, why);
                Figure fig = field.getFigure(x - 1, why);
                if (!(fig == null))
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(x - 1, why));
                if (enemyKing.isInVicinity(x + 1, why)) {
                    enemyKing.addBlackList(point);
                }
            }
        }
    }

    /**
     * special delete method for pawn
     * @param field
     */
    @Override
    public void delete(Field field) {
        if (clone != null)
            clone.delete(field);
        super.delete(field);
    }

}
