package GameCore.Movement;

import GameCore.Field;
import GameCore.Figure.Figure;

public interface SpecialMoveEval {

    void checkSpecialMove(Field field, Figure figure);
}
