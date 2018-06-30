package zhoushi.ist.network;

import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import zhoushi.ist.dal.Dal_Org;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Org;
import zhoushi.ist.entity.OrgNative;
import zhoushi.ist.eventbus.MEOrgItemDetails;

/**
 * Created by wang on 2016/4/28.
 */
public class PullOrgDetailsHandle extends NetHandler {


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
        try {
            JSONObject object = new JSONObject(result);
            JSONObject list = object.getJSONObject("orglist");
            Iterator iterator = list.keys();
            String key;
            JSONObject value;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                value = list.getJSONObject(key);
                Gson gson = new Gson();
                OrgNative org = gson.fromJson(value.toString(),OrgNative.class);
                Org og = new Org(org);// org.orgMember2Hash();
                Dal_Org dorg =  DataManager.getInstance().getM_dataOrg();
                if(dorg.HasData(og.getId()))
                {
                    dorg.UpdateData(og.getId(),og);
                }
                else
                {
                    dorg.AddData(og.getId(),og);
                }

        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().post( new MEOrgItemDetails());
    }
}
