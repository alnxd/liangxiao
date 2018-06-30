package zhoushi.ist.controls;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.xutils.ex.DbException;
import org.xutils.x;

import zhoushi.ist.R;

/**
 * Created by Administrator on 2016/8/13.
 */
public class PopWindows {

    public static void showPhotoPopupWindow(Context context,View view,String sUrl){
        View contentView = LayoutInflater.from(context).inflate(R.layout.photo_popwindow, null);
        ImageView ivScaleImage = (ImageView) contentView.findViewById(R.id.iv_scale_photo);
        x.image().bind(ivScaleImage,sUrl);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
