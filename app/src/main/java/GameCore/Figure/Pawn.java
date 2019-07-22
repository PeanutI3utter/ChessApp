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
    private SpecialMoveEval pawnSpecialMove = new SpecialMoveEval() {
        @Override
        public void checkSpecialMove(Field field, Figure figure) {
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
                if (field.getFigure(fig) == null) {
                    SpecialMove specialMove = new SpecialMove() {
                        @Override
                        public void move(Field field) {
                            Pawn selectedFig = (Pawn) figure;
                            int xPawn = selectedFig.getX();
                            int yPawn = selectedFig.getY();
                            int yDir = selectedFig.getDirection().getY();
                            GhostPawn ghostPawn = new GhostPawn(selectedFig.getOwner(), xPawn, yPawn + yDir, selectedFig, selectedFig.getGame());
                            field.setField(ghostPawn, ghostPawn.getX(), ghostPawn.getY());
                            selectedFig.setClone(ghostPawn);
                            mainFigure.getGame().move(mainFigure, new Point(xPawn, yPawn + yDir * 2));
                        }
                    };
                    specialMove.setFiguresInvolved(figure);
                    specialMove.setHighlightPoint(new Point(x, y));
                    figure.getMd().addSpecial(specialMove);
                }
            }

        }
    };

    public Pawn(Player owner, int x, int y, Game game) {
        super(owner, x, y, game);
        image = owner.player1() ? R.drawable.pawnwhite : R.drawable.pawnblack;
        direction = getOwner().player1() ? Direction.UP : Direction.DOWN;
    }

    public GhostPawn getClone() {
        return clone;
    }

    public void setClone(GhostPawn clone) {
        this.clone = clone;
    }

    public Direction getDirection() {
        return direction;
    }

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

    public void availableFields(Field field, ArrayList<Point> available, ArrayList<Point> attackable, Direction d) {
        int x = getX();
        int y = getY();
        int why = y + d.getY();
        if (why < 8 & why >= 0) {
            if (field.getFigure(x, why) == null)
                available.add(new Point(x, why));
            if (x + 1 < 8) {
                Figure fig = field.getFigure(x + 1, why);
                if (!(fig == null))
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(x + 1, why));
            }
            if (x - 1 >= 0) {
                Figure fig = field.getFigure(x - 1, why);
                if (!(fig == null))
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(x - 1, why));
            }
        }
    }

    @Override
    public void delete(Field field) {
        clone.delete(0, field);
        super.delete(field);
    }

}
