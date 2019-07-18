package GameCore.Graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class Board extends View {
    private Canvas canvas;
    private Bitmap bitmap;
    private Field board[][] = new Field[8][8];


    public Board(Context context) {
        super(context);
    }

    public Board(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private class Field{
        int x;
        int y;
        int color;

        public Field(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
