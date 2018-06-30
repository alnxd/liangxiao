package zhoushi.ist.network;

import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import zhoushi.ist.dal.Dal_Act;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.ActNative;
import zhoushi.ist.eventbus.MEActItemDetails;
import zhoushi.ist.eventbus.MEOrgItemDetails;

/**
 * Created by wang on 2016/4/28.
 */
public class PullActDetailsHandle extends NetHandler {


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
        int nType = msg.getData().getInt("type");
        try {
            JSONObject object = new JSONObject(result);
            JSONObject list = object.getJSONObject("actlist");
            Iterator iterator = list.keys();
            String key;
            JSONObject value;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                value = list.getJSONObject(key);
                Gson gson = new Gson();
                ActNative act = gson.fromJson(value.toString(),ActNative.class);
                Act at = new Act(act);
                Dal_Act dact =  DataManager.getInstance().getM_dataAct();
                if(dact.HasData(act.getId()))
                {
                    dact.UpdateData(act.getId(),at);
                }
                else
                {
                    dact.AddData(act.getId(),at);
                }

        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MEActItemDetails meDetails = new MEActItemDetails();
        meDetails.setM_nType(nType);
        EventBus.getDefault().post( meDetails);
    }
}
