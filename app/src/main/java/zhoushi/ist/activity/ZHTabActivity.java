package zhoushi.ist.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.Margins;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import zhoushi.ist.R;
import zhoushi.ist.activity.ist.actuvity.CreateActActivity;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.fragment.PersonFragment;
import zhoushi.ist.fragment.OrgFragment;
import zhoushi.ist.fragment.ChatFragment;
import zhoushi.ist.fragment.ActFragment;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;


/**
 * Created by Linhh on 16/3/8.
 */
@ContentView(R.layout.activity_zhoushi_tab)
public class ZHTabActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

   // @ViewInject(R.id.tabs)
    private AdvancedPagerSlidingTabStrip m_apSlider;//滑动组件，配合viewpager使用

   // @ViewInject(R.id.vp_main)
    private ZSViewPager m_vpMain;//viewpager

    @ViewInject(R.id.ivCenterBtn)//中间按钮
    private ImageView m_btnCenter;


    private static final int VIEW_FIRST = 0;
    private static final int VIEW_SECOND = 1;
    private static final int VIEW_THIRD = 2;
    private static final int VIEW_FOURTH = 3;
    private static final int VIEW_SIZE = 4;

    //private ActFragment mFirstFragment = ActFragment.instance();
    private ActFragment mFirstFragment = ActFragment.instance();
    private OrgFragment mSecondFragment = OrgFragment.instance();
    private ChatFragment mThirdFragment = ChatFragment.instance();
    private PersonFragment mFourthFragment = PersonFragment.instance();

    private int mSize = 0;

    private View m_viewPop;
    private Button m_btnCreateOrg;
    private Button m_btnCreateAct;
    private Button m_btnCenterBack;

    private int org_height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void init() {
        m_vpMain = (ZSViewPager)this.findViewById(R.id.vp_main);
        m_apSlider = (AdvancedPagerSlidingTabStrip)this.findViewById(R.id.tabs);
        mSize = getResources().getDimensionPixelSize(R.dimen.weibo_tab_size);
        m_vpMain.setOffscreenPageLimit(VIEW_SIZE);
        m_vpMain.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        m_apSlider.setViewPager(m_vpMain);
        m_apSlider.setOnPageChangeListener(this);
        m_vpMain.setCurrentItem(VIEW_SECOND);
    }

    @Event(value = R.id.ivCenterBtn)
    private void onCenterBtnClick(View view)//中间按钮点击事件响应
    {
        //这里改动仅仅能够创建社团了，创建活动放在社团详情页面
        Intent intent = new Intent(ZHTabActivity.this, CreateOrgActivity.class);
        startActivity(intent);
    }

    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popwindoworg, null);
        m_viewPop = contentView.findViewById(R.id.pop_layout);
        m_btnCreateOrg = (Button)contentView.findViewById(R.id.btn_create_org);
        m_btnCreateAct = (Button)contentView.findViewById(R.id.btn_createactivity);
        m_btnCenterBack = (Button)contentView.findViewById(R.id.btn_cancel);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                org_height = m_viewPop.getTop();
                int y = (int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y < org_height){
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.create_org_background));

        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);

        m_btnCreateOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZHTabActivity.this, CreateOrgActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        m_btnCenterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        m_btnCreateAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZHTabActivity.this, CreateActActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
         if(position ==VIEW_FOURTH )
             mFourthFragment.rednerUI();
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
                        return mFirstFragment;
                    case VIEW_SECOND:
                        return mSecondFragment;
                    case VIEW_THIRD:
                        return mThirdFragment;
                    case VIEW_FOURTH:
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


    public void pullAllOrgID(int state) {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLALLORG);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("type", state);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("ZHTabActivity", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                NetManager netManager = NetManager.getInstance();
                NetHandler handler = netManager.GetNetHandler(NetProtocol.PULLALLORG);
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void pullOrgDetails(int id) {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLORGDETAIL);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("roletype", 0);
        params.addParameter("orglist", id +"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("ZHTabActivity", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.PULLORGDETAIL);
                hd.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }


    public void pullActDetails(int id) {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLACTDETAIL);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("roletype", 0);
        params.addParameter("actidlist", id +"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("ZHTabActivity", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putInt("type", 0);
                message.setData(bundle);
                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.PULLACTDETAIL);
                hd.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

}