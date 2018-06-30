package zhoushi.ist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import krelve.view.Kanner;
import zhoushi.ist.R;
import zhoushi.ist.activity.ist.actuvity.ActDetailActivity;
import zhoushi.ist.controls.ListViewBinder;
import zhoushi.ist.eventbus.MEActItemDetails;
import zhoushi.ist.network.NetProtocol;

public class ActFragment extends BaseListFragment {

    private static ActFragment ourInstance = new ActFragment();
    public static ActFragment instance() {
        return ourInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        m_nTypeID = NetProtocol.PULLALLID_ACT;//活动
        m_kanner = (Kanner) view.findViewById(R.id.kanner);
        m_listView = (ListView) view.findViewById(R.id.swipe_target);
        mAdapter = new SimpleAdapter(getActivity(),m_listItemData,R.layout.orgitem,
                new String[]{"photo_image","list_name","list_tips","member_num","timestamp"},
                new int[]{R.id.photo_image,R.id.list_name,R.id.author,R.id.content,R.id.timestamp});
        mAdapter.setViewBinder(new ListViewBinder());
        m_listView.setAdapter(mAdapter);
        m_listView.setOnItemClickListener(this);
        m_listView.setFocusable(false);
        m_scrollView = (ScrollView) view.findViewById(R.id.act_solution_1_sv);
        setupFloatingSearch(view);
        renderKanner();
        mIsInitFinished = true;
        autoRefresh();
        m_scrollView.smoothScrollTo(0, 20);
        return view;
    }

    @Override
    protected void renderKanner() {
        String[] imagesUrl ={NetProtocol.NETROOT_KANNER_ACT+"0.jpg",
                NetProtocol.NETROOT_KANNER_ACT+"1.jpg",
                NetProtocol.NETROOT_KANNER_ACT+"2.jpg",
                NetProtocol.NETROOT_KANNER_ACT+"3.jpg",
                NetProtocol.NETROOT_KANNER_ACT+"4.jpg"};
        m_kanner.setImagesUrl(imagesUrl);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getActDetail(MEActItemDetails data) {
        if(data.getM_nType() != 0)
            return;
        Intent intent = new Intent(getActivity(), ActDetailActivity.class);
        intent.putExtra("m_nPullIntent",m_nItemID);
        startActivity(intent);
    }

}
