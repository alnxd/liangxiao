package zhoushi.ist.controls;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import org.xutils.x;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ListViewBinder implements SimpleAdapter.ViewBinder
{
    /**
     * view：要板顶数据的视图
     * data：要绑定到视图的数据
     * textRepresentation：一个表示所支持数据的安全的字符串，结果是data.toString()或空字符串，但不能是Null
     * 返回值：如果数据绑定到视图返回真，否则返回假
     */
    @Override
    public boolean setViewValue(View view, Object data,
                                String textRepresentation) {
        if((view instanceof ImageView)&(data instanceof String))
        {
            ImageView iv = (ImageView)view;
            x.image().bind(iv, data.toString());
            return true;
        }
        return false;
    }


}
