package zhoushi.ist.viewpage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wang on 2016/5/4.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> viewList;
    private Context context;


    public ViewPagerAdapter(List<View> viewList, Context context) {
        this.viewList = viewList;
        this.context = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {  //移除某项View  函数无返回值;

//         public void destroyItem(View container, int position, Object object)
        //        ((ViewPager)container).removeView(viewList.get(position));
        // 这句话对应上面的函数,一个是ViewGroup，另一个是View；所以需要强制类型转换为Viewpager；下面的instantiateItem同理；
        container.removeView(viewList.get(position));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //添加某项View  函数有返回值;
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }


}
