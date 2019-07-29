package GameCore.Mechanisms;

import android.graphics.Point;

import GameCore.Field;
import GameCore.Figure.Figure;
import GameCore.Game;
import GameCore.Movement.MacroMovements.Move;

/**
 * class handling user inputs
 */
public class InputHandler {
    Game game;
    Field field;
    MoveProcessor moveProcessor;

    public InputHandler(Game game, MoveProcessor moveProcessor) {
        this.game = game;
        field = game.getField();
        this.moveProcessor = moveProcessor;
    }

    public void handleInput(Point clickedPoint) {
        Figure clickedFigure = field.getFigure(clickedPoint);
        Figure selectedFigure = game.getSelected();
        if (selectedFigure == null)
            game.select(clickedFigure);
        else if (clickedFigure == null || clickedFigure.getOwner() != selectedFigure.getOwner()) {
            Move move = selectedFigure.getMove(clickedPoint);
            if (move == null) {
                game.resetSelected();
            } else {
                moveProcessor.processMove(move);
            }
        } else if (clickedFigure == selectedFigure)
            game.resetSelected();
        else
            game.select(clickedFigure);
    }
}
