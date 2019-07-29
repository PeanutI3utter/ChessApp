package GameCore;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;

/**
 * custom button returning a given selection value(type)
 */
public class SelectButton extends AppCompatImageButton {
    private String type;

    public SelectButton(Context context) {
        super(context);
    }

    public SelectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
