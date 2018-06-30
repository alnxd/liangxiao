package zhoushi.ist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import zhoushi.ist.R;
import zhoushi.ist.activity.MemberListActivity;
import zhoushi.ist.activity.PhotoSelectActivity;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.Org;
import zhoushi.ist.entity.Student;
import zhoushi.ist.eventbus.MEPhotoSelected;
import zhoushi.ist.eventbus.MEUserDetails;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.utils.FileSizeUtils;
import zhoushi.ist.utils.Utils;


public class PersonFragment extends Fragment implements View.OnClickListener{

    private static PersonFragment ourInstance = new PersonFragment();
    public static PersonFragment instance() {
        return ourInstance;
    }
    private ImageView m_ivPhoto;
    private TextView m_tvNo;
    private TextView m_tvName;
    private TextView m_tvCollege;
    private TextView m_tvOrg;
    private TextView m_tvAct;
    private EditText m_tvSummary;
    private ImageButton m_btnUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fourth_fragment, null);
        m_ivPhoto = (ImageView) view.findViewById(R.id.my_tupian);
        m_ivPhoto.setOnClickListener(this);
        m_tvName = (TextView) view.findViewById(R.id.tv_my_name);
        m_tvCollege = (TextView) view.findViewById(R.id.my_fanText);
        m_tvNo = (TextView) view.findViewById(R.id.my_memberText);
        m_tvOrg = (TextView) view.findViewById(R.id.my_createOrgText);
        m_tvAct = (TextView) view.findViewById(R.id.my_createActText);
        m_tvSummary = (EditText) view.findViewById(R.id.tv_summary);
        m_btnUpdate= (ImageButton) view.findViewById(R.id.btn_up_summary);
        m_btnUpdate.setOnClickListener(this);
        rednerUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        rednerUI();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            NetManager.getInstance().pullUserData(NetProtocol.LOGIC_PERSON,Integer.toString(DataManager.getInstance().getM_nMe()));
        } else {
            //相当于Fragment的onPause
        }
    }

    public void rednerUI()
    {
        int nMyID = DataManager.getInstance().getM_nMe();
        Student st = DataManager.getInstance().getM_dataStudent().GetData(nMyID);
        if(st == null)return;
        x.image().bind(m_ivPhoto, Utils.configFileUrl(NetProtocol.FILEHANDLE_USER,
                nMyID, "0.jpg"));
        m_tvName.setText(st.getSname());

        m_tvNo.setText(st.getName());
        m_tvCollege.setText(DataManager.getInstance().getM_dataConfig().getM_listClass().get(st.getCollege()));

        int nOrgID = 0;
        if(st.getM_listCO().size() > 0)
            nOrgID = st.getM_listCO().get(0);
        else if(st.getM_listJO().size()>0)
            nOrgID = st.getM_listJO().get(0);
        Org og = DataManager.getInstance().getM_dataOrg().GetData(nOrgID);
        if(og != null)
            m_tvOrg.setText(og.getName());
        else
            m_tvOrg.setText("还未拥有社团");


        String sActs = "";
        Act act = null;
        for(int nID : st.getM_listCA())
        {
            act = DataManager.getInstance().getM_dataAct().GetData(nID);
            if(act == null)continue;
            sActs+=act.getName();
            sActs+= " ";
        }
        for(int nID : st.getM_listJA())
        {
            act = DataManager.getInstance().getM_dataAct().GetData(nID);
            if(act == null)continue;
            sActs+=act.getName();
            sActs+= " ";
        }
        m_tvAct.setText(sActs);

        if(st.getSummary().length() <= 0)
            m_tvSummary.setText("点击这里编辑属于你的简介吧！");
        else
            m_tvSummary.setText(st.getSummary());
       //m_tvSummary.setFocusable(false);
        //m_tvSummary.setFocusableInTouchMode(false);
        m_tvSummary.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_tupian:
                 addPhoto();
                break;
            case R.id.btn_up_summary:
                onUpdateSummaryClick();
                break;
        }
    }

    //上传logo或者普通图片
    private void addPhoto()
    {
        Intent intent = new Intent(this.getActivity(),PhotoSelectActivity.class);
        intent.putExtra("m_nType", NetProtocol.FILEHANDLE_USER);
        intent.putExtra("m_nImageType", NetProtocol.FILEHANDLE_TYPE_LOGO);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoSelected(MEPhotoSelected data) {
        //处理图片上传
        if(data.getM_nType() != NetProtocol.FILEHANDLE_USER)
            return;

        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.FILEHANDLER);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("type", data.getM_nType());
        params.addParameter("opertype",NetProtocol.FILEHANDLE_ADD);
        params.addParameter("phototype", data.getM_nImageType());
        params.addParameter("id", DataManager.getInstance().getM_nMe());
        params.addBodyParameter("upfiles", new File(DataManager.getInstance().getM_listPhotoPath().get(0)));
        Toast.makeText(x.app(), "头像上传成功", Toast.LENGTH_LONG).show();
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int nErrercode = jsonObject.getInt("errorcode");
                    if (nErrercode == 0) {
                        x.image().bind(m_ivPhoto, Utils.configLogoFileUrl(NetProtocol.FILEHANDLE_USER,
                                DataManager.getInstance().getM_nMe()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    //更新个人简介
    private void onUpdateSummaryClick()  {
        int nMyID = DataManager.getInstance().getM_nMe();
        Student st = DataManager.getInstance().getM_dataStudent().GetData(nMyID);
        st.setSummary(m_tvSummary.getText().toString());

        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.UPDATEUSER);
        params.addParameter("id", DataManager.getInstance().getM_nMe());
        params.addParameter("summary", m_tvSummary.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int nErrercode = jsonObject.getInt("errorcode");
                    if (nErrercode == 0)
                        Toast.makeText(x.app(), "更新成功", Toast.LENGTH_LONG).show();
                    ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

        if(data.getM_nType() != NetProtocol.LOGIC_PERSON)
            return;

        rednerUI();
    }
}
