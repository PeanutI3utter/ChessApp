package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import GameCore.Game;

public class MainActivity extends AppCompatActivity {
    ImageView board;
    Canvas canvas;
    Bitmap bitmap;
    Paint paint;
    int w;
    int h;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board = findViewById(R.id.board);
        w = 400;
        h = 400;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        board.setImageBitmap(bitmap);
        paint.setColor(Color.rgb(0x795E3D >> 16, (0x795E3D >> 8) & 0xFF, 0x795E3D & 0xFF));
        Rect rect = new  Rect(0, 0, w/8, w/8);
        canvas.drawRect(rect,paint);

        /*
        Intent i = new Intent(MainActivity.this, Game.class);
        startActivity(i);
        */
    }
}
