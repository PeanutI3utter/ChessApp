package GameCore.Figure;

import android.graphics.Point;

import java.util.ArrayList;

import GameCore.Field;
import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.Direction;
import GameCore.Movement.Jump;
import GameCore.Movement.Movement;
import GameCore.Player;

import static GameCore.Movement.Direction.DOWN;
import static GameCore.Movement.Direction.LEFT;
import static GameCore.Movement.Direction.RIGHT;
import static GameCore.Movement.Direction.UP;

/**
 * abstract model of a figure in chess
 */
public abstract class Figure {
    protected Point pos;
    int image;
    Game game;
    private boolean restricted;
    private Player owner;
    private MoveData md;
    private ArrayList<Point> restrictions;
    private boolean moved;


    public Figure(Player owner, int x, int y, Game game) {
        this.owner = owner;
        pos = new Point(x, y);
        md = new MoveData();
        this.game = game;
        moved = false;
    }

    public Figure(Player owner, Point pos, Game game) {
        this.owner = owner;
        this.pos = pos;
        md = new MoveData();
        this.game = game;
        moved = false;
    }

    /**
     * @return MoveData containing all possible movable fields and attackable fields
     */
    public MoveData getMd() {
        return md;
    }


    public void setMd(MoveData md) {
        this.md = md;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public int getY() {
        return pos.y;
    }


    public void setY(int y) {
        pos.y = y;
    }


    public int getX() {
        return pos.x;
    }


    public void setX(int x) {
        pos.x = x;
    }


    public Point getPos() {
        return pos;
    }


    public void setPos(Point pos) {
        this.pos = pos;
    }


    public Player getOwner() {
        return owner;
    }


    public int getImage() {
        return image;
    }


    public void setImage(int image) {
        this.image = image;
    }


    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Game getGame() {
        return game;
    }

    /**
     * returns true if figures movement is restricted due to its relative position to the king
     *
     * @return boolean value if figure is restricted
     */
    public boolean isRestricted() {
        return restricted;
    }

    /**
     * a figure is restricted if king is threatened or if the figures move could potentially threaten the king
     *
     * @param restricted
     */
    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    /**
     * returns false if figure has never moved
     *
     * @return
     */
    public boolean hasMoved() {
        return moved;
    }

    /**
     * @return set of fields figure is restricted to
     */
    public ArrayList<Point> getRestrictions() {
        return restrictions;
    }


    /**
     * @param restrictions the fields a figure can move to when figure is restriceted
     */
    public void setRestrictions(ArrayList<Point> restrictions) {
        this.restrictions = restrictions;
    }


    /**
     * updates fields a figure can move to
     *
     * @param field current playing field
     */
    abstract public void updateMoveData(Field field);

    public void delete(Field field) {
        getOwner().getFigures().remove(this);
        field.setField(null, this.getX(), this.getY());
    }

    public void move(Field field, int x, int y) {
        field.setField(null, this.getX(), this.getY());
        field.setField(this, x, y);
        setX(x);
        setY(y);
    }


    public void moveViaOffset(Field field, int amount, Direction direction) {
        int x = getX() + direction.getX() * amount;
        int y = getY() + direction.getY() * amount;
        field.setField(null, this.getX(), this.getY());
        field.setField(this, x, y);
        setX(x);
        setY(y);
    }


    /**
     * @param field playing field
     * @param range range a figure can move
     * @deprecated marks all fields figure can move to/ attack
     */
    public void horizontalMove(Field field, int range) {
        ArrayList<Point> available = getMd().getAvailableMoves();
        ArrayList<Point> attackable = getMd().getAttackbleFields();
        Direction[] directions = {RIGHT, LEFT};
        int range0 = range;
        for (Direction d : directions) {
            int dir = d.getX();
            for (int i = 1; pos.x + i * dir < 8 & pos.x + i * dir >= 0 && range > 0; i++) {
                int ex = pos.x + i * dir;
                Figure fig = field.getFigure(ex, pos.y);
                if (!(fig == null)) {
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(ex, pos.y));
                    break;
                }
                available.add(new Point(ex, pos.y));
                range--;
            }
            range = range0;
        }
    }


