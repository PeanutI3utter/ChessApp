package GameCore.Figure;

import android.graphics.Point;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Activities.Game;
import GameCore.Exceptions.ParsingError;
import GameCore.Field;
import GameCore.MoveData;
import GameCore.Movement.MacroMovements.Attack;
import GameCore.Movement.MacroMovements.Move;
import GameCore.Movement.MacroMovements.SingleMove;
import GameCore.Movement.MacroMovements.SpecialMove;
import GameCore.Movement.MoveEval.PotentialMove;
import GameCore.Movement.MovementDescriber.Direction;
import GameCore.Movement.MovementDescriber.Jump;
import GameCore.Movement.MovementDescriber.MovementCategory;
import GameCore.Movement.MovementDescriber.SpecialMoveEval;
import GameCore.PlayerTypes.Player;

import static GameCore.Movement.MovementDescriber.Direction.DOWN;
import static GameCore.Movement.MovementDescriber.Direction.UP;

/**
 * abstract model of a figure in chess
 */
public abstract class Figure {
    protected Point pos;
    protected int image;
    protected Game game;
    private boolean restricted;
    private Player owner;
    private MoveData md;
    private ArrayList<Point> restrictions;
    private boolean moved;
    protected Field field;
    protected ArrayList<PotentialMove> standardMoves;
    protected ArrayList<SpecialMoveEval> specialMovesEval;
    protected boolean selectable = true;

    public Figure(){
        md = new MoveData();
    }

    public Figure(Player owner, Integer x, Integer y, Game game) {
        this.owner = owner;
        pos = new Point(x, y);
        md = new MoveData();
        this.game = game;
        field = game.getField();
        moved = false;
        standardMoves = new ArrayList<>();
        specialMovesEval = new ArrayList<>();
    }

    public Figure(Player owner, Point pos, Game game) {
        this.owner = owner;
        this.pos = pos;
        md = new MoveData();
        this.game = game;
        field = game.getField();
        moved = false;
        standardMoves = new ArrayList<>();
        specialMovesEval = new ArrayList<>();
    }

