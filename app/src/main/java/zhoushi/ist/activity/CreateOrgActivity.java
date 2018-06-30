package zhoushi.ist.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zhoushi.ist.R;
import zhoushi.ist.controls.WheelView;
import zhoushi.ist.dal.DataConfig;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.eventbus.MECreateORG;
import zhoushi.ist.eventbus.MEPhotoSelected;
import zhoushi.ist.eventbus.MessageEvent;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;

/**
 * Created by wang on 2016/5/24.
 */
@ContentView(R.layout.zhoushi_createorg)
public class CreateOrgActivity extends BaseActivity {

    @ViewInject(R.id.apply_org)
    private Button m_btnApplyOrg;

    @ViewInject(R.id.edit_org_categary)
    private EditText edit_org_categary;

    @ViewInject(R.id.edit_org_name)
    private EditText edit_org_name;

    @ViewInject(R.id.edit_org_yuanxi)
    private EditText edit_org_yuanxi;

    @ViewInject(R.id.edit_org_level)
    private EditText edit_org_level;

    @ViewInject(R.id.edit_org_summary)
    private EditText edit_org_summary;

    private int m_orgType;//社团类型
    private int m_orgOwner;//社团所属院系
    private int m_orgLevel;//社团等级

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        edit_org_categary.setText(DataManager.getInstance().getM_dataConfig().getM_listOrgType().get(1));
        edit_org_yuanxi.setText(DataManager.getInstance().getM_dataConfig().getM_listCollege().get(1));
        edit_org_level.setText(DataManager.getInstance().getM_dataConfig().getM_listOrgLevel().get(1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void  renderWheelView(final int nType)
    {
        String sTitle = "请选择社团类型";
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

        int nOffset = 2;
        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(nOffset);
        wv.setItems(listData);
        wv.setSeletion(1);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                int nRealType = selectedIndex - 2;
                if (nType == DataConfig.CREATETYPE)//社团类型
                {
                    m_orgType = nRealType;
                    edit_org_categary.setText(item);
                } else if (nType == DataConfig.CREATEOWNERTYPE)//学院类型
                {
                    m_orgOwner = nRealType;
                    edit_org_yuanxi.setText(item);
                } else if (nType == DataConfig.CREATELEVEL)//等级
                {
                    m_orgLevel = nRealType;
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
                        //   m_orgType = which;
                    }
                })
                .show();
    }

    @Event(value = R.id.apply_org )
    private void onCreateOrgClick(View view) throws DbException {
        if(edit_org_name.getText().length() <= 0
                || edit_org_summary.getText().length() <= 0
                || edit_org_categary.getText().length() <= 0
                || edit_org_yuanxi.getText().length() <=0
                || edit_org_level.getText().length() <=0)
        {
            Toast.makeText(x.app(), "请填写完整的申请信息", Toast.LENGTH_LONG).show();
            return;
        }

        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.CREATEORG);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("name",edit_org_name.getText());
        params.addParameter("max",10);
        params.addParameter("level", m_orgLevel);
        params.addParameter("summary", edit_org_summary.getText());
        params.addParameter("type", m_orgType);
        params.addParameter("ownertype", m_orgOwner);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.v("CreateOrgActivity", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.CREATEORG);
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

    @Event(value = R.id.edit_org_categary )
    private void onOrgCategaryClick(View view)  {
        renderWheelView(DataConfig.CREATETYPE);
    }

    @Event(value = R.id.edit_org_yuanxi )
    private void onOrgCollegeClick(View view)  {
        renderWheelView(DataConfig.CREATEOWNERTYPE);
    }

    @Event(value = R.id.edit_org_level )
    private void onOrgLevelClick(View view)  {
        renderWheelView(DataConfig.CREATELEVEL);
    }

    @Event(value = R.id.btn_upfile )
    private void onUpFileClick(View view)  {
        Intent intent = new Intent(this,PhotoSelectActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hanldeCreateOrg(MECreateORG message) {
        Toast.makeText(x.app(),"申请成功，请耐心等待管理员审批", Toast.LENGTH_LONG).show();
        this.finish();
    }

    //返回
    @Event(value = R.id.btn_back )
    private void onBackClick(View view)  {
//        Intent intent=new Intent();
//        intent.setClass(OrgDetailActivity.this, ZHTabActivity.class);
//        startActivity(intent);
        CreateOrgActivity.this.finish();
    }
}
