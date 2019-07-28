package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;

import GameCore.GameModes.NormalGame;
import GameCore.Graphics.Board;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    Board board;
    Canvas canvas;
    Bitmap bitmap;
    Paint paint;
    int w;
    int h;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        w = 400;
        h = 400;
        iv = findViewById(R.id.board);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        iv.setImageBitmap(bitmap);
        initBoard();
        board.test();
        draw();

        paint = new Paint();
        paint.setColor(Color.rgb(0x795E3D >> 16, (0x795E3D >> 8) & 0xFF, 0x795E3D & 0xFF));
        Rect rect = new  Rect(0, 0, w/8, w/8);
        canvas.drawRect(rect,paint);
        */


        Intent i = new Intent(MainActivity.this, NormalGame.class);
        startActivity(i);

    }

    public void draw(){
        board.drawBoard();
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
}
