package zhoushi.ist.network;

import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zhoushi.ist.dal.Dal_Act;
import zhoushi.ist.dal.Dal_CAA;
import zhoushi.ist.dal.Dal_COA;
import zhoushi.ist.dal.Dal_JAA;
import zhoushi.ist.dal.Dal_JOA;
import zhoushi.ist.dal.Dal_Org;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.ActNative;
import zhoushi.ist.entity.CreateActApply;
import zhoushi.ist.entity.CreateOrgApply;
import zhoushi.ist.entity.JoinActApply;
import zhoushi.ist.entity.JoinOrgApply;
import zhoushi.ist.entity.Org;
import zhoushi.ist.entity.OrgNative;
import zhoushi.ist.eventbus.MessageEventInit;
import zhoushi.ist.utils.Utils;

/**
 * Created by wang on 2016/4/26.
 */
public class PullAllOrgHandle extends NetHandler {
    @Override
    public void handleMessage(Message msg) {
        int type = 0;
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
        try {
            JSONObject object = new JSONObject(result);
            type = object.getInt("type");
            if (type == NetProtocol.PULLALLID_ORG) {
                FillOrgCache(object);
            }else if (type == NetProtocol.PULLALLID_ACT){
                FillActCache(object);
            }else if (type == NetProtocol.PULLALLID_CREATE_ORG){
                FillCreateOrgCache(object);
            }else if (type == NetProtocol.PULLALLID_CREATE_ACT){
                FillCreateActCache(object);
            }else if (type == NetProtocol.PULLALLID_APPLY_JOIN_ORG){
                FillJoinOrgCache(object);
            }else if (type == NetProtocol.PULLALLID_APPLY_JOIN_ACT){
                FillJoinActCache(object);
            }

            MessageEventInit me = new MessageEventInit();
            me.setM_nType(type);
            EventBus.getDefault().post(me);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FillOrgCache(JSONObject js)
    {
        try {
            JSONArray list = js.getJSONArray("datalist");
            for (int i = 0; i < list.length(); i++)
            {
                JSONObject value = (JSONObject)list.get(i);
                Gson gson = new Gson();
                OrgNative org = gson.fromJson(value.toString(), OrgNative.class);
                Org og = new Org(org);
                Dal_Org dorg = DataManager.getInstance().getM_dataOrg();
                if (dorg.HasData(og.getId())) {
                    dorg.UpdateData(og.getId(), og);
                } else {
                    dorg.AddData(og.getId(), og);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FillActCache(JSONObject js)
    {
        try {
            JSONArray list = js.getJSONArray("datalist");
            for (int i = 0; i < list.length(); i++)
            {
                JSONObject value = (JSONObject)list.get(i);
                Gson gson = new Gson();
                ActNative act = gson.fromJson(value.toString(), ActNative.class);
                Act at = new Act(act);
                Dal_Act dact = DataManager.getInstance().getM_dataAct();
                if (dact.HasData(at.getId())) {
                    dact.UpdateData(at.getId(), at);
                } else {
                    dact.AddData(at.getId(), at);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FillJoinOrgCache(JSONObject js)
    {
        try {
            JSONArray list = js.getJSONArray("datalist");
            for (int i = 0; i < list.length(); i++)
            {
                JSONObject value = (JSONObject)list.get(i);
                Gson gson = new Gson();
                JoinOrgApply joa = gson.fromJson(value.toString(), JoinOrgApply.class);
                Dal_JOA dJoa = DataManager.getInstance().getM_dataJOA();
                if (dJoa.HasData(joa.getId())) {
                    dJoa.UpdateData(joa.getId(), joa);
                } else {
                    dJoa.AddData(joa.getId(), joa);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FillJoinActCache(JSONObject js)
    {
        try {
            JSONArray list = js.getJSONArray("datalist");
            for (int i = 0; i < list.length(); i++)
            {
                JSONObject value = (JSONObject)list.get(i);
                Gson gson = new Gson();
                JoinActApply jaa = gson.fromJson(value.toString(), JoinActApply.class);
                Dal_JAA dJaa = DataManager.getInstance().getM_dataJAA();
                if (dJaa.HasData(jaa.getId())) {
                    dJaa.UpdateData(jaa.getId(), jaa);
                } else {
                    dJaa.AddData(jaa.getId(), jaa);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FillCreateOrgCache(JSONObject js)
    {
        try {
            JSONArray list = js.getJSONArray("datalist");
            for (int i = 0; i < list.length(); i++)
            {
                JSONObject value = (JSONObject)list.get(i);
                Gson gson = new Gson();
                CreateOrgApply coa = gson.fromJson(value.toString(), CreateOrgApply.class);
                Dal_COA dCoa = DataManager.getInstance().getM_dataCOA();
                if (dCoa.HasData(coa.getId())) {
                    dCoa.UpdateData(coa.getId(), coa);
                } else {
                    dCoa.AddData(coa.getId(), coa);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FillCreateActCache(JSONObject js)
    {
        try {
            JSONArray list = js.getJSONArray("datalist");
            for (int i = 0; i < list.length(); i++)
            {
                JSONObject value = (JSONObject)list.get(i);
                Gson gson = new Gson();
                CreateActApply caa = gson.fromJson(value.toString(), CreateActApply.class);
                Dal_CAA dCaa = DataManager.getInstance().getM_dataCAA();
                if (dCaa.HasData(caa.getId())) {
                    dCaa.UpdateData(caa.getId(), caa);
                } else {
                    dCaa.AddData(caa.getId(), caa);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
