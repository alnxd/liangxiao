package zhoushi.ist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import zhoushi.ist.R;
import zhoushi.ist.activity.office.OfficeApplyActActivity;
import zhoushi.ist.dal.Dal_CAA;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.CreateActApply;
import zhoushi.ist.eventbus.MessageEventCreateActApplyBackGround;
import zhoushi.ist.eventbus.MessageEventInit;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;

/**
 * Created by wang on 2016/5/20.
 */
public class OfficeActFragment extends Fragment implements OnRefreshListener,OnLoadMoreListener, AdapterView.OnItemClickListener, View.OnClickListener {

    SwipeToLoadLayout swipeToLoadLayout;
    ListView listView;
    SimpleAdapter mAdapter;
    ArrayList<HashMap<String, Object>> m_listItemData = new ArrayList<>();
    int m_nPull;
    int m_nRenderState;//记录当前ui列表渲染状态（审核或者未审核）

    private Button m_bOrgApproveAgree;
    private Button m_bOrgApproveDisagree;

    private static OfficeActFragment ourInstance = new OfficeActFragment();
    public static OfficeActFragment instance() {
        return ourInstance;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_background_act, null);
        swipeToLoadLayout = (SwipeToLoadLayout)view.findViewById(R.id.swipeToLoadLayout);
        m_bOrgApproveAgree = (Button) view.findViewById(R.id.constact_all);
        m_bOrgApproveDisagree = (Button) view.findViewById(R.id.constact_group);
        listView= (ListView)view.findViewById(R.id.swipe_target);
        m_bOrgApproveAgree.setOnClickListener(this);
        m_bOrgApproveDisagree.setOnClickListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        mAdapter = new SimpleAdapter(getActivity(),m_listItemData,R.layout.orgitem,
                new String[]{"photo_image","list_name","list_tips","member_num","timestamp"},
                new int[]{R.id.photo_image,R.id.list_name,R.id.author,R.id.content,R.id.timestamp});

        listView.setOnItemClickListener(this);
        listView.setAdapter(mAdapter);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
                renderList();
            }
        });
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

    public void renderList()
    {
        m_listItemData.clear();
        Dal_CAA dl = DataManager.getInstance().getM_dataCAA();
        Iterator iter = dl.getM_listData().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            CreateActApply caa = (CreateActApply) entry.getValue();
            if (m_nRenderState == 0 ) {//未审批
                if (caa.getState() == 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("photo_image", R.mipmap.zs_yonghu1);
                    map.put("list_name", caa.getName());
                    map.put("list_tips", caa.getSummary());
                    map.put("item_data", caa.getId());
                    m_listItemData.add(map);
                }
            }
            else
            {
                if (caa.getState() != 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("photo_image", R.mipmap.zs_yonghu1);
                    map.put("list_name", caa.getName());
                    map.put("list_tips", caa.getSummary());
                    map.put("item_data", caa.getId());
                    m_listItemData.add(map);
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pullEventBus(MessageEventInit message) {
        if(message.getM_nType() == NetProtocol.PULLALLID_CREATE_ACT)
            autoRefresh();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  pullCreateACTDetailEventBus(MessageEventCreateActApplyBackGround messageEventCreateActApplyBackGround){
        int m_nPullIntent = m_nPull;
        Intent intent = new Intent(getActivity(), OfficeApplyActActivity.class);
        intent.putExtra("m_nPullIntent",m_nPullIntent);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,Object> map = (HashMap<String,Object>)mAdapter.getItem(position);
        m_nPull = (int)map.get("item_data");
        try {
            onPullCreateActDetailClick(m_nPull);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }



    private void onPullCreateActDetailClick(int m_nPull) throws DbException{
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLCREATEACTDETAIL);
        params.addParameter("userid",DataManager.getInstance().getM_nMe());
        params.addParameter("roletype",DataManager.getInstance().getM_nRoleType());
        params.addParameter("idlist",m_nPull);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("OfficeOrgFragment", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);

                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.PULLCREATEACTDETAIL);
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
