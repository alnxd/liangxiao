package zhoushi.ist.activity.ist.actuvity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import zhoushi.ist.R;
import zhoushi.ist.activity.BaseActivity;
import zhoushi.ist.dal.Dal_JAA;
import zhoushi.ist.dal.Dal_JOA;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.JoinActApply;
import zhoushi.ist.entity.JoinOrgApply;
import zhoushi.ist.eventbus.MessageEvevtApplyJoinActDetail;
import zhoushi.ist.eventbus.MessageEvevtApplyJoinOrgDetail;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;

/**
 * Created by wang on 2016/6/5.
 */

@ContentView(R.layout.approvejoin_applyorg)
public class JoinActListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    @ViewInject(R.id.approveList)
    private ListView approveOrgList;

    @ViewInject(R.id.constact_all)
    private Button approveOrgAgree;

    @ViewInject(R.id.constact_group)
    private Button getApproveOrgDisagree;

    int m_nActID;
    int m_nPull;
    int m_nRenderState;//记录当前ui列表渲染状态（审核或者未审核）
    SimpleAdapter mAdapter;
    ArrayList<HashMap<String, Object>> m_listItemData = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        m_nActID = intent.getIntExtra("actid",0);
        //EventBus.getDefault().register(this);
        mAdapter = new SimpleAdapter(this,m_listItemData,R.layout.simpleadapterjoinorg_layout,new String[]{"photoImage","idList"},new int[]{R.id.photoImage,R.id.idList});
        approveOrgList.setAdapter(mAdapter);
        approveOrgAgree.setOnClickListener(this);
        getApproveOrgDisagree.setOnClickListener(this);
        approveOrgList.setOnItemClickListener(this);
        renderList();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.constact_all:
                m_nRenderState = 1;
                break;
            case R.id.constact_group:
                m_nRenderState = 0;
                break;
        }
        renderList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
    }

    public void renderList()
    {
        m_listItemData.clear();
        Dal_JAA dl = DataManager.getInstance().getM_dataJAA();
        Iterator iter = dl.getM_listData().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            JoinActApply jaa = (JoinActApply) entry.getValue();
            if(jaa.getActid() != m_nActID)continue;//选取属于本活动的申请

            if (m_nRenderState == 0) {//未审批
                if(jaa.getState() == 0)
                {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("photoImage", R.mipmap.ic_launcher);
                    map.put("idList", "来自" + jaa.getApplyer() + "同学的申请");
                    map.put("item_data",jaa.getId());
                    m_listItemData.add(map);
                }
            }
            else {
                if(jaa.getState() != 0)
                {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("photoImage", R.mipmap.ic_launcher);
                    map.put("idList","来自"+jaa.getApplyer()+"同学的申请" );
                    map.put("item_data",jaa.getId());
                    m_listItemData.add(map);
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,Object> map = (HashMap<String,Object>)mAdapter.getItem(position);
        m_nPull = (int)map.get("item_data");
        Intent intent = new Intent(this, JoinActDetailActivity.class);
        intent.putExtra("m_nPullIntent",m_nPull);
        startActivity(intent);
    }
}
