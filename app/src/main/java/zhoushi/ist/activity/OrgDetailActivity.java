package zhoushi.ist.activity;


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
import org.xutils.image.ImageOptions;
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
import zhoushi.ist.activity.ist.actuvity.CreateActActivity;
import zhoushi.ist.controls.PopWindows;
import zhoushi.ist.controls.horizontalscrollview.HorizontalScrollViewAdapter;
import zhoushi.ist.controls.horizontalscrollview.MyHorizontalScrollView;
import zhoushi.ist.dal.DataManager;
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

@ContentView(R.layout.zhoushi_orgdetail)
public class OrgDetailActivity extends BaseActivity {

    @ViewInject(R.id.tv_my_name)
    private TextView orgName;

    @ViewInject(R.id.orgMember)
    private TextView orgMember;

    @ViewInject(R.id.orgType)
    private TextView orgType;

    @ViewInject(R.id.orgCollege)
    private TextView orgCollege;

    @ViewInject(R.id.orgSummary)
    private TextView orgSummary;

    @ViewInject(R.id.orgApprove)
    private Button orgApprove;

    @ViewInject(R.id.btn_broadcast_update)
    private ImageButton m_btnBroadCastUp;

    @ViewInject(R.id.btn_photo_wall)
    private ImageButton m_btnPhotoWall;



    @ViewInject(R.id.st_tupian)
    private ImageView m_vpOrgIcon;

    @ViewInject(R.id.edit_org_broadcast)
    private EditText m_etBroadcast;

    @ViewInject(R.id.id_horizontalScrollView)
    private MyHorizontalScrollView m_hsPhotoOrg;
    private HorizontalScrollViewAdapter m_hsAdapterOrg;

    @ViewInject(R.id.id_horizontalScrollView_member)
    private MyHorizontalScrollView m_hsPhotoMember;
    private HorizontalScrollViewAdapter m_hsAdapterMember;

    private  int m_nPull;
    private Context mContext;
    private View m_view;
    private EditText jionOrgSummary;
    private Button joinOrg,cancleJoin;
    private String m_sOldBroadcast;//记录原来的公告，避免更新失败
    private int m_nJoinState=0;//当前状态，加入或者退出
    protected Boolean m_bIsBoss = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        m_nPull = intent.getIntExtra("m_nPullIntent",0);
        Org org = DataManager.getInstance().getM_dataOrg().GetData(m_nPull);
        int m_nME = DataManager.getInstance().getM_nMe();
        m_bIsBoss = (m_nME == org.getOrgBoss());
        Boolean bMember = org.isOrgMember(m_nME);
        if (bMember) {
            if (m_bIsBoss) //如果自己是社长
                renderBoss();
            else
                renderMember();
        }else {
            renderNoMember();
        }

        //orgName.setTypeface(FontManager.getInstance().GetFace("lantingxihei"));
        orgName.setText(org.getName());
        orgMember.setText(org.getM_hashMember().size() + "位成员");
        List<String> listConfig = DataManager.getInstance().getM_dataConfig().getM_listOrgType();
        orgType.setText(listConfig.get(org.getType()));
        orgSummary.setText(" " + org.getSummary());

