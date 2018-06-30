package zhoushi.ist.network;

import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import zhoushi.ist.dal.Dal_COA;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.CreateOrgApply;
import zhoushi.ist.eventbus.MessageEventCreateOrgApplyBackGround;

/**
 * Created by wang on 2016/5/30.
 */
public class PullCreateOrgDetailHandler extends NetHandler {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");

        try {
            JSONObject object = new JSONObject(result);
            JSONObject list = object.getJSONObject("createorglist");
            Iterator iterator = list.keys();
            String key;
            JSONObject value;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                value = list.getJSONObject(key);
                Gson gson = new Gson();
                CreateOrgApply createOrgApply = gson.fromJson(value.toString(),CreateOrgApply.class);
                Dal_COA dal_coa =  DataManager.getInstance().getM_dataCOA();
                if(dal_coa.HasData(createOrgApply.getId()))
                {
                    dal_coa.UpdateData(createOrgApply.getId(),createOrgApply);
                }
                else
                {
                    dal_coa.AddData(createOrgApply.getId(),createOrgApply);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        EventBus.getDefault().post(new MessageEventCreateOrgApplyBackGround());


    }
}
