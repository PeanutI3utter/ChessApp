package GameCore.Graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Figure.Figure;

import static GameCore.Graphics.FieldState.*;

public class Board extends View {
    private Canvas canvas;
    private Bitmap bitmap;
    private Field board[][];
    private Integer lightBrown;
    private Integer darkBrown;
    private Drawable selected;
    private Drawable moveable;
    private Drawable attackable;
    private Paint paint;
    private int w;
    private int h;



    public Board(Context context) {
        super(context);
        paint = new Paint();
    }

    public Board(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        paint = new Paint();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setLightBrown(Integer lightBrown) {
        this.lightBrown = lightBrown;
    }

    public void setDarkBrown(Integer darkBrown) {
        this.darkBrown = darkBrown;
    }

    public void setAttackable(Drawable attackable) {
        this.attackable = attackable;
    }

    public void setMoveable(Drawable moveable) {
        this.moveable = moveable;
    }

    public void setSelected(Drawable selected) {
        this.selected = selected;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void initBoard(){
        if(lightBrown == null | darkBrown == null){
            throw new IllegalStateException("Board Colors are not set!");
        }
        board = new Field[8][8];
        for(int row = 0; row < 8; row++){
            for(int line = 0; line < 8; line++){
                int col = (row + line) % 2 != 0 ? darkBrown : lightBrown;
                board[row][line] = new Field(row, line, col);
            }
        }
        resetBoard();
    }

    public void resetBoard(){
        for(int row = 0; row < 8; row++){
            for(int line = 0; line < 8; line++){
                board[row][line].setState(NOTSELECTED);
            }
        }
    }

    //draws board via given drawables
    public void drawBoard(){
        for(Field[] row : board){
            for(Field field : row){
                int left = field.getX() * 50;
                int top = field.getY() * 50;
                int right = left + 50;
                int bottom = top + 50;
                paint.setColor(field.getColor());
                switch (field.getState()){
                    case NOTSELECTED:
                        canvas.drawRect(left, top, right, bottom, paint);
                        break;
                    case SELECTED:
                        selected.setBounds(left, top, right, bottom);
                        selected.draw(canvas);
                        break;
                    case MOVEABLE:
                        moveable.setBounds(left, top, right, bottom);
                        moveable.draw(canvas);
                        break;
                    case ATTACKABLE:
                        attackable.setBounds(left, top, right, bottom);
                        attackable.draw(canvas);
                        break;
                }
            }
        }
    }

    // highlight all available moves for selected figure
    public void highlight(Figure fig, Figure[][] figureField) {
        ArrayList<Point> av = fig.getMd(figureField).getAvailableMoves();
        ArrayList<Point> at = fig.getMd(figureField).getAttackbleFields();
        for (Point p : av) {
            board[p.x][p.y].setState(MOVEABLE);
        }
        for (Point p : at) {
            board[p.x][p.y].setState(ATTACKABLE);
        }
    }

    public void test(){

    }

    private class Field{
        private int x;
        private int y;
        private int color;
        private FieldState state;

        public Field(int x, int y, int color){
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public void setState(FieldState state) {
            this.state = state;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public FieldState getState() {
            return state;
        }

        public int getColor() {
            return color;
        }
    }
}
