package zhoushi.ist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import zhoushi.ist.dal.Dal_JOA;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.JoinOrgApply;
import zhoushi.ist.entity.Student;
import zhoushi.ist.eventbus.MEUserDetails;
import zhoushi.ist.eventbus.MessageEventApproveJoinOrg;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.utils.Utils;

/**
 * Created by wang on 2016/6/6.
 */

@ContentView(R.layout.font_joinorgapply)
public class JoinOrgDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_upfile)
    private ImageView m_ivPhoto;

    @ViewInject(R.id.tv_apply_name)
    private TextView m_tvName;

    @ViewInject(R.id.edit_org_name)
    private EditText m_eOrgName;

    @ViewInject(R.id.edit_org_id)
    private EditText m_eOrgId;

    @ViewInject(R.id.edit_org_title)
    private EditText m_eOrgTitle;

    @ViewInject(R.id.edit_org_state)
    private EditText m_eOrgState;


    @ViewInject(R.id.edit_org_reason)
    private EditText m_eOrgReason;

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

        Intent intent = getIntent();
        m_nPull  = intent.getIntExtra("m_nPullIntent",0);
        m_bOrgApplyAgree.setOnClickListener(this);
        m_bOrgAppplyDisagree.setOnClickListener(this);

        renderUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void renderUI()
    {
        Dal_JOA dal_joa = DataManager.getInstance().getM_dataJOA();
        JoinOrgApply joinOrgApply =dal_joa.GetData(m_nPull);
        Student st = DataManager.getInstance().getM_dataStudent().GetData(joinOrgApply.getApplyer());
        if(st != null)
        {
            x.image().bind(m_ivPhoto, Utils.configFileUrl(NetProtocol.FILEHANDLE_USER, st.getId(), "0.jpg"));
            m_tvName.setText(st.getSname());
            m_eOrgName.setText(DataManager.getInstance().getM_dataConfig().getM_listClass().get(st.getClass()));
            m_eOrgId.setText(st.getGrade()+"级");
            m_eOrgTitle.setText("普通社员");
            String strState = joinOrgApply.getOptype()==NetProtocol.JOINSTATE_ADD?"申请加入":"申请退出";
            m_eOrgState.setText(strState);
            m_eOrgReason.setText(joinOrgApply.getReason());
        }
        else
        {
           onPullStudentDetailClick(joinOrgApply.getApplyer());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apply_org_agree:
                try {
                    onApproveJoinOrgApplyClick(0);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.apply_org_disagree:
                try {
                    onApproveJoinOrgApplyClick(1);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                break;
        }
    }



    private void onApproveJoinOrgApplyClick(int m_state) throws DbException {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.APPROVEJOINORG);
        params.addParameter("userid",DataManager.getInstance().getM_nMe());
        params.addParameter("applylist", m_nPull);
        params.addParameter("approvelist",m_state);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("BackGround_CreateOrg", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);

                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.APPROVEJOINORG);
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
    public void ApproveOrgApply(MessageEventApproveJoinOrg messageEventApproveJoinOrg){
        m_nErrorCode = messageEventApproveJoinOrg.getnErroecode();
        if (m_nErrorCode == 0) {
            this.finish();
           // Intent intent = new Intent(JoinOrgDetailActivity.this,JoinOrgListActivity.class);
           // startActivity(intent);
        }
    }

    private void onPullStudentDetailClick(int id) {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLUSERDETAIL);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("roletype", 0);
        params.addParameter("idlist", id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putInt("type", NetProtocol.PULLALLID_APPLY_JOIN_ORG);
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

        if(data.getM_nType() != NetProtocol.PULLALLID_APPLY_JOIN_ORG)
            return;

        renderUI();
    }
}
