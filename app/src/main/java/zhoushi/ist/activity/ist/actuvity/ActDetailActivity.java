package zhoushi.ist.activity.ist.actuvity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import zhoushi.ist.R;
import zhoushi.ist.activity.BaseActivity;
import zhoushi.ist.activity.MemberListActivity;
import zhoushi.ist.activity.PhotoSelectActivity;
import zhoushi.ist.controls.PopWindows;
import zhoushi.ist.controls.horizontalscrollview.HorizontalScrollViewAdapter;
import zhoushi.ist.controls.horizontalscrollview.MyHorizontalScrollView;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.Org;
import zhoushi.ist.eventbus.MEPhotoSelected;
import zhoushi.ist.eventbus.MEUserDetails;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.utils.Utils;

/**
 * Created by wang on 2016/5/29.
 */

@ContentView(R.layout.zhoushi_actdetail)
public class ActDetailActivity extends BaseActivity {

    //活动名称
    @ViewInject(R.id.tv_my_name)
    private TextView orgName;

    //活动成员
    @ViewInject(R.id.orgMember)
    private TextView orgMember;

    //活动类型
    @ViewInject(R.id.orgType)
    private TextView orgType;

    //活动所属社团
    @ViewInject(R.id.orgOwnerType)
    private TextView m_tvActOwnerOrg;

    //活动简介
    @ViewInject(R.id.orgSummarytitle)
    private TextView orgSummary;

    //申请加入活动列表按钮
    @ViewInject(R.id.orgApprove)
    private Button orgApprove;

    //活动logo
    @ViewInject(R.id.st_tupian)
    private ImageView m_ivActIcon;

    @ViewInject(R.id.btn_photowall)
    private ImageButton m_btnPhotoWall;



    @ViewInject(R.id.id_horizontalScrollView_photo)
    private MyHorizontalScrollView m_hsPhotoAct;
    private HorizontalScrollViewAdapter m_hsAdapterAct;

    @ViewInject(R.id.id_horizontalScrollView_member)
    private MyHorizontalScrollView m_hsPhotoMember;
    private HorizontalScrollViewAdapter m_hsAdapterMember;

