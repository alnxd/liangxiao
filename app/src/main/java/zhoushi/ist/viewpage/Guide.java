package zhoushi.ist.viewpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import zhoushi.ist.R;
import zhoushi.ist.activity.InterfaceActivity;
import zhoushi.ist.activity.MainActivity;

/**
 * Created by wang on 2016/5/4.
 */
public class Guide extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> viewList;
    private ImageView[]dots;
    private int [] ids = {R.id.iv1,R.id.iv2,R.id.iv3};
    private Button btnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giut_activity);
        initViews();
        initdots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        viewList = new ArrayList<View>();
        viewList.add(inflater.inflate(R.layout.one_viewpager,null));
        viewList.add(inflater.inflate(R.layout.two_viewpager,null));
        viewList.add(inflater.inflate(R.layout.three_viewpager,null));

        viewPagerAdapter = new ViewPagerAdapter(viewList,this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        btnClick = (Button) viewList.get(2).findViewById(R.id.btnClick);
//        btnClick = (Button) findViewById(R.id.btnClick); 不能用这种方法，这种方法btnClick = null；
// 因为R.id.btnClick没在Guide布局中,所以只能用viewList.get(2)得到第二个ViewPager；在寻到btnClick;
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Guide.this, InterfaceActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(this);

    }

    private void initdots(){
        dots = new ImageView[viewList.size()];
        for (int i=0;i<viewList.size();i++){
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        for (int i=0;i<ids.length;i++){
            if (position == i)
                dots[i].setImageResource(R.drawable.login_point_selected);
            else
                dots[i].setImageResource(R.drawable.login_point);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
