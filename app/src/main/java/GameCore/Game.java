package GameCore;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;

import java.util.ArrayList;
import java.util.Arrays;

import GameCore.Figure.Bishop;
import GameCore.Figure.Figure;
import GameCore.Figure.Knight;
import GameCore.Figure.King;
import GameCore.Figure.Pawn;
import GameCore.Figure.PlaceHolder;
import GameCore.Figure.Queen;
import GameCore.Figure.Rook;

public class Game extends AppCompatActivity implements View.OnClickListener {

    PlayerQueue queue;
    Player player1;
    Player player2;
    ImageButton fields[][];
    Figure figureField[][];
    Figure selected;
    int buttonWH;
    int imageWH;
    int fieldW;
    int fieldH;
    View[][] board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        fieldW = 8;
        fieldH = 8;

        //load board image
        board = new View[8][8];
        for(int vertical = 0; vertical < 8; vertical++) {
            for (int horizontal = 0; horizontal < 8; horizontal++) {
                int id = getResources().getIdentifier("f" + ((vertical + 1) * 10 + (horizontal + 1)), "id", getPackageName());
                board[vertical][horizontal] = findViewById(id);
                if ((vertical + horizontal) % 2 != 0)
                    board[vertical][horizontal].setBackgroundColor(getResources().getColor(R.color.boarddarkbrown));
                else
                    board[vertical][horizontal].setBackgroundColor(getResources().getColor(R.color.boardlightbrown));
            }
        }

        //get all field buttons
        fields = new ImageButton[8][8];
        for(int vertical = 0; vertical < 8; vertical++){
            for(int horizontal = 0; horizontal < 8; horizontal++){
                int id = getResources().getIdentifier("field" + ((vertical + 1) * 10 + (horizontal + 1)), "id", getPackageName());
                fields[vertical][horizontal] = findViewById(id);
                fields[vertical][horizontal].setBackgroundColor(Color.TRANSPARENT);
                fields[vertical][horizontal].setOnClickListener(this);
            }
        }
        figureField = new Figure[8][8];
        player1 = new Human(true);
        player2 = new Human(false);
        Player player;
        for(int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (x == 0 | x == 1)
                    player = player2;
                else if (x == 6 | x == 7)
                    player = player1;
                else
                    player = null;

                if (x == 0 | x == 7)
                    switch (y) {
                        case 0:
                        case 7:
                            figureField[x][y] = new Rook(player, x, y);
                            break;
                        case 1:
                        case 6:
                            figureField[x][y] = new Knight(player, x, y);
                            break;
                        case 2:
                        case 5:
                            figureField[x][y] = new Bishop(player, x, y);
                            break;
                        case 4:
                            figureField[x][y] = new King(player, x, y);
                            break;
                        case 3:
                            figureField[x][y] = new Queen(player, x, y);
                            break;
                        default:
                            figureField[x][y] = new PlaceHolder(null, x, y);
                    }
                else if (x == 1 | x == 6)
                    figureField[x][y] = new Pawn(player, x, y);
                else
                    figureField[x][y] = new PlaceHolder(player, x, y);
            }
        }
        draw();
    }

    public void draw(){
        for(int v = 0; v < 8; v++){
            for(int h = 0; h < 8; h++){
                Figure fig = figureField[v][h];
                ImageButton field = fields[v][h];
                if(fig instanceof PlaceHolder)
                    field.setVisibility(View.INVISIBLE);
                else{
                    field.setImageResource(fig.getImage());
                    field.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private int getX(int pos){
        return pos / 10;
    }
    private int getY(int pos){
        return pos % 10;
    }


    private void resetBoardColors() {
        for(int vertical = 0; vertical < 8; vertical++){
            for(int horizontal = 0; horizontal < 8; horizontal++){
                if((vertical + horizontal) % 2 != 0)
                    board[vertical][horizontal].setBackgroundColor(getResources().getColor(R.color.boarddarkbrown));
                else
                    board[vertical][horizontal].setBackgroundColor(getResources().getColor(R.color.boardlightbrown));
            }
        }
    }



    @Override
    public void onClick(View view) {
        String id = getResources().getResourceEntryName(view.getId());
        id = id.substring(id.length() - 2);
        Integer fieldnumber = Integer.parseInt(id);
        Figure figure = figureField[fieldnumber / 10 - 1][fieldnumber % 10 - 1];
        Toast toast = Toast.makeText(getApplicationContext(), figure.getClass().getName() + (fieldnumber / 10 - 1) + "|" + (fieldnumber % 10 - 1), Toast.LENGTH_SHORT);
        toast.show();


        highlight(figure.availableMoves(null));
    }

    public void highlight(ArrayList<Pair<Integer, Integer>> moves) {
        resetBoardColors();
        if (!moves.isEmpty()) {
            for (Pair move : moves) {
                board[(int) move.first][(int) move.second].setBackgroundColor(Color.YELLOW);
            }
        }
    }



}
