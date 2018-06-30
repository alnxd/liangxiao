package zhoushi.ist.network;

import android.os.Message;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import zhoushi.ist.dal.Dal_CAA;
import zhoushi.ist.dal.Dal_COA;
import zhoushi.ist.dal.DataCoaSimple;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.CreateActApply;
import zhoushi.ist.entity.CreateOrgApply;
import zhoushi.ist.eventbus.MessageEventApproveOrg;

/**
 * Created by wang on 2016/6/4.
 */
public class ApproveOrgHandler extends NetHandler{

    private int m_nErrorcode;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
        try {
            JSONObject object = new JSONObject(result);
            m_nErrorcode = object.getInt("errorcode");
            Dal_COA dl = DataManager.getInstance().getM_dataCOA();
            if(m_nErrorcode == 0)//只有服务器返回成功的情况下，刷新缓存才有意义
            {
                JSONArray jsonArray = object.getJSONArray("successlist");
                for (int i =0;i<jsonArray.length();i++){
                    int nApplyID = jsonArray.getInt(i);
                    CreateOrgApply coa = dl.GetData(nApplyID);
                    coa.setState(1);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().post(new MessageEventApproveOrg(m_nErrorcode));
    }
}
