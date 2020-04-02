package test;

import android.graphics.Point;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chess.R;

import java.util.ArrayList;

import GameCore.Movement.MacroMovements.SingleMove;
import GameCore.Utils.Util;


public class VariousTest extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.test);
        tv = findViewById(R.id.tv);
        SingleMove sm1 = new SingleMove(null, new Point(3, 6));
        SingleMove sm2 = new SingleMove(null, new Point(5, 1));
        ArrayList<SingleMove> testArray = new ArrayList<>();
        testArray.add(sm1);
        testArray.add(sm2);
        Point point = new Point(3, 6);
        ArrayList<Point> pointList = new ArrayList<>();
        pointList.add(point);
        Util.subtract(testArray, pointList);
        tv.setText(String.valueOf(testArray.size()));
    }
}
