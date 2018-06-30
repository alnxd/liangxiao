package zhoushi.ist.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import krelve.view.Kanner;
import zhoushi.ist.R;
import zhoushi.ist.activity.OrgDetailActivity;
import zhoushi.ist.controls.ListViewBinder;
import zhoushi.ist.controls.floatingsearchviewdemo.data.ColorSuggestion;
import zhoushi.ist.controls.floatingsearchviewdemo.data.ColorWrapper;
import zhoushi.ist.controls.floatingsearchviewdemo.data.DataHelper;
import zhoushi.ist.eventbus.MEOrgItemDetails;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.utils.Utils;


public class OrgFragment extends BaseListFragment  {

    private static OrgFragment ourInstance = new OrgFragment();
    public static OrgFragment instance() {
        return ourInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // Utils.logTime(" org onCreateView begin");
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        m_nTypeID = NetProtocol.PULLALLID_ORG;//社团
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
        String[] imagesUrl ={NetProtocol.NETROOT_KANNER_ORG+"0.jpg",
                NetProtocol.NETROOT_KANNER_ORG+"1.jpg",
                NetProtocol.NETROOT_KANNER_ORG+"2.jpg",
                NetProtocol.NETROOT_KANNER_ORG+"3.jpg",
                NetProtocol.NETROOT_KANNER_ORG+"4.jpg"};
        m_kanner.setImagesUrl(imagesUrl);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOrgDetail(MEOrgItemDetails MEOrgItemDetails) {
        Intent intent = new Intent(getActivity(), OrgDetailActivity.class);
        intent.putExtra("m_nPullIntent",m_nItemID);
        startActivity(intent);
    }
}

