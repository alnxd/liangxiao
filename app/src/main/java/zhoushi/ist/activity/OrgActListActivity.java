package zhoushi.ist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

import zhoushi.ist.R;
import zhoushi.ist.activity.ist.actuvity.ActDetailActivity;
import zhoushi.ist.activity.ist.actuvity.CreateActActivity;
import zhoushi.ist.dal.Dal_Act;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.Org;
import zhoushi.ist.eventbus.MEActItemDetails;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;

/**
 * Created by wang on 2016/6/5.
 */

@ContentView(R.layout.org_actlist_activity)
public class OrgActListActivity extends BaseActivity implements AdapterView.OnItemClickListener {



    @ViewInject(R.id.btn_create_act)
    private ImageButton m_btnCreateAct;

    @ViewInject(R.id.sv_org_actlist)
    private ListView m_lvActList;
    SimpleAdapter mAdapter;
    ArrayList<HashMap<String, Object>> m_listItemData = new ArrayList<HashMap<String, Object>>();

    private int m_nOrgID;
    private int m_nItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        m_nOrgID = intent.getIntExtra("m_nPullIntent",0);
        Org org = DataManager.getInstance().getM_dataOrg().GetData(m_nOrgID);
        int m_nME = DataManager.getInstance().getM_nMe();
        if (m_nME != org.getOrgBoss())
        {
            m_btnCreateAct.setVisibility(View.INVISIBLE);
        }

        EventBus.getDefault().register(this);
        mAdapter = new SimpleAdapter(this, m_listItemData, R.layout.orgitem,
                new String[]{"photo_image", "list_name", "list_tips", "member_num", "timestamp"},
                new int[]{R.id.photo_image, R.id.list_name, R.id.author, R.id.content, R.id.timestamp});
        m_lvActList.setAdapter(mAdapter);
        m_lvActList.setOnItemClickListener(this);
        renderList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void renderList() {
        m_listItemData.clear();
        Org og = DataManager.getInstance().getM_dataOrg().GetData(m_nOrgID);
        Dal_Act dl = DataManager.getInstance().getM_dataAct();
        for (int i = 0; i < og.getM_listAct().size(); i++) {
            int nActID = og.getM_listAct().get(i);
            Act at = DataManager.getInstance().getM_dataAct().GetData(nActID);
            HashMap<String, Object> map = new HashMap<>();
            map.put("photo_image", R.mipmap.ic_launcher);
            map.put("list_name", at.getName());
            map.put("member_num", "已有成员" + at.getM_hashMember().size());
            map.put("timestamp", "截至日期" + at.getEndtime().split(" ")[0]);
            map.put("item_data", at.getId());
            m_listItemData.add(map);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> map = (HashMap<String, Object>) mAdapter.getItem(position);
        m_nItemID = (int) map.get("item_data");
        pullActDetails(m_nItemID);
    }

    public void pullActDetails(int id) {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLACTDETAIL);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("roletype", 0);
        params.addParameter("actidlist", id + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("ZHTabActivity", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putInt("type", 1);
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

    //创建活动
    @Event(value = R.id.btn_create_act )
    private void onCreateActClick(View view)  {
        Intent intent = new Intent(this, CreateActActivity.class);
        intent.putExtra("m_nPullIntent", m_nOrgID);//传递当前社团id
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getActDetail(MEActItemDetails data) {
        if(data.getM_nType() != 1)
            return;

        Intent intent = new Intent(this, ActDetailActivity.class);
        intent.putExtra("m_nPullIntent", m_nItemID);
        startActivity(intent);
    }

    //返回
    @Event(value = R.id.btn_back )
    private void onBackClick(View view)  {
//        Intent intent=new Intent();
//        intent.setClass(OrgDetailActivity.this, ZHTabActivity.class);
//        startActivity(intent);
        OrgActListActivity.this.finish();
    }
}