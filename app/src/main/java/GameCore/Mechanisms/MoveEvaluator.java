package GameCore.Mechanisms;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Objects;

import GameCore.Field;
import GameCore.Figure.Figure;
import GameCore.Figure.GhostPawn;
import GameCore.Figure.King;
import GameCore.Game;
import GameCore.MoveData;
import GameCore.Movement.MacroMovements.Attack;
import GameCore.Movement.MacroMovements.SingleMove;
import GameCore.Movement.MacroMovements.SpecialMove;
import GameCore.Movement.MoveEval.PotentialMove;
import GameCore.Movement.MovementDescriber.Jump;
import GameCore.Movement.MovementDescriber.MovementCategory;
import GameCore.Movement.MovementDescriber.SpecialMoveEval;
import GameCore.Player;

/**
 * movement evaluator helper class, has methods for detecting all possible movements
 */
public class MoveEvaluator {
    Game game;
    Field field;

    public MoveEvaluator(Game game) {
        this.game = game;
        this.field = game.getField();
    }


    /**
     * universal move evaluator (kind of like ray tracing)
     *
     * @param figure
     */
    public void evalMoves(Figure figure) {
        ArrayList<PotentialMove> potentialMoves = figure.getStandardMoves();
        MoveData md = figure.getMd();
        ArrayList<SingleMove> av = md.getAvailableMoves();
        ArrayList<Attack> at = md.getAttacks();
        Player enemy = game.getQueue().getOtherPlayer(figure.getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        for (PotentialMove p : potentialMoves) {
            ArrayList<Point> threateningPath = new ArrayList<>();
            MovementCategory mc = p.getMc();
            ArrayList<Figure> enemiesInPath = new ArrayList<>();
            int range = p.getRange();
            int jumpLimit = p.getJumpOvers() + 1;
            int numOfEnemiesInPath = 0;
            int numOfAlliesInPath = 0;
            int numOfFiguresInPath;
            int xDir = mc.getX();
            int yDir = mc.getY();
            int ex;
            int why;
            int i = 1;
            boolean isAttack = p.isAttack();
            boolean isMove = p.isMove();
            while ((ex = figure.getX() + i * xDir) < 8 & ex >= 0 & (why = figure.getY() + i * yDir) < 8 & why >= 0 && range > 0) {
                Figure checkingFigure = field.getFigure(ex, why);
                Point checkingPoint = new Point(ex, why);
                numOfFiguresInPath = numOfAlliesInPath + numOfEnemiesInPath;
                if (!isAttack && !(numOfFiguresInPath < jumpLimit))
                    break;
                if (checkingFigure == null || checkingFigure instanceof GhostPawn) {
                    if (isMove && numOfFiguresInPath < jumpLimit) {
                        av.add(new SingleMove(figure, checkingPoint));
                        enemyKing.addBlackList(checkingPoint);
                    }
                } else if (figure.getOwner() == checkingFigure.getOwner()) {
                    if (numOfFiguresInPath < jumpLimit) {
                        enemyKing.addBlackList(checkingPoint);
                    }
                    numOfAlliesInPath++;
                } else if (checkingFigure instanceof King) {
                    if (isAttack) {
                        if (numOfFiguresInPath < jumpLimit) {
                            threateningPath.add(new Point(figure.getX(), figure.getY()));
                            enemy.setThreatened(true);
                            checkingFigure.setRestrictions(new ArrayList<>(threateningPath));
                            ((King) checkingFigure).addThreatendBy(figure);
                            ((King) checkingFigure).addBlackList(new Point(ex + i * xDir, why + i * yDir));
                            at.add(new Attack(figure, checkingFigure, checkingPoint));
                        } else {
                            threateningPath.add(new Point(figure.getX(), figure.getY()));
                            for (Figure enemyInPath : enemiesInPath) {
                                enemyInPath.setRestricted(true);
                                enemyInPath.setRestrictions(threateningPath);
                            }
                            break;
                        }
                    }
                } else {
                    if (isAttack && numOfFiguresInPath < jumpLimit) {
                        at.add(new Attack(figure, checkingFigure, checkingPoint));
                        enemiesInPath.add(checkingFigure);
                    } else if (!(numOfFiguresInPath < jumpLimit))
                        break;
                    numOfEnemiesInPath++;
                }
                threateningPath.add(checkingPoint);
                i++;
                range--;
            }
        }
    }

    /**
     * evaluates special moves
     *
     * @param figure
     */
    public void specialMoves(Figure figure) {
        ArrayList<SpecialMoveEval> evaluators = figure.getSpecialMoves();
        for (SpecialMoveEval evaluator : evaluators) {
            ArrayList<SpecialMove> specialMove = evaluator.checkSpecialMove(game, figure);
            if (specialMove != null) {
                for (SpecialMove sm : specialMove)
                    figure.getMd().addSpecial(sm);
            }
        }
    }


    // deprecated methods
    //#################################################################################################################################################################


    /**
     * marks all fields figure can move to/ attack
     *
     * @param range     range a figure can move
     * @param movements movements a figure is able to execute
     * @deprecated
     */
    @SuppressWarnings("ConstantConditions")
    public void hybridMove(Figure figure, int range, MovementCategory... movements) {
        MoveData md = figure.getMd();
        ArrayList<SingleMove> av = md.getAvailableMoves();
        ArrayList<Attack> at = md.getAttacks();
        Player enemy = game.getQueue().getOtherPlayer(figure.getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        int range0 = range;
        int why;
        int ex;
        for (MovementCategory movement : movements) {
            ArrayList<Point> points = new ArrayList<>();
            Figure enemyInPath = null;
            int numOfEnemiesInPath = 0;
            int xDir = movement.getX();
            int yDir = movement.getY();
            int i = 1;
            if (movement instanceof Jump) {
                int x = figure.getX() + movement.getX();
                int y = figure.getY() + movement.getY();
                if (x > 7 | x < 0 | y < 0 | y > 7)
                    continue;
                Point point = new Point(x, y);
                Figure checkingFig = field.getFigure(point);
                if (checkingFig == null || checkingFig instanceof GhostPawn) {
                    av.add(new SingleMove(figure, point));
                    if (Figure.isInVicinity(point.x, point.y, kingX, kingY))
                        enemyKing.addBlackList(point);
                    continue;
                } else if (checkingFig.getOwner() != figure.getOwner()) {
                    if (checkingFig instanceof King) {
                        enemy.setThreatened(true);
                        enemyKing.setRestrictions(point);
                        enemyKing.addThreatendBy(figure);
                    }
                    at.add(new Attack(figure, checkingFig, point));
                }
                if (Figure.isInVicinity(x, y, kingX, kingY)) {
                    enemyKing.addBlackList(point);
                }
                continue;
            }
            while ((ex = figure.getX() + i * xDir) < 8 && ex >= 0 && (why = figure.getY() + i * yDir) < 8 && why >= 0 && range > 0) {
                Figure fig = field.getFigure(ex, why);
                Point point = new Point(ex, why);
                if (fig == null || fig instanceof GhostPawn) {
                    if (numOfEnemiesInPath > 0) {
                        i++;
                        continue;
                    }
                    av.add(new SingleMove(figure, point));
                    if (Figure.isInVicinity(ex, why, kingX, kingY) & numOfEnemiesInPath < 1)
                        enemyKing.addBlackList(point);
                } else if (fig.getOwner() == figure.getOwner()) {
                    if (Figure.isInVicinity(ex, why, kingX, kingY) && numOfEnemiesInPath < 1) {
                        enemyKing.addBlackList(point);
                    }
                    break;
                } else if (fig instanceof King) {
                    if (numOfEnemiesInPath < 1) {
                        points.add(new Point(figure.getX(), figure.getY()));
                        enemy.setThreatened(true);
                        fig.setRestrictions(new ArrayList<>(points));
                        ((King) fig).addThreatendBy(figure);
                        for (; range > 0; range--) {
                            ((King) fig).addBlackList(new Point(ex + range * xDir, why + range * yDir));
                        }
                        at.add(new Attack(figure, fig, point));
                    } else {
                        enemyInPath.setRestricted(true);
                        enemyInPath.setRestrictions(points);
                    }
                    break;
                } else {
                    if (numOfEnemiesInPath < 1) {
                        at.add(new Attack(figure, fig, point));
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
     * gets all the fields a figure can jump to
     *
     * @param jumps
     * @deprecated
     */
    public void jumpMoves(Figure figure, Jump... jumps) {
        MoveData md = figure.getMd();
        ArrayList<SingleMove> av = md.getAvailableMoves();
        ArrayList<Attack> at = md.getAttacks();
        Player enemy = game.getQueue().getOtherPlayer(figure.getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        for (Jump jump : jumps) {
            int x = figure.getX() + jump.getX();
            int y = figure.getY() + jump.getY();
            if (x > 7 | x < 0 | y < 0 | y > 7)
                continue;
            Point point = new Point(x, y);
            Figure checkingFig = field.getFigure(point);
            if (checkingFig == null || checkingFig instanceof GhostPawn) {
                av.add(new SingleMove(figure, point));
                continue;
            } else if (checkingFig.getOwner() != figure.getOwner()) {
                if (checkingFig instanceof King) {
                    enemy.setThreatened(true);
                    enemyKing.setRestrictions(point);
                    enemyKing.addThreatendBy(figure);
                }
                at.add(new Attack(figure, checkingFig, point));
            }
            if (Figure.isInVicinity(x, y, kingX, kingY)) {
                enemyKing.addBlackList(point);
            }
        }
    }

    /**
     * marks all fields figure can move to/ attack
     *
     * @apiNote modified lineMove() method, can evaluate all moves including jumps now
     * @deprecated
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void moves(Figure figure, PotentialMove... potentialMoves) {
        MoveData md = figure.getMd();
        ArrayList<SingleMove> av = md.getAvailableMoves();
        ArrayList<Attack> at = md.getAttacks();
        Player enemy = game.getQueue().getOtherPlayer(figure.getOwner());
        King enemyKing = enemy.getKing();
        int kingX = enemyKing.getX();
        int kingY = enemyKing.getY();
        for (PotentialMove potentialMove : potentialMoves) {
            MovementCategory m = potentialMove.getMc();
            ArrayList<Point> points = new ArrayList<>();
            Figure enemyInPath = null;
            int range = potentialMove.getRange();
            int jumpOverLimiter = potentialMove.getJumpOvers() + 1;
            int numOfEnemiesInPath = 0;
            int numOfAlliesInPath = 0;
            int xDir = m.getX();
            int yDir = m.getY();
            int ex;
            int why;
            int i = 1;
            boolean isAttack = potentialMove.isAttack();
            boolean isMove = potentialMove.isMove();
            while ((ex = figure.getX() + i * xDir) < 8 & ex >= 0 & (why = figure.getY() + i * yDir) < 8 & why >= 0 & range > 0) {
                Figure fig = field.getFigure(ex, why);
                Point point = new Point(ex, why);
                if (fig == null || fig instanceof GhostPawn) {
                    if (numOfEnemiesInPath + numOfAlliesInPath > potentialMove.getJumpOvers()) {
                        range--;
                        i++;
                        continue;
                    }
                    av.add(new SingleMove(figure, point));
                    if (Figure.isInVicinity(ex, why, kingX, kingY) & numOfEnemiesInPath < jumpOverLimiter)
                        enemyKing.addBlackList(point);
                } else if (fig.getOwner() == figure.getOwner()) {
                    if (Figure.isInVicinity(ex, why, kingX, kingY) && numOfEnemiesInPath < jumpOverLimiter) {
                        enemyKing.addBlackList(point);
                    }
                    if (numOfAlliesInPath + numOfEnemiesInPath >= jumpOverLimiter - 1)
                        break;
                    numOfAlliesInPath++;
                } else if (fig instanceof King) {
                    if (!isAttack) {

                    } else if (numOfEnemiesInPath + numOfAlliesInPath < jumpOverLimiter) {
                        points.add(new Point(figure.getX(), figure.getY()));
                        enemy.setThreatened(true);
                        fig.setRestrictions(new ArrayList<>(points));
                        ((King) fig).addThreatendBy(figure);
                        ((King) fig).addBlackList(new Point(ex + i * xDir, why + i * yDir));
                        at.add(new Attack(figure, fig, point));
                    } else {
                        points.add(new Point(figure.getX(), figure.getY()));
                        Objects.requireNonNull(enemyInPath).setRestricted(true);
                        enemyInPath.setRestrictions(points);
                        break;
                    }
                } else {
                    if (!isAttack) {

                    }
                    if (numOfEnemiesInPath++ + numOfAlliesInPath < jumpOverLimiter) {
                        at.add(new Attack(figure, fig, point));
                        enemyInPath = fig;
                    } else {
                        break;
                    }
                }
                points.add(point);
                i++;
                range--;
            }
        }
    }

}
