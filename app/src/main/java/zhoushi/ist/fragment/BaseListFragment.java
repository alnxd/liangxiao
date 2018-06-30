package zhoushi.ist.fragment;

import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import krelve.view.Kanner;
import zhoushi.ist.R;
import zhoushi.ist.activity.ZHTabActivity;
import zhoushi.ist.bll.OrgItemData;
import zhoushi.ist.controls.RoundImageView;
import zhoushi.ist.controls.floatingsearchviewdemo.data.ColorSuggestion;
import zhoushi.ist.controls.floatingsearchviewdemo.data.ColorWrapper;
import zhoushi.ist.controls.floatingsearchviewdemo.data.DataHelper;
import zhoushi.ist.dal.Dal_Act;
import zhoushi.ist.dal.Dal_Base;
import zhoushi.ist.dal.Dal_Org;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.Org;
import zhoushi.ist.eventbus.MessageEventInit;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.utils.Utils;


public class BaseListFragment extends BaseFragment implements OnRefreshListener,
        OnLoadMoreListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener
{

    protected SwipeToLoadLayout m_stlLayout;
    protected SimpleAdapter mAdapter;
    protected ArrayList<HashMap<String, Object>> m_listItemData = new ArrayList<HashMap<String, Object>>();
    protected ScrollView m_scrollView = null;
    protected ListView m_listView;
    protected int m_nTypeID;//当前类型ID（社团或者活动）
    protected int m_nItemID;//点击item的ID
    protected Kanner m_kanner;//自动滚动控件
    protected FloatingSearchView mSearchView;//搜索框
    protected boolean mIsInitFinished = false;
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    // 用户输入字符时激发该方法
    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    // 单击搜索按钮时激发该方法
    @Override
    public boolean onQueryTextSubmit(String query) {
        // 实际应用中应该在该方法内执行实际查询
        // 此处仅使用Toast显示用户输入的查询内容
        //Toast.makeText(this, "您的选择是:" + query, Toast.LENGTH_SHORT).show();

        if(query.length() <= 0)
            return false;

        m_listItemData.clear();
        ArrayList<Integer> listMatch = null;
        if(m_nTypeID == NetProtocol.PULLALLID_ORG) {
            listMatch = DataManager.getInstance().getM_dataOrg().MatchData(query);
            for (int i=0;i<listMatch.size();i++)
            {
                Org org = DataManager.getInstance().getM_dataOrg().GetData(listMatch.get(i));
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("photo_image", Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ORG, org.getId()));
                map.put("list_name", org.getName());
                String summ = org.getSummary();
                if(summ.length() > 10)
                    map.put("list_tips", summ.substring(0,10)+"...");
                else
                    map.put("list_tips", summ);

                map.put("member_num", org.getM_hashMember().size()+"位同学已加入");
                map.put("timestamp", "创建日期："+"\n"+org.getStarttime().split(" ")[0]);
                map.put("item_data", org.getId());
                m_listItemData.add(map);
            }
        }
        else if(m_nTypeID == NetProtocol.PULLALLID_ACT) {
            listMatch = DataManager.getInstance().getM_dataAct().MatchData(query);
            for (int i=0;i<listMatch.size();i++)
            {
                Act act = DataManager.getInstance().getM_dataAct().GetData(listMatch.get(i));
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("photo_image", Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ACT, act.getId()));
                map.put("list_name", act.getName());
                String summ = act.getSummary();
                if(summ.length() > 10)
                    map.put("list_tips", summ.substring(0,10)+"...");
                else
                    map.put("list_tips", summ);

                map.put("member_num", act.getM_hashMember().size()+"位同学已加入");
                map.put("timestamp", "截至日期："+"\n"+act.getEndtime().split(" ")[0]);
                map.put("item_data", act.getId());
                m_listItemData.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();

        return false;
    }

    //渲染搜索结果
    protected void renderSearchList(ArrayList<Integer> list) {
        if(list.size() <= 0 )return;
        m_listItemData.clear();
        for (int i=0;i<list.size();i++)
        {
            if(m_nTypeID == NetProtocol.PULLALLID_ORG)
            {
                Org org = DataManager.getInstance().getM_dataOrg().GetData(list.get(i));
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("photo_image", Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ORG, org.getId()));
                map.put("list_name", org.getName());
                String summ = org.getSummary();
                if(summ.length() > 10)
                    map.put("list_tips", summ.substring(0,10)+"...");
                else
                    map.put("list_tips", summ);

                map.put("member_num", org.getM_hashMember().size()+"位同学已加入");
                map.put("timestamp", "创建日期："+"\n"+org.getStarttime().split(" ")[0]);
                map.put("item_data", org.getId());
                m_listItemData.add(map);
            }
            else if(m_nTypeID == NetProtocol.PULLALLID_ACT) {
                Act act = DataManager.getInstance().getM_dataAct().GetData(list.get(i));
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("photo_image", Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ACT, act.getId()));
                map.put("list_name", act.getName());
                String summ = act.getSummary();
                if(summ.length() > 10)
                    map.put("list_tips", summ.substring(0,10)+"...");
                else
                    map.put("list_tips", summ);

                map.put("member_num", act.getM_hashMember().size()+"位同学已加入");
                map.put("timestamp", "截至日期："+"\n"+act.getEndtime().split(" ")[0]);
                map.put("item_data", act.getId());
                m_listItemData.add(map);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    protected void renderKanner() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //m_scrollView.smoothScrollTo(0, 20);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            autoRefresh();
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onDestroy() {
        m_kanner.removeCallbacksAndMessages();
        super.onDestroy();
    }

    @Override
    public void onLoadMore() {
//        m_stlLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                m_stlLayout.setLoadingMore(false);
//            }
//        }, 1000);
    }

    @Override
    public void onRefresh() {
    }

    protected void autoRefresh() {
        if(!mIsInitFinished)return;
        //m_scrollView.smoothScrollTo(0, 20);
        DataHelper.initSuggestionsList(m_nTypeID);
        m_listItemData.clear();
        if (m_nTypeID == NetProtocol.PULLALLID_ORG)
            RenderOrgItem();
        else if (m_nTypeID == NetProtocol.PULLALLID_ACT)
            RenderActItem();
    }

    protected void RenderOrgItem( )
    {
        //Utils.logTime("RenderOrgItem begin");
        Dal_Org dl = DataManager.getInstance().getM_dataOrg();
        Iterator iter = dl.getM_listData().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Org org = (Org)entry.getValue();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("photo_image", Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ORG, org.getId()));
            //map.put("photo_image", "http://182.254.242.101:8009/static/1/46/logo/0.jpg");
            map.put("list_name", org.getName());
            String summ = org.getSummary();
            if(summ.length() > 10)
               map.put("list_tips", summ.substring(0,10)+"...");
            else
                map.put("list_tips", summ);

            map.put("member_num", org.getM_hashMember().size()+"位同学已加入");
            map.put("timestamp", "创建日期："+"\n"+org.getStarttime().split(" ")[0]);
            map.put("item_data", org.getId());
            m_listItemData.add(map);

        }
        mAdapter.notifyDataSetChanged();
        //Utils.logTime("RenderOrgItem end");
    }

    protected void RenderActItem()
    {
        Dal_Act dl = DataManager.getInstance().getM_dataAct();
        Iterator iter = dl.getM_listData().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Act act = (Act)entry.getValue();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("photo_image", Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_ACT, act.getId()));
            //map.put("photo_image", "http://182.254.242.101:8009/static/1/46/logo/0.jpg");
            map.put("list_name", act.getName());
            String summ = act.getSummary();
            if(summ.length() > 10)
                map.put("list_tips", summ.substring(0,10)+"...");
            else
                map.put("list_tips", summ);

            map.put("member_num", act.getM_hashMember().size()+"位同学已加入");
            map.put("timestamp", "截至日期："+"\n"+act.getEndtime().split(" ")[0]);
            map.put("item_data", act.getId());
            m_listItemData.add(map);

        }
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void renderList(MessageEventInit message) {
        autoRefresh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ZHTabActivity pt = (ZHTabActivity)this.getActivity();
        HashMap<String,Object> map = (HashMap<String,Object>)mAdapter.getItem(position);
        m_nItemID= (int)map.get("item_data");
        if(m_nTypeID == NetProtocol.PULLALLID_ORG)
            pt.pullOrgDetails(m_nItemID);
        else if(m_nTypeID == NetProtocol.PULLALLID_ACT)
            pt.pullActDetails(m_nItemID);
    }

    protected void setupFloatingSearch(View v)
    {
        mSearchView = (FloatingSearchView) v.findViewById(R.id.sv_top);
        String sHint = (m_nTypeID == NetProtocol.PULLALLID_ORG)?"搜索社团":"搜索活动";
        mSearchView.setSearchHint(sHint);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();
                    //simulates a query call to a data source
                    //with a new query.
                    DataHelper.findSuggestions( newQuery, 5, FIND_SUGGESTION_SIMULATED_DELAY,
                            new DataHelper.OnFindSuggestionsListener() {
                        @Override
                        public void onResults(List<ColorSuggestion> results) {

                            //this will swap the data and
                            //render the collapse/expand animations as necessary
                            mSearchView.swapSuggestions(results);

                            //let the users know that the background
                            //process has completed
                            mSearchView.hideProgress();
                        }
                    });
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                ColorSuggestion colorSuggestion = (ColorSuggestion) searchSuggestion;
                int nID = colorSuggestion.getUserData();
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(nID);
                renderSearchList(list);
            }

            @Override
            public void onSearchAction(String query) {
                DataHelper.findSuggestions(query, 5, FIND_SUGGESTION_SIMULATED_DELAY,
                        new DataHelper.OnFindSuggestionsListener() {
                            @Override
                            public void onResults(List<ColorSuggestion> results) {
                                ArrayList<Integer> list = new ArrayList<Integer>();
                                for(ColorSuggestion cs : results)
                                {
                                    list.add(cs.getUserData());
                                }
                                renderSearchList(list);
                            }
                        });
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.clearQuery();
                autoRefresh();
            }

            @Override
            public void onFocusCleared() {

            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item){
            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHamburger"
        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {

                // Log.d(TAG, "onMenuOpened()");
                //mDrawerLayout.openDrawer(GravityCompat.START);
            }

            @Override
            public void onMenuClosed() {
                //Log.d(TAG, "onMenuClosed()");
            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                //Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
//                ColorSuggestion colorSuggestion = (ColorSuggestion) item;
//
//                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
//                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";
//
//                if (colorSuggestion.getIsHistory()) {
//                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.ic_history_black_24dp, null));
//
//                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
//                    leftIcon.setAlpha(.36f);
//                } else {
//                    leftIcon.setAlpha(0.0f);
//                    leftIcon.setImageDrawable(null);
//                }
//
//                textView.setTextColor(Color.parseColor(textColor));
//                String text = colorSuggestion.getBody()
//                        .replaceFirst(mSearchView.getQuery(),
//                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
//                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                // mSearchResultsList.setTranslationY(newHeight);
            }
        });
    }
}
