package GameCore.Graphics;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chess.R;

import GameCore.Player;

/**
 * prototype not in use
 */
public class SelectView extends ConstraintLayout {
    private Drawable blackQueen = getResources().getDrawable(R.drawable.queenblack, null);
    private Drawable blackhorse = getResources().getDrawable(R.drawable.horseblack, null);
    private Drawable blackbishop = getResources().getDrawable(R.drawable.bishopblack, null);
    private Drawable blackrook = getResources().getDrawable(R.drawable.rookblack, null);
    private Drawable blackpawn = getResources().getDrawable(R.drawable.pawnblack, null);
    private Drawable whiteQueen = getResources().getDrawable(R.drawable.queenwhite, null);
    private Drawable whitehorse = getResources().getDrawable(R.drawable.horsewhite, null);
    private Drawable whitebishop = getResources().getDrawable(R.drawable.bishopwhite, null);
    private Drawable whiterook = getResources().getDrawable(R.drawable.rookwhite, null);
    private Drawable whitepawn = getResources().getDrawable(R.drawable.pawnwhite, null);
    private ConstraintLayout container;
    
    public SelectView(Context context) {
        super(context);
    }

    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(){
        container = (ConstraintLayout) getChildAt(0);
        for(int i = 0; i < container.getChildCount(); i++){

        }
    }

    public void player1(boolean player1){
        if(player1)
            setWhite();
        else
            setBlack();
    }

    /**
     * adds listeners to selection buttons
     * @param onClickListener
     */
    public void addListeners(OnClickListener onClickListener){
        for(int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if (view instanceof ImageButton) {
                view.setOnClickListener(onClickListener);
            }
        }
    }

    public void setBlack(){
        for(int i = 0; i < container.getChildCount(); i++){
            if(container.getChildAt(i) instanceof ImageButton) {
                ImageButton subView = (ImageButton) container.getChildAt(i);
                String nameid = getResources().getResourceEntryName(subView.getId());
                switch (nameid){
                    case "queenselect":
                        subView.setBackground(blackQueen);
                        break;
                    case "rookselect":
                        subView.setBackground(blackrook);
                        break;
                    case "bishopselect":
                        subView.setBackground(blackbishop);
                        break;
                    case "knightselect":
                        subView.setBackground(blackhorse);
                        break;
                    case "keep":
                        subView.setBackground(blackpawn);
                        break;
                            
                }
            }
        }
    }

    public void setWhite(){
        for(int i = 0; i < container.getChildCount(); i++){
            if(container.getChildAt(i) instanceof ImageButton) {
                ImageButton subView = (ImageButton) container.getChildAt(i);
                String nameid = getResources().getResourceEntryName(subView.getId());
                switch (nameid){
                    case "queenselect":
                        subView.setBackground(whiteQueen);
                        break;
                    case "rookselect":
                        subView.setBackground(whiterook);
                        break;
                    case "bishopselect":
                        subView.setBackground(whitebishop);
                        break;
                    case "knightselect":
                        subView.setBackground(whitehorse);
                        break;
                    case "keep":
                        subView.setBackground(whitepawn);
                        break;

                }
            }
        }
    }

    public void update(Player player){
        if(player.player1())
            setWhite();
        else
            setBlack();
    }
}
