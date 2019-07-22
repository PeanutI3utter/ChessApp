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

import GameCore.Figure.Bishop;
import GameCore.Figure.Figure;
import GameCore.Figure.King;
import GameCore.Figure.Knight;
import GameCore.Figure.Pawn;
import GameCore.Figure.Queen;
import GameCore.Figure.Rook;
import GameCore.Graphics.Board;
import GameCore.Movement.SpecialMove;

public class Game extends AppCompatActivity implements View.OnClickListener {

    PlayerQueue queue;
    Player player1;
    Player player2;
    Canvas canvas;
    Bitmap bitmap;
    ImageView imageView;
    ImageButton[][] fields;
    Field figureField;
    Figure selected;
    boolean highlightEnemy = true;
    boolean enemySelected;
    int buttonWH;
    int imageWH;
    int currentTurn;
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
        currentTurn = 0;
        player1 = new Human(true);
        player2 = new Human(false);
        queue = new PlayerQueue(player2, player1);
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


        //load basic layout
        loadFiguresBasic();

        nextTurn();
        draw();
    }

    /**
     * initiates canvas and bitmap object
     */
    public void initDrawingComp(){
        imageView = findViewById(R.id.imageview);
        bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * initiates board object
     */
    public void initBoard(){
        board = new Board(this);
        board.setCanvas(canvas);
        board.setDarkBrown(getColor(R.color.boarddarkbrown));
        board.setLightBrown(getColor(R.color.boardlightbrown));
        board.setSelected(getDrawable(R.drawable.gradientwhite));
        board.setMoveable(getDrawable(R.drawable.gradientblue));
        board.setAttackable(getDrawable(R.drawable.gradientred));
        board.setSpecial(getDrawable(R.drawable.gradientgreen));
        board.initBoard();
        figureField = new Field();
    }

    /**
     * assign all buttons in fields array
     */
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


    public PlayerQueue getQueue() {
        return queue;
    }

    /**
     * loads the basic chess figure layout
     */
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


    /**
     * draws the board and figures
     */
    public void draw(){
        for (int h = 0; h < 8; h++) {
            for (int v = 0; v < 8; v++) {
                Figure fig = figureField.getFigure(h, v);
                ImageButton field = fields[h][v];
                if (fig == null)
                    field.setImageResource(android.R.color.transparent);
                else
                    field.setImageResource(fig.getImage());
            }
        }
        board.drawBoard();
    }

    /**
     * @param fig
     * @param point
     */

    public void move(Figure fig, Point point) {
        Figure dest = figureField.getFigure(point.x, point.y);
        if (dest != null)
            dest.delete(figureField);
        figureField.setField(null, fig.getX(), fig.getY());
        figureField.setField(fig, point.x, point.y);
        fig.setPos(point);
        fig.setMoved(true);
    }


    private int getX(int pos){
        return pos / 10;
    }
    private int getY(int pos){
        return pos % 10;
    }



    @Override
    /**
     * evaluates current state and evalutaes next step
     */
    public void onClick(View view) {
        Point clickedPoint = getClickedField(view.getId());
        Figure figure = figureField.getFigure(clickedPoint);
        if (selected == null) {
            select(figure);
        } else {
            afterSelection(figure, clickedPoint);
        }
        draw();
    }

    /**
     * selects the figure if figure is of current player
     */
    public void select(Figure fig) {
        if (fig == null)
            return;
        if (fig.getOwner() == currentPlayer)
            highlight(fig);
    }


    public void afterSelection(Figure figure, Point clickedPoint) {
        if (figure == null) {
            if (selected.getMd().getAvailableMoves().contains(clickedPoint)) {
                move(selected, clickedPoint);
                nextTurn();
            } else {
                SpecialMove specialMove = selected.getMd().getSpecialMove(clickedPoint);
                if (specialMove != null) {
                    specialMove.move(figureField);
                    nextTurn();
                } else
                    resetSelected();
            }
        } else if (figure == selected) {
            resetSelected();
        } else if (figure.getOwner() == currentPlayer) {
            board.hardResetBoard();
            highlight(figure);
        } else {
            if (selected.getMd().getAttackbleFields().contains(figure.getPos())) {
                move(selected, figure.getPos());
                nextTurn();
            } else {
                resetSelected();
            }
        }
    }


    /**
     * moves the game to next turn
     */
    public void nextTurn() {
        Player oldplayer = currentPlayer;
        currentPlayer = queue.next();
        currentPlayer.reset();
        oldplayer.update(figureField);
        board.hardResetBoard();
        selected = null;
        currentColor.setBackground(getDrawable(currentPlayer.getPlayerColor()));
        currentPlayer.update(figureField);
        if (currentPlayer.isThreatened())
            board.highlightAttack(currentPlayer.getKing());
        currentTurn++;
    }

    // highlight all available moves for selected figure
    public void highlight(Figure fig) {
        selected = fig;
        board.highlight(fig);
    }

    /**
     * resets highlight to non selected state
     */
    public void resetSelected() {
        selected = null;
        enemySelected = false;
        if (currentPlayer.isThreatened())
            board.highlightAttack(currentPlayer.getKing());
        board.resetBoard();
    }

    /**
     * calculates clicked position via id
     *
     * @param idN id of clicked element(button)
     * @return parsed position via id
     */
    private Point getClickedField(int idN) {
        String id = getResources().getResourceEntryName(idN);
        Integer lineNum = Integer.parseInt(id.substring(id.length() - 2, id.length() - 1)) - 1;
        Integer rowNum = Integer.parseInt(id.substring(id.length() - 1)) - 1;
        return new Point(rowNum, lineNum);
    }


}
