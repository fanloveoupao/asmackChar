package MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by bruse on 16/3/7.
 */
public class NoScrollGridView  extends GridView {

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridView(Context argContext) {
        super(argContext);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}