    private  int m_nPull;
    private Context mContext;
    private View m_view;
    private EditText jionOrgSummary;
    private Button joinOrg,cancleJoin;
    private int m_nJoinState=0;//当前状态，加入或者退出
    private Boolean m_bIsBoss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        m_nPull = intent.getIntExtra("m_nPullIntent",0);
        Act act = DataManager.getInstance().getM_dataAct().GetData(m_nPull);
        Org org = DataManager.getInstance().getM_dataOrg().GetData(act.getOrgid());
        int m_nME = DataManager.getInstance().getM_nMe();
        m_bIsBoss = (m_nME == org.getOrgBoss());
        Boolean bMember = act.isActMember(m_nME);
        if (bMember) {
            if (m_bIsBoss) {//社长同时也是活动创建人
                orgApprove.setText("申请列表");
                orgApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActDetailActivity.this,JoinActListActivity.class);
                        intent.putExtra("actid",m_nPull);
                        startActivity(intent);
                    }
                });
            } else {
                m_nJoinState = NetProtocol.JOINSTATE_DELETE;
                orgApprove.setText("申请退出");
                orgApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupWindow(v);
                    }
                });
                m_btnPhotoWall.setVisibility(View.INVISIBLE);
            }
        }else {
            orgApprove.setText("申请加入");
            m_nJoinState = NetProtocol.JOINSTATE_ADD;
            orgApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupWindow(v);
                }
            });
            m_btnPhotoWall.setVisibility(View.INVISIBLE);
        }

        orgName.setText(act.getName());
        orgMember.setText(act.getM_hashMember().size()+"位成员");
        List<String> listConfig = DataManager.getInstance().getM_dataConfig().getM_listCollege();
        orgType.setText(listConfig.get(act.getType()));
        orgSummary.setText("活动简介：" +act.getSummary());
        m_tvActOwnerOrg.setText(org.getName());
        x.image().bind(m_ivActIcon,Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ACT,m_nPull));
        initHS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //初始化横向滚动控件
    protected void initHS()
    {
        m_hsPhotoAct.setCurrentImageChangeListener(new MyHorizontalScrollView.CurrentImageChangeListener() {
            @Override
            public void onCurrentImgChanged(int position,
                                            View viewIndicator) {
              //  viewIndicator.setBackgroundColor(Color
                //        .parseColor("#AA024DA4"));
            }
        });
        //添加点击回调
        m_hsPhotoAct.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //view.setBackgroundColor(Color.parseColor("#AA024DA4"));
                String sUrl = m_hsAdapterAct.getmDatas().get(position);
                showPhotoPopupWindow(view,sUrl);
            }
        });
        //设置适配器
        m_hsAdapterAct = new HorizontalScrollViewAdapter(this);
        renderActPhotos();


        m_hsPhotoMember.setCurrentImageChangeListener(new MyHorizontalScrollView.CurrentImageChangeListener() {
            @Override
            public void onCurrentImgChanged(int position,
                                            View viewIndicator) {
                //viewIndicator.setBackgroundColor(Color
                  //      .parseColor("#AA024DA4"));
            }
        });
        //添加点击回调
        m_hsPhotoMember.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                String sUrl = m_hsAdapterMember.getmDatas().get(position);
                showPhotoPopupWindow(view,sUrl);
            }
        });
        //设置适配器
        m_hsAdapterMember = new HorizontalScrollViewAdapter(this);
        renderActMemberPhotos();
    }

    //渲染相册控件
    protected void renderActPhotos()
    {
        m_hsAdapterAct.getmDatas().clear();
        Act act = DataManager.getInstance().getM_dataAct().GetData(m_nPull);
        ArrayList<String> listPhoto = act.getM_listPhoto();
        for(int i=0;i<listPhoto.size();i++)
        {
            m_hsAdapterAct.getmDatas().add(Utils.configFileUrl(NetProtocol.FILEHANDLE_ACT,m_nPull,listPhoto.get(i)));
        }
        m_hsPhotoAct.initDatas(m_hsAdapterAct);
    }

    //渲染相册控件
    protected void renderActMemberPhotos()
    {
        m_hsAdapterMember.getmDatas().clear();
        Act act = DataManager.getInstance().getM_dataAct().GetData(m_nPull);
        HashMap<Integer, Integer> listMember = act.getM_hashMember();
        Iterator iter = listMember.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int)entry.getKey();
            m_hsAdapterMember.getmDatas().add(Utils.configFileUrl(NetProtocol.FILEHANDLE_USER,key,"0.jpg"));
        }
        m_hsPhotoMember.initDatas(m_hsAdapterMember);
    }

    private void onApplyJoinAct(int state) throws DbException {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.JOINACT);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("actid", m_nPull);
        params.addParameter("optype", state);  // 0 加入  1 退出;
        params.addParameter("reason", jionOrgSummary.getText());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("OrgDetailActivity", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);

                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.JOINACT);
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


    private void showPopupWindow(View view){
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.showjoinorg_popwindow, null);
        jionOrgSummary = (EditText) contentView.findViewById(R.id.jionOrgSummary);
        joinOrg = (Button) contentView.findViewById(R.id.joinOrg);
        cancleJoin = (Button) contentView.findViewById(R.id.cancleJoin);


        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);


        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int org_height = m_view.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < org_height) {
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
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.create_org_background));


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        cancleJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        joinOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onApplyJoinAct(m_nJoinState);
                    Toast.makeText(ActDetailActivity.this, "您已提交申请,请等待审核", Toast.LENGTH_SHORT).show();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //上传logo或者普通图片
    private void addPhoto(int nType)
    {
        if(!m_bIsBoss)return;//只有社长可以更新
        Intent intent = new Intent(this,PhotoSelectActivity.class);
        intent.putExtra("m_nType", NetProtocol.FILEHANDLE_ACT);
        intent.putExtra("m_nImageType", nType);
        startActivity(intent);
    }

    //上传照片
    @Event(value = R.id.st_tupian )
    private void onAddLogoClick(View view)  {
        addPhoto(NetProtocol.FILEHANDLE_TYPE_LOGO);
    }

    @Event(value = R.id.btn_photowall )
    private void onPhotoWallClick(View view)  {
        addPhoto(NetProtocol.FILEHANDLE_TYPE);
    }

    //返回
    @Event(value = R.id.btn_back )
    private void onBackClick(View view)  {
//        Intent intent=new Intent();
//        intent.setClass(OrgDetailActivity.this, ZHTabActivity.class);
//        startActivity(intent);
        ActDetailActivity.this.finish();
    }

    @Event(value = R.id.btn_photo_member )
    private void onPhotoMemberClick(View view)  {
        //先拉取成员信息 todo
        //首先检查缓存中是否有，没有再拉取
        String sParam = "";
        Act at = DataManager.getInstance().getM_dataAct().GetData(m_nPull);
        Iterator iter = at.getM_hashMember().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int nID = (int)entry.getKey();
            if(DataManager.getInstance().getM_dataStudent().HasData(nID) == false)
            {
                sParam += nID;
                sParam += ";";
            }
        }

        //这里如果没有需要拉取的，直接转入MemberListActivity
        if(sParam.length() <= 0)
        {
            Intent intent = new Intent(this, MemberListActivity.class);
            intent.putExtra("m_nOwnerType", NetProtocol.PULLALLID_ACT);
            intent.putExtra("m_nOwner", m_nPull);
            startActivity(intent);
            return;
        }
        sParam = sParam.substring(0,sParam.length()-1);

        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLUSERDETAIL);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("roletype", 0);
        params.addParameter("idlist", sParam);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putInt("type", NetProtocol.PULLALLID_ACT);
                message.setData(bundle);

                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.PULLUSERDETAIL);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUserDetail(MEUserDetails data) {

        if(data.getM_nType() != NetProtocol.PULLALLID_ACT)
            return;

        Intent intent = new Intent(this, MemberListActivity.class);
        intent.putExtra("m_nOwnerType", NetProtocol.PULLALLID_ACT);
        intent.putExtra("m_nOwner", m_nPull);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoSelected(MEPhotoSelected data) {
        //处理图片上传
        if(data.getM_nType() != NetProtocol.FILEHANDLE_ACT)
            return;

        if(!m_bIsBoss)return;//只有社长可以更新

        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.FILEHANDLER);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("type", data.getM_nType());
        params.addParameter("opertype",NetProtocol.FILEHANDLE_ADD);
        params.addParameter("phototype", data.getM_nImageType());
        params.addParameter("id", m_nPull);
        params.addBodyParameter("upfiles", new File(DataManager.getInstance().getM_listPhotoPath().get(0)));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int nErrercode = jsonObject.getInt("errorcode");
                    String sPhoto = jsonObject.getString("photo");
                    if (nErrercode == 0) {
                        x.image().clearMemCache();
                        x.image().clearCacheFiles();
                        if (sPhoto.length() == 0)
                            x.image().bind(m_ivActIcon, Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ACT, m_nPull));
                        else {
                            Act act = DataManager.getInstance().getM_dataAct().GetData(m_nPull);
                            act.setPhoto(sPhoto);
                            act.photoString2Hash();
                            renderActPhotos();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    private void showPhotoPopupWindow(View view,String sUrl){
        PopWindows.showPhotoPopupWindow(this, view, sUrl);
    }
}



