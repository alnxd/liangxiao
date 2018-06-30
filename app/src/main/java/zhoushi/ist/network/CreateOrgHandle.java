package zhoushi.ist.network;

import android.os.Message;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import zhoushi.ist.eventbus.MECreateORG;
import zhoushi.ist.eventbus.MessageEvent;

/**
 * Created by wang on 2016/5/29.
 */
public class CreateOrgHandle extends NetHandler {

    private int m_nErrorCode,m_createOrgId;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
        try {
            JSONObject jsonObject = new JSONObject(result);
            m_nErrorCode = jsonObject.getInt("errorcode");
            m_createOrgId = jsonObject.getInt("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //通知消息给业务层

        EventBus.getDefault().post(
                new MECreateORG(m_nErrorCode,m_createOrgId));
    }


}
