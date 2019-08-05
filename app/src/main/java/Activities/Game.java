package Activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import GameCore.Exceptions.ParsingError;
import GameCore.Field;
import GameCore.Figure.Figure;
import GameCore.GameSaver.SaveLoader;
import GameCore.Graphics.Board;
import GameCore.Graphics.SelectView;
import GameCore.Mechanisms.InputHandler;
import GameCore.Mechanisms.MoveEvaluator;
import GameCore.Mechanisms.MoveProcessor;
import GameCore.PlayerQueue;
import GameCore.PlayerTypes.Human;
import GameCore.PlayerTypes.Player;
import GameCore.Recorder;
import GameCore.SelectButton;


public abstract class Game extends AppCompatActivity implements View.OnClickListener {
    // game mechanics variables
    protected PlayerQueue queue;
    protected Player player1;
    protected Player player2;
    protected ImageButton[][] fields;
    protected Field figureField;
    protected boolean enemySelected;
    protected int currentTurn;
    protected int fieldSize;
    protected Player currentPlayer;
    protected InputHandler inputHandler;
    protected MoveEvaluator moveEvaluator;
    protected MoveProcessor moveProcessor;
    protected ArrayList<SelectButton> selectButtons;
    protected boolean gameOnGoing;
    protected SaveLoader sl;
    protected String name;
    protected Recorder recorder;

    //graphics variables
    protected Canvas canvas;
    protected Bitmap bitmap;
    protected ImageView imageView;
    protected Figure selected;
    protected Board board;
    protected View currentColor;
    protected TextView player1Name;
    protected TextView player2Name;
    protected SelectView selector;
    protected Button newGame;
    protected Button rewindButton;
    protected Button redoButton;

    protected boolean selectorOpen = false;


    @SuppressWarnings("CatchMayIgnoreException")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {

        }

        fieldSize = setFieldSize();

