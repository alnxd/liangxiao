package zhoushi.ist.network;

import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import zhoushi.ist.dal.Dal_CAA;
import zhoushi.ist.dal.Dal_COA;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.CreateActApply;
import zhoushi.ist.entity.CreateOrgApply;
import zhoushi.ist.eventbus.MessageEventCreateActApplyBackGround;
import zhoushi.ist.eventbus.MessageEventCreateOrgApplyBackGround;

/**
 * Created by wang on 2016/5/30.
 */
public class PullCreateActDetailHandler extends NetHandler {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");

        try {
            JSONObject object = new JSONObject(result);
            JSONObject list = object.getJSONObject("createactlist");
            Iterator iterator = list.keys();
            String key;
            JSONObject value;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                value = list.getJSONObject(key);
                Gson gson = new Gson();
                CreateActApply createActApply = gson.fromJson(value.toString(),CreateActApply.class);
                Dal_CAA dal_caa =  DataManager.getInstance().getM_dataCAA();
                if(dal_caa.HasData(createActApply.getId()))
                {
                    dal_caa.UpdateData(createActApply.getId(),createActApply);
                }
                else
                {
                    dal_caa.AddData(createActApply.getId(),createActApply);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        EventBus.getDefault().post(new MessageEventCreateActApplyBackGround());


    }
}
