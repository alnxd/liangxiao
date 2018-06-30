package zhoushi.ist.activity.office;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
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
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import zhoushi.ist.R;
import zhoushi.ist.activity.BaseActivity;
import zhoushi.ist.dal.Dal_CAA;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.CreateActApply;
import zhoushi.ist.eventbus.MessageEventApproveACT;
import zhoushi.ist.fragment.OfficeActFragment;
import zhoushi.ist.fragment.OfficeOrgFragment;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;

/**
 * Created by wang on 2016/5/30.
 */



   @ContentView(R.layout.background_actapply)
public class OfficeApplyActActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.edit_org_name)
    private EditText m_eOrgName;

    @ViewInject(R.id.edit_org_categary)
    private EditText m_eOrgCategary;

    @ViewInject(R.id.edit_org_yuanxi)
    private EditText m_eOrgYuanxi;

    @ViewInject(R.id.edit_org_level)
    private EditText m_eOrgLevel;

    @ViewInject(R.id.edit_org_summary)
    private EditText m_eOrgSummary;

    @ViewInject(R.id.apply_org_agree)
    private Button m_bOrgApplyAgree;

    @ViewInject(R.id.apply_org_disagree)
    private Button m_bOrgAppplyDisagree;

    private  int m_nPull;
    private int m_nErrorCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        m_nPull  = intent.getIntExtra("m_nPullIntent",0);
        Dal_CAA dal_caa = DataManager.getInstance().getM_dataCAA();
        CreateActApply createActApply = dal_caa.GetData(m_nPull);
        m_eOrgName.setText(createActApply.getName());
        m_eOrgCategary.setText(DataManager.getInstance().getM_dataConfig().getM_listOrgType().get(createActApply.getType()));
        m_eOrgYuanxi.setText(DataManager.getInstance().getM_dataConfig().getM_listCollege().get(createActApply.getOwnertype()));
        m_eOrgLevel.setText(DataManager.getInstance().getM_dataConfig().getM_listOrgLevel().get(createActApply.getLevel()));
        m_eOrgSummary.setText(createActApply.getSummary());
        m_bOrgApplyAgree.setOnClickListener(this);
        m_bOrgAppplyDisagree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apply_org_agree:
                try {
                    onApproveACTApplyClick(0);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.apply_org_disagree:
                try {
                    onApproveACTApplyClick(1);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    private void onApproveACTApplyClick(int m_state) throws DbException {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.APPROVEACT);
        params.addParameter("userid",DataManager.getInstance().getM_nMe());
        params.addParameter("createactlist",m_nPull);
        params.addParameter("opertypelist",m_state);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("BackGround_CreateACT", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);

                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.APPROVEACT);
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
    public void ApproveACTApply(MessageEventApproveACT messageEventApproveAct){
        m_nErrorCode = messageEventApproveAct.getnErroecode();
        if (m_nErrorCode == 0) {
            Toast.makeText(x.app(),"审批成功", Toast.LENGTH_LONG).show();
            this.finish();
            OfficeActFragment.instance().renderList();
        }
    }

}
