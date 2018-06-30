package zhoushi.ist.activity.office;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.Margins;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import zhoushi.ist.R;
import zhoushi.ist.activity.ZSViewPager;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.fragment.OfficeOrgFragment;
import zhoushi.ist.fragment.PersonFragment;
import zhoushi.ist.fragment.ChatFragment;
import zhoushi.ist.fragment.OfficeActFragment;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;


/**
 * Created by Linhh on 16/3/8.
 */
public class OfficeMainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    public AdvancedPagerSlidingTabStrip mAPSTS;
    public ZSViewPager mVP;

    private static final int VIEW_FIRST = 0;
    private static final int VIEW_SECOND = 1;
    private static final int VIEW_THIRD = 2;
    private static final int VIEW_FOURTH = 3;

    private static final int VIEW_SIZE = 4;

    private OfficeActFragment mFirstFragment = null;
    private OfficeOrgFragment mTeacher_Back = null;
    private ChatFragment mThirdFragment = null;
    private PersonFragment mFourthFragment = null;
    private ImageView mIvCenterBtn = null;


    private int mSize = 0;

    private  int userid,roletype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zhoushi_tab);
        findViews();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void findViews() {
        mAPSTS = (AdvancedPagerSlidingTabStrip) findViewById(R.id.tabs);
        mVP = (ZSViewPager) findViewById(R.id.vp_main);
        mIvCenterBtn = (ImageView) findViewById(R.id.ivCenterBtn);
    }

    private void init() {
        mIvCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OfficeMainActivity.this, "Center Btn is Clicked.", Toast.LENGTH_SHORT).show();
            }
        });
        mSize = getResources().getDimensionPixelSize(R.dimen.weibo_tab_size);
        mVP.setOffscreenPageLimit(VIEW_SIZE);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        mVP.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        adapter.notifyDataSetChanged();
        mAPSTS.setViewPager(mVP);
        mAPSTS.setOnPageChangeListener(this);
        mVP.setCurrentItem(DataManager.getInstance().getM_nCurrItem());
        //mVP.getCurrentItem()
        //       mAPSTS.showDot(VIEW_FIRST,"99+");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case VIEW_FIRST:

            case VIEW_SECOND:

            case VIEW_THIRD:

            case VIEW_FOURTH:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class FragmentAdapter extends FragmentStatePagerAdapter implements AdvancedPagerSlidingTabStrip.IconTabProvider, AdvancedPagerSlidingTabStrip.LayoutProvider {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_FIRST:
                        if (null == mFirstFragment)
                            mFirstFragment = OfficeActFragment.instance();
                        return mFirstFragment;

                    case VIEW_SECOND:
                        if (null == mTeacher_Back)
                            mTeacher_Back = OfficeOrgFragment.instance();
                        return mTeacher_Back;

                    case VIEW_THIRD:
                        if (null == mThirdFragment)
                            mThirdFragment = ChatFragment.instance();
                        return mThirdFragment;

                    case VIEW_FOURTH:
                        if (null == mFourthFragment)
                            mFourthFragment = PersonFragment.instance();
                        return mFourthFragment;
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_FIRST:
                        return "活动";
                    case VIEW_SECOND:
                        return "社团";
                    case VIEW_THIRD:
                        return "公告";
                    case VIEW_FOURTH:
                        return "我的";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public float getPageWeight(int position) {
            return 0.0f;
        }

        @Override
        public int[] getPageRule(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_FIRST:
                        return new int[0];
                    case VIEW_SECOND:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_LEFT};
                    case VIEW_THIRD:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_RIGHT};
                    case VIEW_FOURTH:
                        return new int[0];
                    default:
                        break;
                }
            }
            return new int[0];
        }

        @Override
        public Margins getPageMargins(int position) {
            return null;
        }

        @Override
        public Integer getPageIcon(int index) {
            if (index >= 0 && index < VIEW_SIZE) {
                switch (index) {
                    case VIEW_FIRST:
                        return R.mipmap.zs_huodong;
                    case VIEW_SECOND:
                        return R.mipmap.zs_shetuan;
                    case VIEW_THIRD:
                        return R.mipmap.zs_liaotian;
                    case VIEW_FOURTH:
                        return R.mipmap.zs_yonghu;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Integer getPageSelectIcon(int index) {
            if (index >= 0 && index < VIEW_SIZE) {
                DataManager.getInstance().setM_nCurrItem(index);
                switch (index) {
                    case VIEW_FIRST:
                        return R.mipmap.zh_hongdong1;
                    case VIEW_SECOND:
                        return R.mipmap.zs_shetuan1;
                    case VIEW_THIRD:
                        return R.mipmap.zs_liaotian1;
                    case VIEW_FOURTH:
                        return R.mipmap.zs_yonghu1;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Rect getPageIconBounds(int position) {
            return new Rect(0, 0, mSize, mSize);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }
}