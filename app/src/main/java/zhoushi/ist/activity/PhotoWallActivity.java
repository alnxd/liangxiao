package zhoushi.ist.activity;
import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.GridView;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import zhoushi.ist.R;
import zhoushi.ist.controls.GridViewAdapter;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.Org;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.utils.Utils;

@ContentView(R.layout.activity_photowall)
public class PhotoWallActivity extends BaseActivity {

    @ViewInject(R.id.photo_wall)
    private GridView m_gv;

    private GridViewAdapter m_adapter;
    public final static String[] imageUrls = new String[]{
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
            NetProtocol.NETROOTORGIMG,
    };

    public final static String[] imageMemberUrls = new String[]{
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
            NetProtocol.NETROOTUSERIMG,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int nType = intent.getIntExtra("m_nType", 0);
        int nID = intent.getIntExtra("m_nID",0);
        m_adapter = new GridViewAdapter(this, imageUrls);
        m_gv.setAdapter(m_adapter);
        if(nType == NetProtocol.PULLALLID_ORG) {
            Org og = DataManager.getInstance().getM_dataOrg().GetData(nID);
            fillAdapter(nType,nID,og.getM_listPhoto());

        }
        else if(nType == NetProtocol.PULLALLID_ACT)
        {
            Act at = DataManager.getInstance().getM_dataAct().GetData(nID);
            fillAdapter(nType,nID,at.getM_listPhoto());
        }

        m_adapter.notifyDataSetChanged();
    }

    protected void fillAdapter(int nType,int nID,ArrayList<String> list) {
        m_adapter.imageUrls = new String[list.size()];
       for(int i=0;i<list.size();i++)
       {
           m_adapter.imageUrls[i] = Utils.configFileUrl(nType,nID,list.get(i));
       }
    }
}