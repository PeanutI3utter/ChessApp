package Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;

import java.util.Objects;

import Activities.GameModes.NormalGame;
import GameCore.Graphics.Board;

public class MainMenu extends AppCompatActivity {
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
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {

        }
        findViewById(R.id.newgame).setOnClickListener(view -> {
            Intent i = new Intent(MainMenu.this, NormalGame.class);
            i.putExtra("load", false);
            startActivity(i);
        });
        findViewById(R.id.loadgame).setOnClickListener(view ->{
            Intent i = new Intent(MainMenu.this, NormalGame.class);
            i.putExtra("load", true);
            i.putExtra("loadFile", "Player 1 vs Player 2");
            startActivity(i);
        });
        findViewById(R.id.infobutton).setOnClickListener(view -> {
            new AlertDialog.Builder(MainMenu.this).setTitle("Information").setMessage("OpenChess\nBeta 2.1\n")

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })

                    .show();
        });
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

        /*
        Intent i = new Intent(MainMenu.this, NormalGame.class);
        i.putExtra("load", true);
        i.putExtra("loadFile", "Player 1 vs Player 2");
        startActivity(i);
        */
    }


}
