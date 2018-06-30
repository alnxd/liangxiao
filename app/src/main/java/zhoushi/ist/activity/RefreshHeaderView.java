package zhoushi.ist.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by wang on 2016/5/9.
 */
public class RefreshHeaderView extends TextView implements SwipeRefreshTrigger, SwipeTrigger {

    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onRefresh() {
        setText("refreshing");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                setText("release to refresh");
            } else {
                setText("swip to refresh");
            }
        } else {
            setText("refresh returning");
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        setText("compile");
    }

    @Override
    public void onReset() {
        setText("");
    }
}
