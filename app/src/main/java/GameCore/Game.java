package GameCore;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Figure.Bishop;
import GameCore.Figure.Figure;
import GameCore.Figure.King;
import GameCore.Figure.Knight;
import GameCore.Figure.Pawn;
import GameCore.Figure.PlaceHolder;
import GameCore.Figure.Queen;
import GameCore.Figure.Rook;

public class Game extends AppCompatActivity implements View.OnClickListener {

    PlayerQueue queue;
    Player player1;
    Player player2;
    ImageButton[][] fields;
    Figure[][] figureField;
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
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {

        }
        fieldW = 8;
        fieldH = 8;
        player1 = new Human(true);
        player2 = new Human(false);

        //load board image
        board = new View[8][8];
        loadBoard();

        //get all field buttons
        fields = new ImageButton[8][8];
        getAllButtons();


        figureField = new Figure[8][8];
        /*load basic layout
        loadFiguresBasic();
        */

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                figureField[i][j] = new PlaceHolder(null, i, j);
            }
        }

        figureField[4][4] = new Queen(player1, 4, 4);
        for (int i = 6; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch ((i + j) % 3) {
                    case 0:
                        figureField[j][i] = new Rook(player2, j, i);
                        break;
                    case 1:
                        figureField[j][i] = new Bishop(player2, j, i);
                        break;
                    case 2:
                        figureField[j][i] = new Knight(player1, j, i);
                }
            }
        }
        draw();
    }

    // loads the board by filling colors and such
    public void loadBoard() {
        for(int vertical = 0; vertical < 8; vertical++) {
            for (int horizontal = 0; horizontal < 8; horizontal++) {
                int id = getResources().getIdentifier("f" + ((vertical + 1) * 10 + (horizontal + 1)), "id", getPackageName());
                board[horizontal][vertical] = findViewById(id);
                if ((vertical + horizontal) % 2 != 0)
                    board[horizontal][vertical].setBackgroundColor(getResources().getColor(R.color.boarddarkbrown));
                else
                    board[horizontal][vertical].setBackgroundColor(getResources().getColor(R.color.boardlightbrown));
            }
        }
    }


    // assign all buttons in fields array
    public void getAllButtons() {
        for(int vertical = 0; vertical < 8; vertical++){
            for(int horizontal = 0; horizontal < 8; horizontal++){
                int id = getResources().getIdentifier("field" + ((vertical + 1) * 10 + (horizontal + 1)), "id", getPackageName());
                fields[horizontal][vertical] = findViewById(id);
                fields[horizontal][vertical].setBackgroundColor(Color.TRANSPARENT);
                fields[horizontal][vertical].setOnClickListener(this);
            }
        }
    }


    //loads the basic chess figure layout
    public void loadFiguresBasic() {
        Player player;
        for(int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (y == 0 | y == 1)
                    player = player2;
                else if (y == 6 | y == 7)
                    player = player1;
                else
                    player = null;

                if (y == 0 | y == 7)
                    switch (x) {
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
                else if (y == 1 | y == 6)
                    figureField[x][y] = new Pawn(player, x, y);
                else
                    figureField[x][y] = new PlaceHolder(player, x, y);
            }
        }
    }


    //draw the figures
    public void draw(){
        for (int h = 0; h < 8; h++) {
            for (int v = 0; v < 8; v++) {
                Figure fig = figureField[h][v];
                ImageButton field = fields[h][v];
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
    //OnClickListener for figures
    public void onClick(View view) {
        String id = getResources().getResourceEntryName(view.getId());
        id = id.substring(id.length() - 2);
        Integer fieldnumber = Integer.parseInt(id);
        Figure figure = figureField[fieldnumber % 10 - 1][fieldnumber / 10 - 1];
        Toast toast = Toast.makeText(getApplicationContext(), figure.getClass().getName() + (fieldnumber % 10 - 1) + "|" + (fieldnumber / 10 - 1), Toast.LENGTH_SHORT);
        toast.show();


        highlight(figure, figure.availableMoves(figureField));
    }


    // highlight all available moves for selected figure
    public void highlight(Figure fig, MoveData md) {
        resetBoardColors();
        ArrayList<Point> av = md.getAvailableMoves();
        ArrayList<Point> at = md.getAttackbleFields();
        for (Point p : av)
            board[p.x][p.y].setBackground(getDrawable(R.drawable.gradientblue));
        for (Point p : at)
            board[p.x][p.y].setBackground(getDrawable(R.drawable.gradientred));
        board[fig.getX()][fig.getY()].setBackground(getDrawable(R.drawable.gradientwhite));
    }



}
