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
        Pawn pawn = (Pawn) figure;
        int pawnYf = pawn.getY() + pawn.getDirection().getY();
        if (!figure.hasMoved()) {
            int x = figure.getX();
            int y = figure.getY() + direction.getY() * 2;
            if (x < 0 | x > 7 | y < 0 | y > 7)
                return;
            if(field.getFigure(x, figure.getY() + direction.getY()) != null && !(field.getFigure(x, figure.getY() + direction.getY()) instanceof  GhostPawn))
                return;
            Point fig = new Point(x, y);
            if (figure.isRestricted()) {
                if (!figure.getRestrictions().contains(fig))
                    return;
            }
            if (figure.getOwner().isThreatened()) {
                if (!figure.getOwner().getKing().getRestrictions().contains(new Point(getX(), getY() + getDirection().getY() * 2)))
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
                        mainFigure.moveViaOffset(2, selectedFig.getDirection());
                        mainFigure.setMoved(true);
                        selectedFig.setClone(ghostPawn);
                    }
                };
                specialMove.setFiguresInvolved(figure);
                specialMove.setHighlightPoint(new Point(x, y));
                figure.getMd().addSpecial(specialMove);
            }
        }else if(pawnYf + ((Pawn) figure).getDirection().getY() == 7 || pawnYf + ((Pawn) figure).getDirection().getY() == 0){
            SpecialMove specialMove = new SpecialMove() {
                @Override
                public void move(Field field) {
                    Pawn selectedFig = (Pawn) mainFigure;
                    selectedFig.moveViaOffset(1, selectedFig.getDirection());
                }
            };
            specialMove.setFiguresInvolved(figure);
            specialMove.setHighlightPoint(new Point(figure.getX(), figure.getY() + ((Pawn) figure).getDirection().getY()));
            specialMove.setOpenSelector(true);
            figure.getMd().addSpecial(specialMove);
        }
    });

    public Pawn(Player owner, Integer x, Integer y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.pawnwhite : R.drawable.pawnblack;
        direction = getOwner().player1() ? Direction.UP : Direction.DOWN;
    }

    /**
     * overwritten move method because of ghost pawn
     *
     * @param x
     * @param y
     */
    @Override
    public void move(int x, int y) {
        if (clone != null)
            clone.delete();
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
    public void moveViaOffset(int amount, Direction direction) {
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
        return clone;
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
            if (field.getFigure(x, why) == null || field.getFigure(x, why) instanceof  GhostPawn)
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
     */
    @Override
    public void delete() {
        if (clone != null)
            clone.delete();
        super.delete();
    }

}