    /**
     * @return MoveData containing all possible movable fields and attackable fields
     */
    public MoveData getMd() {
        return md;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setField(Field field) {
        this.field = field;
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

    public static Figure fromString(String figureDescription, Player player1, Player player2, Field field, Game game) throws ParsingError {
        Figure newFigure = null;
        String[] token = figureDescription.split(",");
        Player owner = token[4].equalsIgnoreCase("1") ? player1 : player2;
        boolean moved = token[5].equalsIgnoreCase("1");
        Integer x = Integer.parseInt(token[1]);
        Integer y = Integer.parseInt(token[2]);
        if (token[0].equalsIgnoreCase("1")) {
            Figure master = field.getFigure(Integer.parseInt(token[6]), Integer.parseInt(token[7]));
            try {
                newFigure = getGhost(token[3], owner, x, y, (Ghostable) master, game);
            } catch (ClassNotFoundException e) {
                throw new ParsingError("Error while parsing");
            } catch (NoSuchMethodException e) {
                throw new ParsingError("Error while parsing");
            } catch (IllegalAccessException e) {
                throw new ParsingError("Error while parsing");
            } catch (InvocationTargetException e) {
                throw new ParsingError("Error while parsing");
            } catch (InstantiationException e) {
                throw new ParsingError("Error while parsing");
            }
        } else {
            try {
                newFigure = getFigure(token[3], owner, x, y, game);
            } catch (ClassNotFoundException e) {
                throw new ParsingError("Error while parsing");
            } catch (NoSuchMethodException e) {
                throw new ParsingError("Error while parsing");
            } catch (IllegalAccessException e) {
                throw new ParsingError("Error while parsing");
            } catch (InvocationTargetException e) {
                throw new ParsingError("Error while parsing");
            } catch (InstantiationException e) {
                throw new ParsingError("Error while parsing");
            }
        }
        if (newFigure != null) {
            if (newFigure instanceof King)
                owner.setKing((King) newFigure);
            owner.addFigure(newFigure);
            newFigure.setMoved(moved);
            field.setField(newFigure, x, y);
        }
        return newFigure;
    }

    public ArrayList<PotentialMove> getStandardMoves() {
        return standardMoves;
    }

    public ArrayList<SpecialMoveEval> getSpecialMoves() {
        return specialMovesEval;
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
     * gets move that has been chosen/clicked
     *
     * @param clickedPoint
     * @return
     */
    public Move getMove(Point clickedPoint) {
        for (SpecialMove sm : getMd().getSpecialMoves()) {
            if (sm.getHighlight().equals(clickedPoint))
                return sm;
        }
        for (SingleMove single : getMd().getAvailableMoves()) {
            if (single.getHighlight().equals(clickedPoint))
                return single;
        }
        for (Attack attack : getMd().getAttacks()) {
            if (attack.getHighlight().equals(clickedPoint))
                return attack;
        }
        return null;
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
     */
    abstract public void updateMoveData();

    public static Figure fromStringWithoutSet(String figureDescription, Player player1, Player player2, Field field, Game game) throws ParsingError {
        Figure newFigure = null;
        String[] token = figureDescription.split(",");
        Player owner = token[4].equalsIgnoreCase("1") ? player1 : player2;
        boolean moved = token[5].equalsIgnoreCase("1");
        Integer x = Integer.parseInt(token[1]);
        Integer y = Integer.parseInt(token[2]);
        if (token[0].equalsIgnoreCase("1")) {
            Figure master = field.getFigure(Integer.parseInt(token[6]), Integer.parseInt(token[7]));
            try {
                newFigure = getGhost(token[3], owner, x, y, (Ghostable) master, game);
            } catch (ClassNotFoundException e) {
                throw new ParsingError("Error while parsing");
            } catch (NoSuchMethodException e) {
                throw new ParsingError("Error while parsing");
            } catch (IllegalAccessException e) {
                throw new ParsingError("Error while parsing");
            } catch (InvocationTargetException e) {
                throw new ParsingError("Error while parsing");
            } catch (InstantiationException e) {
                throw new ParsingError("Error while parsing");
            }
        } else {
            try {
                newFigure = getFigure(token[3], owner, x, y, game);
            } catch (ClassNotFoundException e) {
                throw new ParsingError("Error while parsing");
            } catch (NoSuchMethodException e) {
                throw new ParsingError("Error while parsing");
            } catch (IllegalAccessException e) {
                throw new ParsingError("Error while parsing");
            } catch (InvocationTargetException e) {
                throw new ParsingError("Error while parsing");
            } catch (InstantiationException e) {
                throw new ParsingError("Error while parsing");
            }
        }
        if (newFigure != null) {
            newFigure.setMoved(moved);
        }
        return newFigure;
    }

    /**
     * gets Figure out of class name
     *
     * @param className name of figure class
     * @param owner     owner of the figure
     * @param x
     * @param y
     * @param newGame   game in which the figure participates in
     * @return
     */
    public static Figure getFigure(String className, Player owner, Integer x, Integer y, Game newGame) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class figClass = Class.forName(className);
        Constructor constructor = figClass.getConstructor(Player.class, Integer.class, Integer.class, Game.class);
        return (Figure) constructor.newInstance(owner, x, y, newGame);
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean isInVicinity(int x1, int y1, int x2, int y2) {
        return Math.abs((double) x1 - x2) <= 1 & Math.abs((double) y1 - y2) <= 1;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void move(int x, int y) {
        field.setField(null, this.getX(), this.getY());
        field.setField(this, x, y);
        setX(x);
        setY(y);
        setMoved(true);
    }

    public void move(Point position) {
        field.setField(null, this.getX(), this.getY());
        field.setField(this, position.x, position.y);
        setX(position.x);
        setY(position.y);
        setMoved(true);
    }

    public void attack(Figure attackedFigure, Point position) {
        attackedFigure.onAttack();
        move(position);
    }

    public void attack(Point pos){
        field.getFigure(pos).onAttack();
        move(pos);
    }


    public void moveViaOffset(int amount, MovementCategory direction) {
        int x = getX() + direction.getX() * amount;
        int y = getY() + direction.getY() * amount;
        field.setField(null, this.getX(), this.getY());
        field.setField(this, x, y);
        setX(x);
        setY(y);
    }

    public void onNextTurn(){}

    /**
     * @param range range a figure can move
     * deprecated

    public void horizontalMove(int range) {
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
     */


    /**
     * @param range range a figure can move
     * @deprecated marks all fields figure can move to/ attack
     */
    public void verticalMove(int range) {
        ArrayList<SingleMove> available = md.getAvailableMoves();
        ArrayList<Attack> attackable = md.getAttacks();
        int range0 = range;
        Direction[] directions = {UP, DOWN};
        for (Direction d : directions) {
            if (d != Direction.UP && d != Direction.DOWN)
                continue;
            int dir = d.getY();
            for (int i = 1; pos.y + i * dir >= 0 && pos.y + i * dir < 8 && range > 0; i++) {
                int why = pos.y + i * dir;
                Figure fig = field.getFigure(pos.x, why);
                //noinspection StatementWithEmptyBody
                if(fig == null || fig instanceof  GhostPawn)
                    //available.add(new Point(pos.x, why))
                    ;
                else {
                    /*
                    if (fig.getOwner() != getOwner())
                        attackable.add(new Point(pos.x, why));
                    break;
                    */
                }
                range--;
            }
            range = range0;
        }
    }


    /**
     * marks all fields figure can move to/ attack
     *
     * @param range     range a figure can move
     * @param movements movements a figure is able to execute
     */
    public void hybridMove(int range, MovementCategory... movements) {
        ArrayList<SingleMove> av = md.getAvailableMoves();
        ArrayList<Attack> at = md.getAttacks();
        Player enemy = game.getQueue().getOtherPlayer(getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        int range0 = range;
        for (MovementCategory movement : movements) {
            ArrayList<Point> points = new ArrayList<>();
            Figure enemyInPath = null;
            int numOfEnemiesInPath = 0;
            int xDir = movement.getX();
            int yDir = movement.getY();
            int i = 1;
            int j = 1;
            if (movement instanceof Jump) {
                int x = pos.x + movement.getX();
                int y = pos.y + movement.getY();
                if (x > 7 | x < 0 | y < 0 | y > 7)
                    continue;
                Point point = new Point(x, y);
                Figure checkingFig = field.getFigure(point);
                if (checkingFig == null || checkingFig instanceof GhostPawn) {
                    av.add(new SingleMove(this, point));
                    continue;
                } else if (checkingFig.getOwner() != getOwner()) {
                    if (checkingFig instanceof King) {
                        enemy.setThreatened(true);
                        enemyKing.setRestrictions(point);
                        enemyKing.addThreatendBy(this);
                    }
                    at.add(new Attack(this, checkingFig, point));
                }
                if (isInVicinity(x, y, kingX, kingY)) {
                    enemyKing.addBlackList(point);
                }
                continue;
            }
            boolean isThreatened = false;
            while (pos.x + i * xDir < 8 & pos.x + i * xDir >= 0 & pos.y + i * yDir < 8 & pos.y + i * yDir >= 0 & range > 0) {
                int why = pos.y + i * yDir;
                int ex = pos.x + i * xDir;
                Figure fig = field.getFigure(ex, why);
                Point point = new Point(ex, why);
                if (fig == null || fig instanceof GhostPawn) {
                    if (numOfEnemiesInPath > 0) {
                        i++;
                        continue;
                    }
                    av.add(new SingleMove(this, point));
                    if (isInVicinity(ex, why, kingX, kingY) && numOfEnemiesInPath < 1)
                        enemyKing.addBlackList(point);
                } else if (fig.getOwner() == getOwner()) {
                    if (isInVicinity(ex, why, kingX, kingY) && numOfEnemiesInPath < 1) {
                        enemyKing.addBlackList(point);
                    }
                    break;
                } else if (fig instanceof King) {
                    if (numOfEnemiesInPath < 1) {
                        points.add(new Point(this.getX(), this.getY()));
                        enemy.setThreatened(true);
                        fig.setRestrictions(new ArrayList<>(points));
                        ((King) fig).addThreatendBy(this);
                        ((King) fig).addBlackList(new Point(ex + i * xDir, why + i * yDir));
                        at.add(new Attack(this, fig, point));
                    } else {
                        enemyInPath.setRestricted(true);
                        enemyInPath.setRestrictions(points);
                        break;
                    }
                } else {
                    if (numOfEnemiesInPath < 1) {
                        at.add(new Attack(this, fig, point));
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

    public void onAttack() {
        delete();
    }

    public void lineMoves(int range, Direction... directions) {
        ArrayList<SingleMove> av = md.getAvailableMoves();
        ArrayList<Attack> at = md.getAttacks();
        Player enemy = game.getQueue().getOtherPlayer(getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        int range0 = range;
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
                if (fig == null || fig instanceof GhostPawn) {
                    if (numOfEnemiesInPath > 0) {
                        i++;
                        continue;
                    }
                    av.add(new SingleMove(this, point));
                    if (isInVicinity(ex, why, kingX, kingY) & numOfEnemiesInPath < 1)
                        enemyKing.addBlackList(point);
                } else if (fig.getOwner() == getOwner()) {
                    if (isInVicinity(ex, why, kingX, kingY) && numOfEnemiesInPath < 1) {
                        enemyKing.addBlackList(point);
                    }
                    break;
                } else if (fig instanceof King) {
                    if (numOfEnemiesInPath < 1) {
                        points.add(new Point(this.getX(), this.getY()));
                        enemy.setThreatened(true);
                        fig.setRestrictions(new ArrayList<>(points));
                        ((King) fig).addThreatendBy(this);
                        ((King) fig).addBlackList(new Point(ex + i * xDir, why + i * yDir));
                        at.add(new Attack(this, fig, point));
                    } else {
                        enemyInPath.setRestricted(true);
                        enemyInPath.setRestrictions(points);
                        break;
                    }
                } else {
                    if (numOfEnemiesInPath < 1) {
                        at.add(new Attack(this, fig, point));
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



    public void reset() {
        getMd().reset();
        setRestricted(false);
        setRestrictions(null);
    }

    /**
     * gets all the fields a
     *
     * @param jumps
     */
    public void jumpMoves(Jump... jumps) {
        ArrayList<SingleMove> av = md.getAvailableMoves();
        ArrayList<Attack> at = md.getAttacks();
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
            if (checkingFig == null || checkingFig instanceof GhostPawn) {
                av.add(new SingleMove(this, point));
                continue;
            }else if (checkingFig.getOwner() != getOwner()) {
                if (checkingFig instanceof King) {
                    enemy.setThreatened(true);
                    enemyKing.setRestrictions(point);
                    enemyKing.addThreatendBy(this);
                }
                at.add(new Attack(this, checkingFig, point));
            }
            if (isInVicinity(x, y, kingX, kingY)) {
                enemyKing.addBlackList(point);
            }
        }
    }

    public boolean isInVicinity(int x, int y) {
        return Math.abs((double) pos.x - x) <= 1 && Math.abs((double) pos.y - y) <= 1;
    }

    /**
     * gets ghost Figure out of class name
     *
     * @param className name of figure class
     * @param owner     owner of the figure
     * @param x
     * @param y
     * @param newGame   game in which the figure participates in
     * @return
     */
    public static Ghost getGhost(String className, Player owner, Integer x, Integer y, Ghostable master, Game newGame) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class figClass = Class.forName(className);
        Constructor constructor = figClass.getConstructor(Player.class, Integer.class, Integer.class, Ghostable.class, Game.class);
        return (Ghost) constructor.newInstance(owner, x, y, master, newGame);
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void delete() {
        getOwner().getFigures().remove(this);
        field.setField(null, this.getX(), this.getY());
        game.getRecorder().onDelete(this);
    }

    public void deleteWithoutRecord() {
        getOwner().getFigures().remove(this);
        field.setField(null, this.getX(), this.getY());
    }

    public String toString() {
        String out = "";
        String moved = hasMoved() ? "1" : "0";
        String ghost = (this instanceof Ghost) ? "1" : "0";
        String player1 = this.getOwner().player1() ? "1" : "0";
        out += ghost + "," + getX() + "," + getY() + "," + this.getClass().getName() + "," + player1 + "," + moved;
        if (this instanceof Ghost) {
            out += "," + ((Ghost) this).getMaster().getPos().x + "," + ((Ghost) this).getMaster().getPos().y;
        }
        return out;
    }
}