        listConfig = DataManager.getInstance().getM_dataConfig().getM_listCollege();
        orgCollege.setText(listConfig.get(org.getOwntype()));
        x.image().bind(m_vpOrgIcon, Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ORG, m_nPull));
        if(org.getAnnounce().length() <=0)
            m_etBroadcast.setText("暂时还没有新的公告！");
        else
            m_etBroadcast.setText(org.getAnnounce());
        initBroadcast();
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
        m_hsPhotoOrg.setCurrentImageChangeListener(new MyHorizontalScrollView.CurrentImageChangeListener() {
            @Override
            public void onCurrentImgChanged(int position,
                                            View viewIndicator) {

               // viewIndicator.setBackgroundColor(Color
                 //       .parseColor("#AA024DA4"));
            }
        });
        //添加点击回调
        m_hsPhotoOrg.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
               // view.setBackgroundColor(Color.parseColor("#AA024DA4"));
                String sUrl = m_hsAdapterOrg.getmDatas().get(position);
                showPhotoPopupWindow(view,sUrl);
            }
        });
        //设置适配器
        m_hsAdapterOrg = new HorizontalScrollViewAdapter(this);
        renderOrgPhotos();


        m_hsPhotoMember.setCurrentImageChangeListener(new MyHorizontalScrollView.CurrentImageChangeListener() {
            @Override
            public void onCurrentImgChanged(int position,
                                            View viewIndicator) {
              //  viewIndicator.setBackgroundColor(Color
                      //  .parseColor("#AA024DA4"));
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
        renderOrgMemberPhotos();
    }

    protected void initBroadcast()
    {
        m_etBroadcast.setFocusable(false);
        m_etBroadcast.setFocusableInTouchMode(false);
//        m_etBroadcast.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//// 此处为得到焦点时的处理内容
//                } else {
//// 此处为失去焦点时的处理内容
//                    onUpdateBroadcastClick();
//                }
//            }
//        });
    }

    //渲染社长ui状态
    protected void renderBoss()
    {
        orgApprove.setText("申请列表");
        orgApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgDetailActivity.this, JoinOrgListActivity.class);
                intent.putExtra("m_nOrgID", m_nPull);
                startActivity(intent);
            }
        });
        m_btnBroadCastUp.setVisibility(View.VISIBLE);
        m_btnPhotoWall.setVisibility(View.VISIBLE);
    }

    //渲染普通成员ui状态
    protected void renderMember()
    {
        m_nJoinState = NetProtocol.JOINSTATE_DELETE;
        orgApprove.setText("申请退出");
        orgApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        m_btnBroadCastUp.setVisibility(View.INVISIBLE);
        m_btnPhotoWall.setVisibility(View.INVISIBLE);
    }

    //渲染非成员ui状态
    protected void renderNoMember()
    {
        m_nJoinState = NetProtocol.JOINSTATE_ADD;
        orgApprove.setText("申请加入");
        orgApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        m_btnBroadCastUp.setVisibility(View.INVISIBLE);
        m_btnPhotoWall.setVisibility(View.INVISIBLE);
    }

    //渲染相册控件
    protected void renderOrgPhotos()
    {
        m_hsAdapterOrg.getmDatas().clear();
        Org org = DataManager.getInstance().getM_dataOrg().GetData(m_nPull);
        ArrayList<String> listPhoto = org.getM_listPhoto();
        for(int i=0;i<listPhoto.size();i++)
        {
            m_hsAdapterOrg.getmDatas().add(Utils.configFileUrl(NetProtocol.FILEHANDLE_ORG,m_nPull,listPhoto.get(i)));
        }
        m_hsPhotoOrg.initDatas(m_hsAdapterOrg);
    }

    //渲染相册控件
    protected void renderOrgMemberPhotos()
    {
        m_hsAdapterMember.getmDatas().clear();
        Org org = DataManager.getInstance().getM_dataOrg().GetData(m_nPull);
        HashMap<Integer, Integer> listMember = org.getM_hashMember();
        Iterator iter = listMember.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int)entry.getKey();
            m_hsAdapterMember.getmDatas().add(Utils.configFileUrl(NetProtocol.FILEHANDLE_USER,key,"0.jpg"));
        }
        m_hsPhotoMember.initDatas(m_hsAdapterMember);
    }

    private void onApplyJoinOrg(int state) throws DbException {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.JOINNORG);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("orgid", m_nPull);
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
                NetHandler hd = netManager.GetNetHandler(NetProtocol.JOINNORG);
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
                    onApplyJoinOrg(m_nJoinState);
                    Toast.makeText(OrgDetailActivity.this, "您已提交申请,请等待审核", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showPhotoPopupWindow(View view,String sUrl){
        PopWindows.showPhotoPopupWindow(this,view,sUrl);
    }

    //上传logo或者普通图片
    private void addPhoto(int nType)
    {
        if(!m_bIsBoss)return;//只有社长可以更新
        Intent intent = new Intent(this,PhotoSelectActivity.class);
        intent.putExtra("m_nType", NetProtocol.FILEHANDLE_ORG);
        intent.putExtra("m_nImageType", nType);
        startActivity(intent);
    }

    @Event(value = R.id.btn_photo_member )
    private void onPhotoMemberClick(View view)  {
        //先拉取成员信息 todo
        //首先检查缓存中是否有，没有再拉取
        String sParam = "";
        Org og = DataManager.getInstance().getM_dataOrg().GetData(m_nPull);
        Iterator iter = og.getM_hashMember().entrySet().iterator();
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
        if(sParam.length() <=0)
        {
            Intent intent = new Intent(this, MemberListActivity.class);
            intent.putExtra("m_nOwnerType", NetProtocol.PULLALLID_ORG);
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
                bundle.putInt("type", NetProtocol.PULLALLID_ORG);
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

    //上传照片
    @Event(value = R.id.st_tupian )
    private void onAddLogoClick(View view)  {
        addPhoto(NetProtocol.FILEHANDLE_TYPE_LOGO);
    }

    //上传照片
    @Event(value = R.id.btn_photo_wall )
    private void onAddPhotoClick(View view)  {
            addPhoto(NetProtocol.FILEHANDLE_TYPE);
    }

    //编辑公告
    @Event(value = R.id.edit_org_broadcast )
    private void onEditBroadcastClick(View view)  {
        if(!m_bIsBoss)return;//只有社长可以更新
        m_etBroadcast.setFocusableInTouchMode(true);
        m_etBroadcast.setFocusable(true);
        m_sOldBroadcast = m_etBroadcast.getText().toString();
    }

    //返回
    @Event(value = R.id.btn_back )
    private void onBackClick(View view)  {
//        Intent intent=new Intent();
//        intent.setClass(OrgDetailActivity.this, ZHTabActivity.class);
//        startActivity(intent);
        OrgDetailActivity.this.finish();
    }


    //更新公告
    @Event(value = R.id.btn_broadcast_update )
    private void onUpdateBroadcastClick(View view)  {
        if(!m_bIsBoss)return;//只有社长可以更新
        m_etBroadcast.setEnabled(false);
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.UPDATEORG);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("id", m_nPull);
        params.addParameter("announce", m_etBroadcast.getText().toString());
        Org org = DataManager.getInstance().getM_dataOrg().GetData(m_nPull);
        org.setAnnounce(m_etBroadcast.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int nErrercode = jsonObject.getInt("errorcode");
                    if (nErrercode == 0)
                        Toast.makeText(x.app(), "更新成功", Toast.LENGTH_LONG).show();
                    ;
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

        m_etBroadcast.setFocusable(false);
        m_etBroadcast.setFocusableInTouchMode(false);
    }

    //创建活动
    @Event(value = R.id.btn_create_act )
    private void onCreateActClick(View view)  {
        Intent intent = new Intent(OrgDetailActivity.this, CreateActActivity.class);
        intent.putExtra("m_nPullIntent", m_nPull);//传递当前社团id
        startActivity(intent);
    }

    //查看当前社团所有活动
    @Event(value = R.id.btn_orgact )
    private void onShowActsClick(View view)  {
        Intent intent = new Intent(OrgDetailActivity.this, OrgActListActivity.class);
        intent.putExtra("m_nPullIntent", m_nPull);//传递当前社团id
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUserDetail(MEUserDetails data) {

        if(data.getM_nType() != NetProtocol.PULLALLID_ORG)
            return;

        Intent intent = new Intent(OrgDetailActivity.this, MemberListActivity.class);
        intent.putExtra("m_nOwnerType",NetProtocol.PULLALLID_ORG);
        intent.putExtra("m_nOwner",m_nPull);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoSelected(MEPhotoSelected data) {
        //处理图片上传
        if(data.getM_nType() != NetProtocol.FILEHANDLE_ORG)
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
                        if(sPhoto.length() == 0) {
                            x.image().bind(m_vpOrgIcon, Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ORG, m_nPull));
                        }
                        else
                        {
                            Org og = DataManager.getInstance().getM_dataOrg().GetData(m_nPull);
                            og.setPhoto(sPhoto);
                            og.photoStr2Hash();
                            renderOrgPhotos();
                        }

                    }
                    ;
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


}



