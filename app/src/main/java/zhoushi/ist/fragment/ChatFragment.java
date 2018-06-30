package zhoushi.ist.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import zhoushi.ist.R;
import zhoushi.ist.dal.Dal_Act;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.network.NetProtocol;


public class ChatFragment extends Fragment  {

    private static ChatFragment ourInstance = new ChatFragment();
    public static ChatFragment instance() {
        return ourInstance;
    }

    private ListView m_lvBroadcastList;
    SimpleAdapter mAdapter;
    ArrayList<HashMap<String, Object>> m_listItemData = new ArrayList<HashMap<String, Object>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, null);
        m_lvBroadcastList = (ListView)view.findViewById(R.id.sv_broadcastlist);
        mAdapter = new SimpleAdapter(getActivity(), m_listItemData, R.layout.broadcast_item,
                new String[]{"title", "author", "timestamp", "content"},
                new int[]{R.id.broadcast_title, R.id.author, R.id.timestamp, R.id.content});
        m_lvBroadcastList.setAdapter(mAdapter);
        renderList();









        WebView wv = (WebView)view.findViewById(R.id.webView_gg);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);


// User settings

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点

        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放



        webSettings.setLoadWithOverviewMode(true);

/**
 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
 */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);


        wv.loadUrl(NetProtocol.NETROOTBROADCAST);
        return view;
    }

    protected void renderList()
    {
//        Dal_Act dl = DataManager.getInstance().getM_dataAct();
//        Iterator iter = dl.getM_listData().entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            Act act = (Act)entry.getValue();
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("photo_image", R.mipmap.logo);
//            map.put("list_name", act.getName());
//            String summ = act.getSummary();
//            if(summ.length() > 10)
//                map.put("list_tips", summ.substring(0,10)+"...");
//            else
//                map.put("list_tips", summ);
//
//            map.put("member_num", act.getM_hashMember().size()+"位同学已加入");
//            map.put("timestamp", "截至日期："+act.getEndtime().split(" ")[0]);
//            map.put("item_data", act.getId());
//            m_listItemData.add(map);
//
//        }
//        mAdapter.notifyDataSetChanged();
    }
}
