package GameCore;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Figure.Bishop;
import GameCore.Figure.Figure;
import GameCore.Figure.GhostPawn;
import GameCore.Figure.King;
import GameCore.Figure.Knight;
import GameCore.Figure.Pawn;
import GameCore.Figure.Queen;
import GameCore.Figure.Rook;
import GameCore.Graphics.Board;
import GameCore.Graphics.SelectView;
import GameCore.Mechanisms.InputHandler;
import GameCore.Mechanisms.MoveEvaluator;
import GameCore.Mechanisms.MoveProcessor;


public abstract class Game extends AppCompatActivity implements View.OnClickListener {
    // game mechanics variables
    protected PlayerQueue queue;
    protected Player player1;
    protected Player player2;
    protected ImageButton[][] fields;
    protected Field figureField;
    protected boolean enemySelected;
    protected int currentTurn;
    protected int fieldW;
    protected int fieldH;
    protected Player currentPlayer;
    protected InputHandler inputHandler;
    protected MoveEvaluator moveEvaluator;
    protected MoveProcessor moveProcessor;
    protected ArrayList<SelectButton> selectButtons;

    //graphics variables
    protected Canvas canvas;
    protected Bitmap bitmap;
    protected ImageView imageView;
    protected Figure selected;
    protected int buttonWH;
    protected int imageWH;
    protected Board board;
    protected View currentColor;
    protected TextView player1Name;
    protected TextView player2Name;
    protected SelectView selector;

    protected boolean selectorOpen = false;
    protected Figure getSelected = null;


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
        player1 = new Human("Player 1",true);
        player2 = new Human("Player 2", false);


        //init drawing components
        initDrawingComp();

        //load board image
        initBoard();

        //
        initBasicComponents();


        //get all field buttons
        fields = new ImageButton[8][8];
        getAllButtons();

        getSelectorButton();


        //load basic layout
        loadFiguresBasic();

        nextTurn();
        draw();


    }

    public void initBasicComponents(){
        queue = new PlayerQueue(player2, player1);
        currentPlayer = queue.next();
        currentColor = findViewById(R.id.currentcolor);
        currentColor.setBackgroundColor(Color.WHITE);
        player1Name = findViewById(R.id.player1);
        player2Name = findViewById(R.id.player2);
        player1Name.setTextColor(Color.WHITE);
        player2Name.setTextColor(Color.BLACK);
        player1Name.setText(player1.getName());
        player2Name.setText(player2.getName());
        selector = findViewById(R.id.selector);
        selector.init();
        moveProcessor = new MoveProcessor(this);
        moveEvaluator = new MoveEvaluator(this);
        inputHandler = new InputHandler(this, moveProcessor);
    }

    /**
     * initiates canvas and bitmap object
     */
    public void initDrawingComp(){
        imageView = findViewById(R.id.imageview);
        bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);
        selector = findViewById(R.id.selector);
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

    /*
        assign listeners to selector buttons
     */
    public void getSelectorButton(){
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

    public void openSelector(){
        selectorOpen = true;
    }

    /*
        returns player queue
     */
    public PlayerQueue getQueue() {
        return queue;
    }

    /*
        get selected figure
     */
    public Figure getSelected() {
        return selected;
    }

    /*
        returns current field
     */
    public Field getField() {
        return figureField;
    }

    public MoveEvaluator getMoveEvaluator() {
        return moveEvaluator;
    }

    public MoveProcessor getMoveProcessor() {
        return moveProcessor;
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
        if(selectorOpen)
            selector.setVisibility(View.VISIBLE);
        else
            selector.setVisibility(View.INVISIBLE);
        board.drawBoard();
    }

    /**
     * @return 0 if game goes on, 1 if someone has one, 2 if draw
     */
    abstract public short checkWin();

    /**
     * @param fig
     * @param point
     */

    public void move(Figure fig, Point point) {
        Figure dest = figureField.getFigure(point.x, point.y);
        if (dest != null)
            dest.delete();
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
        if(selectorOpen && view instanceof SelectButton) {
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
            selectorOpen = false;
            nextTurn();
            draw();
            return;
        }
        if(selectorOpen)
            return;
        Point clickedPoint = getClickedField(view.getId());
        inputHandler.handleInput(clickedPoint);
        draw();
    }

    /**
     * selects the figure if figure is of current player
     */
    public void select(Figure fig) {
        if (fig == null | fig instanceof GhostPawn)
            return;
        if (fig.getOwner() == currentPlayer)
            highlight(fig);
    }


    public void win(Player player){
        Toast toast = Toast.makeText(getApplicationContext(), player.getName() + " won!", Toast.LENGTH_SHORT);
        toast.show();
    }


    public void gameDraw(){

    }


    /**
     * moves the game to next turn
     */
    public void nextTurn() {
        resetSelected();
        Player oldplayer = currentPlayer;
        currentPlayer = queue.next();
        currentPlayer.reset();
        oldplayer.reset();
        oldplayer.update();
        board.hardResetBoard();
        currentColor.setBackground(getDrawable(currentPlayer.getPlayerColor()));
        currentPlayer.update();
        currentPlayer.onNextTurn();
        switch (checkWin()){
            case 0:
                break;
            case 1:
                win(oldplayer);
                break;
            case 2:
                gameDraw();
                break;
        }
        if (currentPlayer.isThreatened())
            board.highlightAttack(currentPlayer.getKing());
        selector.update(currentPlayer);
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
