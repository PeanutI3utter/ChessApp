package GameCore.GameModes;

import android.view.View;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Figure.Bishop;
import GameCore.Figure.Figure;
import GameCore.Figure.King;
import GameCore.Figure.Knight;
import GameCore.Figure.Pawn;
import GameCore.Figure.Queen;
import GameCore.Figure.Rook;
import GameCore.Game;
import GameCore.GameSaver.SaveLoader;
import GameCore.PlayerTypes.Player;
import GameCore.SelectButton;

/**
 * normal game mode where a player wins if the other player is in check mate
 */
public class NormalGame extends Game {
    @Override
    public int setFieldSize() {
        return 8;
    }

    @Override
    public short checkWin() {
        if(currentPlayer.canMove())
            return 0;
        if(currentPlayer.isThreatened())
            return 1;
        return 2;
    }

    @Override
    public void getSelectorButton() {
        selectButtons = new ArrayList<>();
        SelectButton queen = findViewById(R.id.queenselect);
        queen.setType("queen");
        queen.setOnClickListener(this);
        selectButtons.add(queen);
        SelectButton rook = findViewById(R.id.rookselect);
        rook.setType("rook");
        rook.setOnClickListener(this);
        selectButtons.add(rook);
        SelectButton bishop = findViewById(R.id.bishopselect);
        bishop.setType("bishop");
        bishop.setOnClickListener(this);
        selectButtons.add(bishop);
        SelectButton knight = findViewById(R.id.knightselect);
        knight.setType("knight");
        knight.setOnClickListener(this);
        selectButtons.add(knight);
        SelectButton keep = findViewById(R.id.keep);
        keep.setType("keep");
        keep.setOnClickListener(this);
        selectButtons.add(keep);
    }

    @Override
    public void selectorAction(View view) {
        Figure change = selected;
        switch (((SelectButton)view).getType()){
            case "queen":
                change = new Queen(currentPlayer, selected.getX(), selected.getY(), this);
                break;
            case "rook":
                change = new Rook(currentPlayer, selected.getX(), selected.getY(), this);
                break;
            case "bishop":
                change = new Bishop(currentPlayer, selected.getX(), selected.getY(), this);
                break;
            case "knight":
                change = new Knight(currentPlayer, selected.getX(), selected.getY(), this);
                break;
            case "keep":
        }
        ((Pawn)selected).exchange(change);
    }

    @Override
    public SaveLoader setSaveLoader() {
        return new SaveLoader(this);
    }

    @Override
    public void loadFigures() {
        Player player;
        for(int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                if (y == 0 | y == 1)
                    player = player2;
                else if (y == 6 | y == 7)
                    player = player1;
                else
                    player = null;

                if (y == 0 | y == 7) {
                    switch (x) {
                        case 0:
                        case 7:
                            figureField.setField(new Rook(player, x, y, this), x, y);
                            break;
                        case 1:
                        case 6:
                            figureField.setField(new Knight(player, x, y, this), x, y);
                            break;
                        case 2:
                        case 5:
                            figureField.setField(new Bishop(player, x, y, this), x, y);
                            break;
                        case 4:
                            figureField.setField(new King(player, x, y, this), x, y);
                            player.setKing((King) figureField.getFigure(x, y));
                            break;
                        case 3:
                            figureField.setField(new Queen(player, x, y, this), x, y);
                            break;
                        default:
                            figureField.setField(null, x, y);
                    }
                    player.addFigure(figureField.getFigure(x, y));
                } else if (y == 1 | y == 6) {
                    figureField.setField(new Pawn(player, x, y, this), x, y);
                    player.addFigure(figureField.getFigure(x, y));
                }
                else
                    figureField.setField(null, x, y);
            }
        }
    }
}