        if(getIntent().getExtras().getBoolean("load")) {
            name = getIntent().getExtras().getString("loadFile");
            try {
                load(name);
            } catch (ParsingError parsingError) {
                Toast parsingerror = Toast.makeText(Game.this, parsingError.getMessage(), Toast.LENGTH_SHORT);
                parsingerror.show();
                newGame();
            } catch (IOException e) {
                Toast error = Toast.makeText(Game.this, e.getMessage(), Toast.LENGTH_SHORT);
                error.show();
                newGame();
            }
        }
        else
            newGame();

    }


    public void newGame(){
        currentTurn = 0;
        player1 = new Human("Player 1",true);
        player2 = new Human("Player 2", false);
        currentPlayer = null;


        initBasis();

        //
        initBasicComponents();

        //load basic layout
        loadFigures();


        onNextTurn();
        draw();
    }





    /**
     *
     * ################################initializers
     */
     public void initBasis(){
        gameOnGoing = true;

        //init drawing components
        initDrawingComp();

        //load board image
        initBoard();

        sl = new SaveLoader(this);
         recorder = new Recorder(this);


        //get all field buttons
        fields = new ImageButton[fieldSize][fieldSize];
        getAllButtons();

        getSelectorButton();

        getMenuButton();
    }

    public void initBasicComponents(){
        queue = new PlayerQueue(player2, player1);
        if(currentPlayer == null)
            currentPlayer = queue.next();
        else if(currentPlayer == queue.peak())
            queue.next();
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
        board = new Board(this, fieldSize);
        board.setCanvas(canvas);
        board.setDarkBrown(getColor(R.color.boarddarkbrown));
        board.setLightBrown(getColor(R.color.boardlightbrown));
        board.setSelected(getDrawable(R.drawable.gradientwhite));
        board.setMoveable(getDrawable(R.drawable.gradientblue));
        board.setAttackable(getDrawable(R.drawable.gradientred));
        board.setSpecial(getDrawable(R.drawable.gradientgreen));
        board.initBoard();
        if(figureField == null)
            figureField = new Field(fieldSize);
    }

    /**
     * assign all buttons in fields array
     */
    public void getAllButtons() {
        for(int vertical = 0; vertical < fieldSize; vertical++){
            for(int horizontal = 0; horizontal < fieldSize; horizontal++){
                int id = getResources().getIdentifier("field" + ((vertical + 1) * 10 + (horizontal + 1)), "id", getPackageName());
                fields[horizontal][vertical] = findViewById(id);
                fields[horizontal][vertical].setBackgroundColor(Color.TRANSPARENT);
                fields[horizontal][vertical].setOnClickListener(this);
            }
        }
    }

    public void getMenuButton(){
        newGame = findViewById(R.id.newgame);
        newGame.setOnClickListener(view -> newGame());
        findViewById(R.id.save_and_export).setOnClickListener(view -> save());
        rewindButton = findViewById(R.id.revert);
        rewindButton.setOnClickListener(view -> {
            if (recorder.canRewind())
                recorder.rewind(1);
            draw();
        });
        redoButton = findViewById(R.id.redo);
        redoButton.setOnClickListener(view -> {
            if (recorder.canUndoRewind())
                recorder.undoRewind(1);
            draw();
        });
    }



    /**
     * ####################################abstract methods that should be determined by game modes
     */

    /*
        assign listeners to selector buttons
     */
    abstract public void getSelectorButton();

    abstract public void selectorAction(View view);

    // set field size depending on game mode
    abstract public int setFieldSize();

    abstract public SaveLoader setSaveLoader();

    /**
     * @return 0 if game goes on, 1 if someone has one, 2 if draw
     */
    abstract public short checkWin();


    /**
     * loads the basic chess figure layout
     */
    abstract public void loadFigures();





    /**
     * #####################################game mechanic methods
     */

    /**
     * draws the board and figures
     */
    public void draw(){
        for (int h = 0; h < fieldSize; h++) {
            for (int v = 0; v < fieldSize; v++) {
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
        currentColor.setBackground(getDrawable(currentPlayer.getPlayerColor()));
        board.drawBoard();
    }



    @Override
    /**
     * evaluates current state and evalutaes next step
     */
    public void onClick(View view) {
        if(selectorOpen && view instanceof SelectButton) {
            inputHandler.handleSelectorInput(view);
            selectorOpen = false;
            nextTurn();
            draw();
            return;
        }
        if(selectorOpen)
            return;
        Point clickedPoint = getClickedField(view.getId());
        inputHandler.handleBoardInput(clickedPoint);
        draw();
    }

    /**
     * selects the figure if figure is of current player
     */
    public void select(Figure fig) {
        if (fig == null || !fig.isSelectable())
            return;
        if (fig.getOwner() == currentPlayer)
            highlight(fig);
    }

    /**
     * things to do when a player wins
     * @param player
     */
    public void win(Player player){
        Toast toast = Toast.makeText(getApplicationContext(), player.getName() + " won!", Toast.LENGTH_SHORT);
        toast.show();
    }


    public void figureCreated(Figure figure) {
        recorder.onCreate(figure);
    }

    @SuppressWarnings("EmptyMethod")
    public void gameDraw(){

    }


    /**
     * moves the game to next turn
     */
    public void onNextTurn() {
        resetSelected();
        Player oldplayer = currentPlayer;
        currentPlayer = queue.next();
        currentPlayer.reset();
        oldplayer.reset();
        oldplayer.update();
        board.hardResetBoard();
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
        if (recorder.canRewind()) {
            rewindButton.setEnabled(true);
        } else {
            rewindButton.setEnabled(false);
        }
        if (recorder.canUndoRewind()) {
            redoButton.setEnabled(true);
        } else {
            redoButton.setEnabled(false);
        }
        currentTurn++;
    }

    public void nextTurn() {
        recorder.onNextTurn();
        onNextTurn();
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

    /**
     * opens select view
     */
    public void openSelector(){
        selectorOpen = true;
    }





    /**
     *#############################################save and load
     */

    public void save(){
        boolean success;
        if(name == null)
            success = sl.saveNew(getApplicationContext());
        else
            success = sl.saveOld(getApplicationContext());
        if(!success){
            Toast error = Toast.makeText(getApplicationContext(), "Error saving!", Toast.LENGTH_SHORT);
            error.show();
        }
    }

    public void load(String path) throws ParsingError, IOException {
        boolean success;
        initBasis();
        success = sl.load(getApplicationContext(), path);
        if(!success){
            Toast error = Toast.makeText(getApplicationContext(), "Error loading!", Toast.LENGTH_SHORT);
            error.show();
            newGame();
        }
        initBasicComponents();
        onNextTurn(); // syncing queue
        onNextTurn(); // next turn for updating move data and detecting win/loss/draw
        draw();
    }








    /**
     * ##########################################getters and setters
     */
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Field getFigureField() {
        return figureField;
    }

    public void setFigureField(Field figureField) {
        this.figureField = figureField;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setQueue(PlayerQueue queue) {
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
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
}
