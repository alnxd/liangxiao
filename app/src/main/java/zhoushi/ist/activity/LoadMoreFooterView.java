package zhoushi.ist.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by wang on 2016/5/9.
 */
public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLoadMore() {
        setText("loading more");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled <= -getHeight()) {
                setText("releadw to load more");
            } else {
                setText("swip to load more");
            }
        } else {
            setText("load more returning");
        }
    }

    @Override
    public void onRelease() {
        setText("loading more");
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