    /**
     * @param field playing field
     * @param range range a figure can move
     * @deprecated marks all fields figure can move to/ attack
     */
    public void verticalMove(Field field, int range) {
        ArrayList<Point> available = md.getAvailableMoves();
        ArrayList<Point> attackable = md.getAttackbleFields();
        int range0 = range;
        Direction[] directions = {UP, DOWN};
        for (Direction d : directions) {
            if (d != Direction.UP & d != Direction.DOWN)
                continue;
            int dir = d.getY();
            for (int i = 1; pos.y + i * dir >= 0 && pos.y + i * dir < 8 && range > 0; i++) {
                int why = pos.y + i * dir;
                Figure fig = field.getFigure(pos.x, why);
                if (!(fig == null)) {
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(pos.x, why));
                    break;
                }
                available.add(new Point(pos.x, why));
                range--;
            }
            range = range0;
        }
    }


    /**
     * marks all fields figure can move to/ attack
     *
     * @param field     playing field
     * @param range     range a figure can move
     * @param movements movements a figure is able to execute
     */
    public void hybridMove(Field field, int range, Movement... movements) {
        ArrayList<Point> av = md.getAvailableMoves();
        ArrayList<Point> at = md.getAttackbleFields();
        Player enemy = game.getQueue().getOtherPlayer(getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        int range0 = range;
        dir:
        for (Movement movement : movements) {
            ArrayList<Point> points = new ArrayList<>();
            Figure enemyInPath = null;
            int numOfEnemiesInPath = 0;
            int xDir = movement.getX();
            int yDir = movement.getY();
            int i = 1;
            int j = 1;
            if (movement instanceof Jump) {
                int x = pos.x + xDir;
                int y = pos.y + yDir;
                Point point = new Point(x, y);
                Figure checkingFig = field.getFigure(point);
                if (checkingFig == null) {
                    if (isInVicinity(x, y, kingX, kingY)) {
                        enemyKing.addBlackList(point);
                    }
                    av.add(point);
                    continue;
                }
                if (checkingFig.getOwner() != getOwner()) {
                    if (checkingFig instanceof King) {
                        enemy.setThreatened(true);
                        enemyKing.setRestrictions(point);
                        enemyKing.addThreatendBy(this);
                    }
                    at.add(point);
                }
                continue;
            }
            boolean isThreatened = false;
            while (pos.x + i * xDir < 8 & pos.x + i * xDir >= 0 & pos.y + i * yDir < 8 & pos.y + i * yDir >= 0 & range0 > 0) {
                int why = pos.y + i * yDir;
                int ex = pos.x + i * xDir;
                Figure fig = field.getFigure(ex, why);
                Point point = new Point(ex, why);
                if (fig == null) {
                    if (isThreatened) {
                        enemyKing.addBlackList(point);
                    } else {
                        av.add(point);
                        if (isInVicinity(ex, why, kingX, kingY) & numOfEnemiesInPath < 1)
                            enemyKing.addBlackList(point);
                    }
                } else if (fig instanceof GhostPawn) {
                    if (getOwner() == fig.getOwner()) {
                        if (isThreatened) {
                            enemyKing.addBlackList(point);
                        } else {
                            av.add(point);
                        }
                    } else
                        at.add(point);
                } else if (fig.getOwner() == getOwner()) {
                    break;
                } else if (fig instanceof King) {
                    if (numOfEnemiesInPath < 1) {
                        points.add(new Point(this.getX(), this.getY()));
                        enemy.setThreatened(true);
                        fig.setRestrictions(new ArrayList<>(points));
                        ((King) fig).addThreatendBy(this);
                        ((King) fig).addBlackList(new Point(ex + i * xDir, why + i * yDir));
                        at.add(point);
                    } else {
                        enemyInPath.setRestricted(true);
                        enemyInPath.setRestrictions(points);
                        break;
                    }
                } else {
                    if (numOfEnemiesInPath < 1) {
                        at.add(point);
                        numOfEnemiesInPath++;
                        enemyInPath = fig;
                    } else {
                        break;
                    }
                }
                points.add(point);
                i++;
                range--;
            }
            range = range0;
        }
    }

    /**
     * gets all the fields a
     *
     * @param field
     * @param jumps
     */
    public void jumpMoves(Field field, Jump... jumps) {
        ArrayList<Point> av = md.getAvailableMoves();
        ArrayList<Point> at = md.getAttackbleFields();
        Player enemy = game.getQueue().getOtherPlayer(getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        for (Jump jump : jumps) {
            int x = pos.x + jump.getX();
            int y = pos.y + jump.getY();
            if (x > 7 | x < 0 | y < 0 | y > 7)
                continue;
            Point point = new Point(x, y);
            Figure checkingFig = field.getFigure(point);
            if (checkingFig == null) {
                if (isInVicinity(x, y, kingX, kingY)) {
                    enemyKing.addBlackList(point);
                }
                av.add(point);
                continue;
            }
            if (checkingFig.getOwner() != getOwner()) {
                if (checkingFig instanceof King) {
                    enemy.setThreatened(true);
                    enemyKing.setRestrictions(point);
                    enemyKing.addThreatendBy(this);
                }
                at.add(point);
            } else {
                if (isInVicinity(x, y, kingX, kingY)) {
                    enemyKing.addBlackList(point);
                }
            }
        }
    }

    public void lineMoves(Field field, int range, Direction... directions) {
        ArrayList<Point> av = md.getAvailableMoves();
        ArrayList<Point> at = md.getAttackbleFields();
        Player enemy = game.getQueue().getOtherPlayer(getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        int range0 = range;
        dir:
        for (Direction d : directions) {
            ArrayList<Point> points = new ArrayList<>();
            Figure enemyInPath = null;
            int numOfEnemiesInPath = 0;
            int xDir = d.getX();
            int yDir = d.getY();
            int i = 1;
            boolean isThreatened = false;
            while (pos.x + i * xDir < 8 & pos.x + i * xDir >= 0 & pos.y + i * yDir < 8 & pos.y + i * yDir >= 0 & range > 0) {
                int why = pos.y + i * yDir;
                int ex = pos.x + i * xDir;
                Figure fig = field.getFigure(ex, why);
                Point point = new Point(ex, why);
                if (fig == null) {
                    if (isThreatened) {
                        enemyKing.addBlackList(point);
                    } else {
                        av.add(point);
                        if (isInVicinity(ex, why, kingX, kingY) & numOfEnemiesInPath < 1)
                            enemyKing.addBlackList(point);
                    }
                } else if (fig instanceof GhostPawn) {
                    if (getOwner() == fig.getOwner())
                        av.add(point);
                    else
                        at.add(point);
                } else if (fig.getOwner() == getOwner()) {
                    break;
                } else if (fig instanceof King) {
                    if (numOfEnemiesInPath < 1) {
                        points.add(new Point(this.getX(), this.getY()));
                        enemy.setThreatened(true);
                        fig.setRestrictions(new ArrayList<>(points));
                        ((King) fig).addThreatendBy(this);
                        ((King) fig).addBlackList(new Point(ex + i * xDir, why + i * yDir));
                        at.add(point);
                    } else {
                        enemyInPath.setRestricted(true);
                        enemyInPath.setRestrictions(points);
                        break;
                    }
                } else {
                    if (numOfEnemiesInPath < 1) {
                        at.add(point);
                        numOfEnemiesInPath++;
                        enemyInPath = fig;
                    } else {
                        break;
                    }
                }
                points.add(point);
                i++;
                range--;
            }
            range = range0;
        }
    }

    public boolean canAttack(Figure fig) {
        ArrayList<Point> attacks = getMd().getAttackbleFields();
        for (Point p : attacks) {
            if (p.x == fig.getX() & p.y == fig.getY())
                return true;
        }
        ArrayList<Point> av = getMd().getAvailableMoves();
        for (Point p : av) {
            if (p.x == fig.getX() & p.y == fig.getY())
                return true;
        }
        return false;
    }

    public boolean canAttack(Point pos) {
        return getMd().getAttackbleFields().contains(pos) | getMd().getAvailableMoves().contains(pos);
    }

    public void reset() {
        getMd().reset();
        setRestricted(false);
        setRestrictions(null);
    }

    public boolean isInVicinity(int x1, int y1, int x2, int y2) {
        return Math.abs((double) x1 - x2) <= 1 & Math.abs((double) y1 -y2) <= 1;
    }
}
