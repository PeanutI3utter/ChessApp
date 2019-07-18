package GameCore;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import GameCore.Graphics.Board;

public class Game extends AppCompatActivity implements View.OnClickListener {

    PlayerQueue queue;
    Player player1;
    Player player2;
    Canvas canvas;
    Bitmap bitmap;
    ImageView imageView;
    ImageButton[][] fields;
    Figure[][] figureField;
    Figure selected;
    int buttonWH;
    int imageWH;
    int fieldW;
    int fieldH;
    Board board;
    Player currentPlayer;
    View currentColor;


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
        queue = new PlayerQueue(player1, player2);
        currentPlayer = queue.next();
        currentColor = findViewById(R.id.currentcolor);
        currentColor.setBackgroundColor(Color.WHITE);


        //init drawing components
        initDrawingComp();

        //load board image
        initBoard();


        //get all field buttons
        fields = new ImageButton[8][8];
        getAllButtons();


        figureField = new Figure[8][8];
        //load basic layout
        loadFiguresBasic();


        draw();
    }

    public void initDrawingComp(){
        imageView = findViewById(R.id.imageview);
        bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    public void initBoard(){
        board = new Board(this);
        board.setCanvas(canvas);
        board.setDarkBrown(getColor(R.color.boarddarkbrown));
        board.setLightBrown(getColor(R.color.boardlightbrown));
        board.setSelected(getDrawable(R.drawable.gradientwhite));
        board.setMoveable(getDrawable(R.drawable.gradientblue));
        board.setAttackable(getDrawable(R.drawable.gradientred));
        board.initBoard();
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
                    field.setImageResource(android.R.color.transparent);
                else
                    field.setImageResource(fig.getImage());
            }
        }
        board.drawBoard();
    }

    public void move(Figure fig, Point point) {
        figureField[fig.getX()][fig.getY()] = new PlaceHolder(null, fig.getX(), fig.getY());
        figureField[point.x][point.y] = fig;
        fig.setPos(point);
    }


    private int getX(int pos){
        return pos / 10;
    }
    private int getY(int pos){
        return pos % 10;
    }






    @Override
    //OnClickListener for figures
    public void onClick(View view) {
        Figure figure = getClickedField(view.getId());
        if (selected == null) {
            if (figure instanceof PlaceHolder)
                return;
            if (figure.getOwner() == currentPlayer) {
                selected = figure;
                selected.availableMoves(figureField);
                highlight(figure);
            }
        } else {
            selected.availableMoves(figureField);
            if (figure instanceof PlaceHolder) {
                if (selected.getMd(figureField).getAvailableMoves().contains(figure.getPos())) {
                    move(selected, figure.getPos());
                    nextMove();
                } else {
                    selected = null;
                    board.resetBoard();
                }
            } else if (figure.getOwner() == currentPlayer) {
                board.resetBoard();
                selected = figure;
                highlight(figure);
            } else {
                if (selected.getMd(figureField).getAttackbleFields().contains(figure.getPos())) {
                    move(selected, figure.getPos());
                    nextMove();
                } else {
                    selected = null;
                    board.resetBoard();
                }
            }
        }
        draw();
    }


    public void nextMove() {
        currentPlayer = queue.next();
        selected = null;
        currentColor.setBackgroundColor(currentPlayer.getPlayerColor());
        board.resetBoard();
    }

    // highlight all available moves for selected figure
    public void highlight(Figure fig) {
        board.highlight(fig, figureField);
        draw();
    }


    private Figure getClickedField(int idN) {
        String id = getResources().getResourceEntryName(idN);
        id = id.substring(id.length() - 2);
        Integer fieldnumber = Integer.parseInt(id);
        return figureField[fieldnumber % 10 - 1][fieldnumber / 10 - 1];
    }


}
