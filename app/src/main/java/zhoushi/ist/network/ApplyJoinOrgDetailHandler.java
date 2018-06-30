package zhoushi.ist.network;

import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.Iterator;

import zhoushi.ist.dal.Dal_COA;
import zhoushi.ist.dal.Dal_JOA;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.CreateOrgApply;
import zhoushi.ist.entity.JoinOrgApply;
import zhoushi.ist.eventbus.MessageEvevtApplyJoinOrgDetail;

/**
 * Created by wang on 2016/6/5.
 */
public class ApplyJoinOrgDetailHandler extends NetHandler {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;

        String result = msg.getData().getString("result");

        try {
            JSONObject object = new JSONObject(result);
            JSONObject list = object.getJSONObject("applyjoinOrg");
            Iterator iterator = list.keys();
            String key;
            JSONObject value;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                value = list.getJSONObject(key);
                Gson gson = new Gson();
                JoinOrgApply joinOrgApply = gson.fromJson(value.toString(),JoinOrgApply.class);
                Dal_JOA dal_joa =  DataManager.getInstance().getM_dataJOA();
                if(dal_joa.HasData(joinOrgApply.getId()))
                {
                    dal_joa.UpdateData(joinOrgApply.getId(),joinOrgApply);
                }
                else
                {
                    dal_joa.AddData(joinOrgApply.getId(),joinOrgApply);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().post(new MessageEvevtApplyJoinOrgDetail());
    }


}
