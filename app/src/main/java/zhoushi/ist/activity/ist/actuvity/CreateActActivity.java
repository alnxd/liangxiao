package zhoushi.ist.activity.ist.actuvity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import zhoushi.ist.R;
import zhoushi.ist.activity.BaseActivity;
import zhoushi.ist.activity.PhotoSelectActivity;
import zhoushi.ist.controls.WheelView;
import zhoushi.ist.dal.DataConfig;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.eventbus.MessageEvent;
import zhoushi.ist.eventbus.MessageEventCreateAct;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;

/**
 * Created by wang on 2016/5/24.
 */
@ContentView(R.layout.zhoushi_createact)
public class CreateActActivity extends BaseActivity {

    @ViewInject(R.id.apply_org)
    private Button m_btnApplyOrg;

    @ViewInject(R.id.edit_org_name)
    private EditText edit_org_name;

    @ViewInject(R.id.edit_org_categary)
    private EditText edit_org_categary;

    @ViewInject(R.id.edit_org_yuanxi)
    private EditText edit_org_yuanxi;

    @ViewInject(R.id.edit_org_level)
    private EditText edit_org_level;

    @ViewInject(R.id.edit_org_summary)
    private EditText edit_org_summary;

    private int m_actType;//活动类型
    private int m_actOwner;//活动所属院系
    private int m_actLevel;//活动等级

    private int m_nOrgID;//对应社团id
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        m_nOrgID = intent.getIntExtra("m_nPullIntent",0);

        edit_org_categary.setText(DataManager.getInstance().getM_dataConfig().getM_listOrgType().get(1));
        edit_org_yuanxi.setText(DataManager.getInstance().getM_dataConfig().getM_listCollege().get(1));
        edit_org_level.setText(DataManager.getInstance().getM_dataConfig().getM_listOrgLevel().get(1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Event(value = R.id.apply_org )
    private void onCreateActClick(View view) throws DbException {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.CREATEACT);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("name",edit_org_name.getText());
        params.addParameter("max",10);
        params.addParameter("level",m_actLevel);
        params.addParameter("summary",edit_org_summary.getText());
        params.addParameter("starttime",DataFormat(CurruentData())/1000);
        params.addParameter("endtime",DataFormat(CurruentData())/1000);
        params.addParameter("area","周口师范学院");
        params.addParameter("type",m_actType);
        params.addParameter("ownertype", m_actOwner);
        params.addParameter("orgid", m_nOrgID);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.v("CreateOrgActivity", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.CREATEACT);
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

    private long DataFormat(String data){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=data;
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.print("转换时间戳:" + date.getTime());
        return date.getTime();
    }

    private String CurruentData(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        System.out.println("当前时间:" + date);
        return date;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CreateActEventBus(MessageEventCreateAct messageEventCreateAct) {
        Toast.makeText(x.app(),"申请成功，请耐心等待管理员审批", Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void  renderWheelView(final int nType)
    {
        String sTitle = "请选择活动类型";
        List<String> listData = null;
        if(nType == DataConfig.CREATETYPE)//社团类型
        {
            listData = DataManager.getInstance().getM_dataConfig().getM_listOrgType();
        }
        else if(nType == DataConfig.CREATEOWNERTYPE)//学院类型
        {
            sTitle = "请选择所属学院";
            listData = DataManager.getInstance().getM_dataConfig().getM_listCollege();
        }
        else if(nType == DataConfig.CREATELEVEL)//等级
        {
            sTitle = "请选择所属等级";
            listData = DataManager.getInstance().getM_dataConfig().getM_listOrgLevel();
        }

        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);
        wv.setItems(listData);
        wv.setSeletion(1);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                int nRealType = selectedIndex - 2;
                if (nType == DataConfig.CREATETYPE)//社团类型
                {
                    m_actType = nRealType;
                    edit_org_categary.setText(item);
                } else if (nType == DataConfig.CREATEOWNERTYPE)//学院类型
                {
                    m_actOwner = nRealType;
                    edit_org_yuanxi.setText(item);
                } else if (nType == DataConfig.CREATELEVEL)//等级
                {
                    m_actLevel = nRealType;
                    edit_org_level.setText(item);
                }
            }
        });

        new AlertDialog.Builder(this)
                .setTitle(sTitle)
                .setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                })
                .show();
    }


    @Event(value = R.id.edit_org_categary )
    private void onOrgCategaryClick(View view)  {
        renderWheelView(0);
    }

    @Event(value = R.id.edit_org_yuanxi )
    private void onOrgCollegeClick(View view)  {
        renderWheelView(1);
    }

    @Event(value = R.id.edit_org_level )
    private void onOrgLevelClick(View view)  {
        renderWheelView(DataConfig.CREATELEVEL);
    }
}
