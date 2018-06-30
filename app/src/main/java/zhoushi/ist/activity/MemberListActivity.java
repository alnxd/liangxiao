package zhoushi.ist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Iterator;
import java.util.Map;

import zhoushi.ist.R;
import zhoushi.ist.activity.ist.actuvity.ActDetailActivity;
import zhoushi.ist.controls.ListViewBinder;
import zhoushi.ist.dal.Dal_Act;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.Org;
import zhoushi.ist.entity.Student;
import zhoushi.ist.eventbus.MEActItemDetails;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.utils.Utils;

/**
 * Created by wang on 2016/6/5.
 */

@ContentView(R.layout.member_list)
public class MemberListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @ViewInject(R.id.list_member)
    private ListView m_lvMemberList;
    SimpleAdapter mAdapter;
    ArrayList<HashMap<String, Object>> m_listItemData = new ArrayList<HashMap<String, Object>>();

    private int m_nOwnerType;//所属类型，社团或活动
    private int m_nOwnerID;//所属id，社团或活动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        m_nOwnerType = intent.getIntExtra("m_nOwnerType",0);
        m_nOwnerID = intent.getIntExtra("m_nOwner",0);

        EventBus.getDefault().register(this);
        mAdapter = new SimpleAdapter(this, m_listItemData, R.layout.member_list_item,
                new String[]{"photo_image", "name", "college", "grade", "sex","title"},
                new int[]{R.id.photo_image, R.id.name, R.id.college, R.id.grade, R.id.sex,R.id.title});
        mAdapter.setViewBinder(new ListViewBinder());
        m_lvMemberList.setAdapter(mAdapter);
        m_lvMemberList.setOnItemClickListener(this);
        renderList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void renderList() {
        m_listItemData.clear();
        HashMap<Integer,Integer> listMember = null;
        if(m_nOwnerType == NetProtocol.PULLALLID_ORG)
        {
            Org og = DataManager.getInstance().getM_dataOrg().GetData(m_nOwnerID);
            listMember = og.getM_hashMember();
        }
        else if(m_nOwnerType == NetProtocol.PULLALLID_ACT)
        {
            Act at = DataManager.getInstance().getM_dataAct().GetData(m_nOwnerID);
            listMember = at.getM_hashMember();
        }

        Iterator iter = listMember.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int nID = (int)entry.getKey();
            int nTitle = (int)entry.getValue();
            Student st = DataManager.getInstance().getM_dataStudent().GetData(nID);
            HashMap<String, Object> map = new HashMap<>();
            map.put("photo_image", Utils.configFileUrl(NetProtocol.FILEHANDLE_USER, nID, "0.jpg"));
            map.put("name", st.getSname()==null?"某某":st.getSname());
            map.put("college",DataManager.getInstance().getM_dataConfig().getM_listClass().get(st.getCollege()));
            map.put("grade", st.getGrade()+"级");
            map.put("sex", (st.getSex() == 0)?"男":"女");
            map.put("title", (nTitle == 0)?"管理员":"成员");
            map.put("item_data", st.getId());
            m_listItemData.add(map);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//       // HashMap<String, Object> map = (HashMap<String, Object>) mAdapter.getItem(position);
//       // m_nItemID = (int) map.get("item_data");
//       // pullActDetails(m_nItemID);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getActDetail(MEActItemDetails data) {
//        Intent intent = new Intent(this, ActDetailActivity.class);
//        intent.putExtra("m_nPullIntent", m_nItemID);
//        startActivity(intent);
    }

    //返回
    @Event(value = R.id.btn_back )
    private void onBackClick(View view)  {
//        Intent intent=new Intent();
//        intent.setClass(OrgDetailActivity.this, ZHTabActivity.class);
//        startActivity(intent);
        MemberListActivity.this.finish();
    }
}