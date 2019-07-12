package GameCore;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;

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
        for(int vertical = 0; vertical < 8; vertical++){
            for(int horizontal = 0; horizontal < 8; horizontal++){
                int id = getResources().getIdentifier("f" + ((vertical + 1) * 10 + (horizontal + 1)), "id", getPackageName());
                board[vertical][horizontal] = findViewById(id);
                if((vertical + horizontal) % 2 != 0)
                    board[vertical][horizontal].setBackgroundColor(getResources().getColor(R.color.boardlightbrown));
                else
                    board[vertical][horizontal].setBackgroundColor(getResources().getColor(R.color.boarddarkbrown));
            }
        }

        board[4][4].setBackgroundColor(Color.WHITE);


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
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 | i == 1)
                    player = player2;
                else if (i == 6 | i == 7)
                    player = player1;
                else
                    player = null;

                if (i == 0 | i == 7)
                    switch (j) {
                        case 0:
                        case 7:
                            figureField[i][j] = new Rook(player);
                            break;
                        case 1:
                        case 6:
                            figureField[i][j] = new Knight(player);
                            break;
                        case 2:
                        case 5:
                            figureField[i][j] = new Bishop(player);
                            break;
                        case 4:
                            figureField[i][j] = new King(player);
                            break;
                        case 3:
                            figureField[i][j] = new Queen(player);
                            break;
                        default:
                            figureField[i][j] = new PlaceHolder(null);
                    }
                else if (i == 1 | i == 6)
                    figureField[i][j] = new Pawn(player);
                else
                    figureField[i][j] = new PlaceHolder(player);
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


    private int getX(Integer pos){
        return pos / 10 - 1;
    }

    private int getY(Integer pos){
        return pos % 10 - 1;
    }


    private void resetBoardColors(){
        for(int vertical = 0; vertical < 8; vertical++){
            for(int horizontal = 0; horizontal < 8; horizontal++){
                if((vertical + horizontal) % 2 != 0)
                    board[vertical][horizontal].setBackgroundColor(getResources().getColor(R.color.boardlightbrown));
                else
                    board[vertical][horizontal].setBackgroundColor(getResources().getColor(R.color.boarddarkbrown));
            }
        }
    }



    @Override
    public void onClick(View view) {
        String id = getResources().getResourceEntryName(view.getId());
        id = id.substring(id.length() - 2);
        Integer fieldnumber = Integer.parseInt(id);
        Figure figure = figureField[fieldnumber / 10 - 1][fieldnumber % 10 - 1];
        Toast toast = Toast.makeText(getApplicationContext(), figure.getClass().getName() + (fieldnumber / 10) + "|" + (fieldnumber % 10), Toast.LENGTH_SHORT);
        toast.show();
    }


}
